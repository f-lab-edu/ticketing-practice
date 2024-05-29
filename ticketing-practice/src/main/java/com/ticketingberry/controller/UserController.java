package com.ticketingberry.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ticketingberry.model.UserCreateForm;
import com.ticketingberry.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	// GET 방식으로 넘기면 다시 회원가입 화면으로 돌아감 (잘못된 접근)
	@GetMapping("/join")
	public String join(UserCreateForm userCreateForm) {
		return "join_form";
	}
	
	@PostMapping("/join")
	// 작성한 내용을 validation 하는 것
	public String join(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		// 작성 결과가 에러나면 다시 회원가입 화면으로 돌아감
		if (bindingResult.hasErrors()) {
			return "join_form";
		}
		
		// 비밀번호와 비밀번호 확인이 같지 않으면 다시 회원가입 화면으로 돌아감
		if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			// 어디서 틀렸는지 알려주어야 함
			// bindingResult.rejectValue(필드명, 오류코드, 에러메시지)
			// 오류코드는 마음대로 정해도 됨
			bindingResult.rejectValue("password2", "passwordIncorrect", "비밀번호가 일치하지 않습니다.");
			return "join_form";
		}
		
		try {
			userService.addUser(userCreateForm.getUsername(), userCreateForm.getPassword1(), userCreateForm.getName(), userCreateForm.getEmail(),
					userCreateForm.getPhone(), userCreateForm.getBirth(), userCreateForm.getGender(), userCreateForm.getRole());
		} catch (DataIntegrityViolationException e) {	// 중복 객체가 들어오면 다시 회원가입 화면으로 돌아감
			e.printStackTrace();
			bindingResult.reject("joinFailed", e.getMessage());
			return "join_form";
		}
		
		// 에러가 나지 않으면 메인 화면으로 페이지 전환
		return "redirect:/main";
	}
	
	// 로그인 화면으로 이동
	@GetMapping("/login")
	public String login(Model model, Principal principal) {
		return "login_form";
	}
}



















