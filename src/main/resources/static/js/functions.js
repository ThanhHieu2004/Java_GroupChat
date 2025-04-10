let stompClient = null;

function connect() {
	const socket = new SockJS('/ws');
	stompClient = Stomp.over(socket);

	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		/* Listen to messages being sent to /topic/public */
		stompClient.subscribe('/topic/public', function(message) {
			const msg = JSON.parse(message.body);
			showMessage(msg);
		});
	});
}

window.sendMessage = function() {
	const content = document.getElementById("messageInput").value;
	stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
		content: content,
		user: {
			username: username,
			avatar: avatar
		}
	}));
	document.getElementById("messageInput").value = "";
}

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

window.onload = connect;

