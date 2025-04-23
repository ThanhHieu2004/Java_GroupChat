package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.models.Group;
import com.example.demo.service.GroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GroupController {
	private List<String> groups = new ArrayList<>();

	@Autowired
	private GroupRepository groupRepository;

	@MessageMapping("/group.create")
	@SendTo("/topic/groups")
	public String createGroup(String groupName) {
		if (!groupRepository.existsByGroupName(groupName)) {
			Group newGroup = new Group();
			newGroup.setGroupName(groupName);
			newGroup.setUserList(new ArrayList<>()); // Optional
			newGroup.setGroupAvatar("https://cdn-icons-png.flaticon.com/512/5677/5677749.png"); // Default avatar

			groupRepository.save(newGroup);
		}
		System.out.println("Group created and saved: " + groupName);
		return groupName;
	}
}
