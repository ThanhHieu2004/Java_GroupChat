const config = {
	iceServers: [
		{ urls: 'stun:stun.l.google.com:19302' } // STUN server to help peers find each other
	]
};


async function initVideoCall() {
	const video = document.getElementById("videoPlayer");
	video.style.display = "block";

	try {
		const stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
		video.srcObject = stream;
	} catch (err) {
		console.error("Failed to access camera:", err);
	}
}

function callVideo() {
	document.getElementById("videoContainer").style.display = "block";
	videoActive = true;

	switchGroup("Video Call");
	initVideoCall(); // start video
}


