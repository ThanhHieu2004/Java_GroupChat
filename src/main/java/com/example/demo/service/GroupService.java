package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Group;

@Service
public class GroupService {

	@Autowired
	private GroupRepository groupRepository;

	public String createGroup(String groupName) {
		if (!groupRepository.existsByGroupName(groupName)) {
			Group newGroup = new Group();
			newGroup.setGroupName(groupName);
//			newGroup.setUserList(new ArrayList<>()); // Optional
			newGroup.setGroupAvatar("https://cdn-icons-png.flaticon.com/512/5677/5677749.png"); // Default avatar

			groupRepository.save(newGroup);
		}
		System.out.println("Group created and saved: " + groupName);
		return groupName;
	}
}
