package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.models.ChatMessage;
import com.example.demo.service.ChatMessageRepository;

@Controller
public class ChatController {

	@Autowired
	private ChatMessageRepository messageRepo;

	@MessageMapping("/chat.sendMessage.{groupName}")
	@SendTo("/topic/groups/{groupName}")
	public ChatMessage sendMessage(@DestinationVariable String groupName, @Payload ChatMessage message) {
		message.setGroupName(groupName);
		// Add timestamp
		message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")));

		//
		// Save to MongoDB
		messageRepo.save(message);

		return message;
	}

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

}
