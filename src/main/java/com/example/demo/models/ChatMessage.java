package com.example.demo.models;

public class ChatMessage {
	private String content;
	private User user;
	private String type; // "CHAT", "JOIN", etc.

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
