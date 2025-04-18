package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.models.Group;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GroupController {
	private List<String> groups = new ArrayList<>();
	
	@MessageMapping("/group.create")
	@SendTo("/topic/groups")
	public String createGroup(String groupName) {
        if (!groups.contains(groupName)) {
            groups.add(groupName);
        }
        return groupName;
    }
}
