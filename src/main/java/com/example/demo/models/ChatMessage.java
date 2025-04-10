package com.example.demo.models;

public class ChatMessage {
    private User user;
    private String content;

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
}
