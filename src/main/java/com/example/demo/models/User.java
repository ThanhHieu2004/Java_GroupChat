package com.example.demo.models;

public class User {
	private int id;
	private String username;
	private String avatar;
	private boolean isOnline;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public User(int id, String name, String avatar) {
		super();
		this.id = id;
		this.username = name;
		this.avatar = avatar;
		this.isOnline = true;
	}

	public User(String name, String avatar) {
		super();
		this.username = name;
		this.avatar = avatar;
		this.isOnline = true;
	}
	
	public User(String name) {
		super();
		this.username = name;
		this.avatar = createNewAvatar();
		this.isOnline = true;
	}

	public User() {
		this.isOnline = true;
	}
	
	public static String createNewAvatar() {
		String randomAvatar = "https://avatar.iran.liara.run/public/";
		int randomNum = (int) (Math.random() * 101) + 1; // 1 to 100
		return randomAvatar + String.valueOf(randomNum);
	}

}
