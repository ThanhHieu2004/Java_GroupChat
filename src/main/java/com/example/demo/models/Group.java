package com.example.demo.models;

import java.util.List;

public class Group {
	private int groupID;
	private String groupName;
	private List<User> userList;
	private String groupAvatar;

	public Group() {

	}

	public Group(int groupID, String groupName, List<User> userList, String groupAvatar) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
		this.userList = userList;
		this.groupAvatar = groupAvatar;
	}

	public String getGroupAvatar() {
		return groupAvatar;
	}

	public void setGroupAvatar(String groupAvatar) {
		this.groupAvatar = groupAvatar;
	}

	public Group(int groupID, String groupName, List<User> userList) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
		this.userList = userList;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public Group(int groupID, String groupName) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
	}

}
