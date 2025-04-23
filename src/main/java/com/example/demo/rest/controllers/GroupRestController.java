package com.example.demo.rest.controllers;

import com.example.demo.models.Group;
import com.example.demo.service.GroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupRestController {

	@Autowired
	private GroupRepository groupRepository;

	@GetMapping
	public List<Group> getAllGroups() {
		return groupRepository.findAll();
	}

	@GetMapping("/{groupName}")
	public Group getGroupByName(@PathVariable String groupName) {
		return groupRepository.findByGroupName(groupName);
	}
}
