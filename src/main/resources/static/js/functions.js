let stompClient = null;

function connect() {
	const socket = new SockJS('/ws');
	stompClient = Stomp.over(socket);

	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/public', function(message) {
			const msg = JSON.parse(message.body);
			showMessage(msg.sender + ": " + msg.content);
		});
	});
}

window.sendMessage = function() {
	const content = document.getElementById("messageInput").value;
	stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
		sender: username,
		content: content
	}));
	document.getElementById("messageInput").value = "";
}

/*function showMessage(text) {*/
window.showMessage = function(text) {
	const chatBox = document.getElementById("chatBox");
	const messageElement = document.createElement("div");
	messageElement.classList.add("chat-message-right", "pb-4"); // or 'chat-message-right' depending on user
	messageElement.innerHTML = `
            <div>
                <img src="${avatar}"
                    class="rounded-circle mr-1" width="40" height="40">
                <div class="text-muted small text-nowrap mt-2">${new Date().toLocaleTimeString()}</div>
            </div>
            <div class="flex-shrink-1 bg-light rounded py-2 px-3 ml-3">
                <div class="font-weight-bold mb-1">${username}</div>
                ${text}
            </div>
        `;
	chatBox.appendChild(messageElement);
	chatBox.scrollTop = chatBox.scrollHeight;
}

window.onload = connect;

