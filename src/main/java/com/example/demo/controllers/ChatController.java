package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.models.ChatMessage;

@Controller
public class ChatController {

	private Map<String, List<ChatMessage>> groupMessageMap = new HashMap<>();

	@MessageMapping("/chat.sendMessage.{group}")
	@SendTo("/topic/groups/{group}")
	public ChatMessage sendGroupMessage(@DestinationVariable String group, ChatMessage message) {
		groupMessageMap.putIfAbsent(group, new ArrayList<>());
		groupMessageMap.get(group).add(message);
		return message;
	}

	@MessageMapping("/chat.loadHistory.{group}")
	public void loadGroupHistory(@DestinationVariable String group, SimpMessageHeaderAccessor headerAccessor) {
		List<ChatMessage> history = groupMessageMap.getOrDefault(group, new ArrayList<>());
		String sessionId = headerAccessor.getSessionId();
		String simpSessionId = headerAccessor.getSessionId();
		for (ChatMessage msg : history) {
			// Manually send each message back to client (since we can't @SendTo here)
			messagingTemplate.convertAndSend("/queue/history-" + simpSessionId, msg);
			System.out.println("Sending history for group: " + group + " to session: " + sessionId);
		}
	}

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/chat.joinGroup.{group}")
	@SendTo("/topic/groups/{group}")
	public ChatMessage userJoinedGroup(@DestinationVariable String group, ChatMessage message) {
		message.setType("JOIN");
		message.setContent(message.getUser().getUsername() + " joined the group");
		System.out.println("A user joined a group!");
		return message;
	}

}
