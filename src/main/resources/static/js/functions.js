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

		/* Listen to messages being sent to /topic/public */
		// Subscribe to chat messages
		console.log("Trying to subscribe to /topic/public...");
		stompClient.subscribe('/topic/public', function(message) {
			const msg = JSON.parse(message.body);
			showMessage(msg);
		});

		console.log("Trying to subscribe to /topic/groups...");
		// Subscribe to group creation
		stompClient.subscribe('/topic/groups', function(message) {
			console.log("Received new group: ", message.body);
			const groupName = message.body;
			addGroupToSidebar(groupName);
		});
	});
}

let currentGroup = "public"; // default group
let messageSubscription = null;

sendMessageFn = function() {
	const content = document.getElementById("messageInput").value;
	stompClient.send(`/app/chat.sendMessage.${currentGroup}`, {}, JSON.stringify({
		content: content,
		senderUsername: username,
		senderAvatar: avatar,
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

	const isCurrentUser = msg.senderUsername === username;
	/*console.log(msg.senderUsername);*/
	const displayName = isCurrentUser ? "You" : msg.senderUsername;
	const alignmentClass = isCurrentUser ? "chat-message-right" : "chat-message-left";

	messageElement.classList.add(alignmentClass, "pb-4"); // or 'chat-message-left' depending on user
	messageElement.innerHTML = `
            <div>
                <img src="${msg.senderAvatar}"
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
function subscribeToGroup(groupName) {

	let groupchatName = document.getElementsByClassName("groupChatName")[0];
	groupchatName.innerHTML = groupName;

	// Subscribe to new group topic
	messageSubscription = stompClient.subscribe(`/topic/groups/${groupName}`, function(message) {
		try {
			const msg = JSON.parse(message.body);
			showMessage(msg);
		} catch (err) {
			console.error("Failed to parse message", err);
		}
	});
}

window.createNewGroup = function() {
	const groupName = prompt("Enter a name for your new group:");

	if (!groupName || groupName.trim() === "") {
		alert("Group name cannot be empty.");
		return;
	}

	// Wait for a bit before connection is established.
	if (!stompClient || !stompClient.connected) {
		alert("WebSocket connection not ready yet. Please wait...");
		return;
	}

	// Send group to backend
	stompClient.send("/app/group.create", {}, groupName);


}

function addGroupToSidebar(groupName) {
	fetch(`/api/groups/${groupName}`)
		.then(response => response.json())
		.then(group => {
			const groupList = document.querySelector(".list-group");
			const groupItem = document.createElement("a");
			groupItem.href = "#";
			groupItem.className = "list-group-item list-group-item-action border-0";

			groupItem.innerHTML = `
				<div class="d-flex align-items-start">
					<img src="${group.groupAvatar}" class="rounded-circle mr-1" alt="${group.groupName}" width="40" height="40">
					<div class="flex-grow-1 ml-3">
						${group.groupName}
						<div class="small text-success">
							<span class="fas fa-circle chat-online"></span> Active
						</div>
					</div>
				</div>
			`;

			groupItem.addEventListener("click", function() {
				switchGroup(groupName);
			});
			// Switch group right after creating it!
			switchGroup(groupName);

			groupList.appendChild(groupItem);
		})
		.catch(error => console.error("Failed to load group avatar:", error));

}

function clearChatBox() {
	const chatBox = document.getElementById("chatBox");
	chatBox.innerHTML = '';
}

function switchGroup(groupName) {
	const groupChat = document.querySelector(".py-2.px-4.border-bottom.d-none.d-lg-block");
	if (groupChat) groupChat.style.visibility = "visible";

	if (messageSubscription) messageSubscription.unsubscribe();

	clearChatBox();
	currentGroup = groupName;

	fetch(`/api/groups/${groupName}`)
		.then(response => response.json())
		.then(group => {
			const classAvatar = document.getElementsByClassName("classImage")[0]
			if (classAvatar) {
				classAvatar.src = group.groupAvatar || "https://cdn-icons-png.flaticon.com/512/5677/5677749.png";
			}
		})

	fetch(`/api/messages/${groupName}`)
		.then(response => response.json())
		.then(messages => {
			messages.forEach(msg => {
				showMessage(msg);
			});
		})
		.catch(error => console.error("Failed to load messages:", error));

	// Update the group name and subscribe to the new group
	groupChat.getElementsByClassName("groupChatName")[0].innerText = groupName;
	subscribeToGroup(groupName);

	console.log(`Switched to group: ${groupName}`);
}



document.addEventListener("DOMContentLoaded", function() {
	connect();
	// Fetch group from backend
	fetch("/api/groups")
		.then(response => response.json())
		.then(data => {
			data.forEach(group => {
				addGroupToSidebar(group.groupName);
			});
		})
		.catch(error => console.error("Failed to load groups:", error));
});