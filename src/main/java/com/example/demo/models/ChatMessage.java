package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
public class ChatMessage {
	@Id
	private String id;
	private String groupName;
	private String content;
	private String senderUsername;
	private String senderAvatar;
	private String timestamp;; 

	public ChatMessage() {
	}

	public ChatMessage(String groupName, String content, String senderUsername, String senderAvatar, String timestamp) {
		this.groupName = groupName;
		this.content = content;
		this.senderUsername = senderUsername;
		this.senderAvatar = senderAvatar;
		this.timestamp = timestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSenderUsername() {
		return senderUsername;
	}

	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}

	public String getSenderAvatar() {
		return senderAvatar;
	}

	public void setSenderAvatar(String senderAvatar) {
		this.senderAvatar = senderAvatar;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
