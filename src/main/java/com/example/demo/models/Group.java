package com.example.demo.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "groups")
public class Group {
	@Id
	private String id;
	private String groupName;
	private List<User> userList;
	private String groupAvatar;

	public Group() {

	}

	public Group(String groupName, List<User> userList, String groupAvatar) {
		super();
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

	public Group(String groupName, List<User> userList) {
		super();
		this.groupName = groupName;
		this.userList = userList;
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

	public Group(String groupName) {
		super();
		this.groupName = groupName;
	}

}
