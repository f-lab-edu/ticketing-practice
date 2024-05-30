package com.ticketingberry.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping({"/", "/main"})
	public String getMainPage(Model model, Principal principal) {
		// 사용자가 인증되어 있지 않은 경우
		if (principal == null || "anonymousUser".equals(principal.getName())) {
			// 로그인 페이지로 리디렉션
			return "redirect:/user/login";
		}
		
		return "main";
	}
}
