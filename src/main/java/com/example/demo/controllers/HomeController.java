package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.User;

@Controller
public class HomeController {
	List<User> activeUsers = new ArrayList<>();
	
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }
    
    @PostMapping("/chat")
    public String newUser(@RequestParam("username") String username, Model model) {
    	User user = new User(username);
    	if (!activeUsers.contains(user)) {
            activeUsers.add(user);
        }
    	model.addAttribute("user", user);
    	return "chat";
    }
}