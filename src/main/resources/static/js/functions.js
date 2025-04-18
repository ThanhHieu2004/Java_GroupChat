let stompClient = null;
const messageInput = document.getElementById("messageInput");
const sendBtn = document.getElementById("sendMessageBtn");

// **** Chat Function **** //
document.addEventListener("DOMContentLoaded", function() {
	messageInput.addEventListener("input", function() {
		sendBtn.disabled = messageInput.value.trim() === "";
	});
});

function connect() {
	const socket = new SockJS('/ws');
	stompClient = Stomp.over(socket);

	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		/* Load history */
		const sessionId = stompClient.ws._transport.url.split('/').slice(-2, -1)[0]; // Extract session ID from SockJS URL
		stompClient.subscribe(`/queue/history-${sessionId}`, function(message) {
			const msg = JSON.parse(message.body);
			showMessage(msg);
		});

		/* Listen to messages being sent to /topic/public */
		// Subscribe to chat messages
		stompClient.subscribe('/topic/public', function(message) {
			const msg = JSON.parse(message.body);
			showMessage(msg);
		});

		// Subscribe to group creation
		/*stompClient.subscribe('/topic/groups', function(message) {
			const groupName = message.body;
			addGroupToSidebar(groupName);
		});*/
	});
}

sendMessageFn = function() {
	const content = document.getElementById("messageInput").value;
	stompClient.send(`/app/chat.sendMessage.${currentGroup}`, {}, JSON.stringify({
		content: content,
		user: {
			username: username,
			avatar: avatar
		}
	}));
	document.getElementById("messageInput").value = "";
	sendBtn.disabled = true;
}

messageInput.addEventListener("keypress", function(evt) {
	if (evt.key == "Enter" && sendBtn.disabled === false) {
		sendMessageFn();
	}
});


window.sendMessage = sendMessageFn;

function getCurrentTime() {
	var time = new Date().toLocaleTimeString([], {
		hour: '2-digit',
		minute: '2-digit',
		hour12: true
	});
	return time;
}

/*function showMessage(text) {*/
window.showMessage = function(msg) {
	const chatBox = document.getElementById("chatBox");
	const messageElement = document.createElement("div");

	if (msg.type === "JOIN") {
		messageElement.classList.add("text-center", "text-muted", "mb-2");
		messageElement.textContent = msg.content;
		chatBox.appendChild(messageElement);
		chatBox.scrollTop = chatBox.scrollHeight;
		return;
	}

	const isCurrentUser = msg.user.username === username;
	/*console.log(msg.user.username);*/
	const displayName = isCurrentUser ? "You" : msg.user.username;
	const alignmentClass = isCurrentUser ? "chat-message-right" : "chat-message-left";

	messageElement.classList.add(alignmentClass, "pb-4"); // or 'chat-message-left' depending on user
	messageElement.innerHTML = `
            <div>
                <img src="${msg.user.avatar}"
                    class="rounded-circle mr-1" width="40" height="40">
                <div class="text-muted small text-nowrap mt-2">${getCurrentTime()}</div>
            </div>
            <div class="flex-shrink-1 bg-light rounded py-2 px-3 ml-3">
                <div class="font-weight-bold mb-1">${displayName}</div>
                ${msg.content}
            </div>
        `;
	chatBox.appendChild(messageElement);
	chatBox.scrollTop = chatBox.scrollHeight;
}


// **** CREATE A NEW GROUP FUNCTION **** //
let currentGroup = "public"; // default group
let messageSubscription = null;
function subscribeToGroup(groupName) {
	console.log("[subscribeToGroup] Trying to subscribe to group:", groupName);

	// Unsubscribe from previous group
	if (messageSubscription) {
		console.log("[subscribeToGroup] Unsubscribing from previous group");
		messageSubscription.unsubscribe();
	}

	// Subscribe to new group topic
	messageSubscription = stompClient.subscribe(`/topic/groups/${groupName}`, function(message) {
		console.log("✅ [subscribeToGroup] Received message on group topic:", message.body);
		try {
			const msg = JSON.parse(message.body);
			showMessage(msg);
		} catch (err) {
			console.error("Failed to parse message", err);
		}
	});

	console.log("📨 [subscribeToGroup] Sending joinGroup for", groupName);

	stompClient.send(`/app/chat.joinGroup.${groupName}`, {}, JSON.stringify({
		user: {
			username: username,
			avatar: avatar
		}
	}));
}

window.createNewGroup = function() {
	const groupName = prompt("Enter a name for your new group:");

	if (!groupName || groupName.trim() === "") {
		alert("Group name cannot be empty.");
		return;
	}

	// Send group to backend
	stompClient.send("/app/group.create", {}, groupName);
}

function addGroupToSidebar(groupName) {
	const groupList = document.querySelector(".col-lg-5 .list-group");
	if (!groupList) return;

	const groupItem = document.createElement("a");
	groupItem.href = "#";
	groupItem.className = "list-group-item list-group-item-action border-0";
	groupItem.innerHTML = `
		<div class="d-flex align-items-start">
			<img src="https://cdn-icons-png.flaticon.com/512/5677/5677749.png"
				class="rounded-circle mr-1" alt="${groupName}" width="40" height="40">
			<div class="flex-grow-1 ml-3">
				${groupName}
				<div class="small text-success">
					<span class="fas fa-circle chat-online"></span> Active
				</div>
			</div>
		</div>
	`;

	groupItem.addEventListener("click", function() {
		if (currentGroup !== groupName) {
			currentGroup = groupName;

			document.getElementById("groupName").textContent = groupName;

			document.querySelectorAll(".list-group-item").forEach(el => el.classList.remove("active"));
			groupItem.classList.add("active");

			subscribeToGroup(groupName);
			clearChatBox();

			// Load history
			stompClient.send(`/app/chat.loadHistory.${groupName}`, {});
		}
	});
	/*console.log(`Added ${groupName}!`); */
	groupList.appendChild(groupItem);

}

function clearChatBox() {
	const chatBox = document.getElementById("chatBox");
	chatBox.innerHTML = '';
}


document.addEventListener("DOMContentLoaded", connect);


