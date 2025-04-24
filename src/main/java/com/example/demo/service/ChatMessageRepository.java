package com.example.demo.service;

import com.example.demo.models.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
	List<ChatMessage> findByGroupName(String groupName);
}
