package com.ticketingberry.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ticketingberry.model.entity.User;
import com.ticketingberry.service.UserService;

@Controller
public class MainController {
	@Autowired
	private UserService userService;
	
	@GetMapping({"/", "/main"})
	public String getMainPage(Model model, Principal principal) {
		// 사용자가 인증된 경우
		if (principal != null && !"anonymousUser".equals(principal.getName())) {
			String username = principal.getName();
			User user = userService.getUser(username);
			model.addAttribute("nickname", user.getNickname());
		} 
		
		return "main";
	}
}
