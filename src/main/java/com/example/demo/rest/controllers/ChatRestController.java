package com.example.demo.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.ChatMessage;
import com.example.demo.service.ChatMessageRepository;

@RestController
@RequestMapping("/api/messages/{groupName}")
public class ChatRestController {
	@Autowired
	private ChatMessageRepository messageRepo;

	@GetMapping
	public List<ChatMessage> getMessagesForGroup(@PathVariable String groupName) {
		return messageRepo.findByGroupName(groupName);
	}
}
