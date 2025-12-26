package com.user.steammgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.user.steammgmt.model.User;
import com.user.steammgmt.service.UserService;

@Controller
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login")
	public String loginPage() {
		return "/user/login";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
		try {
			boolean success = userService.register(user);
			if (success) {
				redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
			} else {
				redirectAttributes.addFlashAttribute("error", "Username đã tồn tại.");
			}
		} catch (IllegalArgumentException e) {
			if ("EMAIL_EXISTS".equals(e.getMessage())) {
				redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng.");
			}
		}
		return "redirect:/login";
	}
}
