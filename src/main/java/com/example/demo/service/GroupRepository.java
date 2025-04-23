package com.example.demo.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Group;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
	boolean existsByGroupName(String groupName);
	Group findByGroupName(String groupName);
}

