package com.user.steammgmt.advice;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.user.steammgmt.model.Notification;
import com.user.steammgmt.model.User;
import com.user.steammgmt.service.NotificationService;
import com.user.steammgmt.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalModelAttribute {

	private final UserService userService;
	private final NotificationService notificationService;

	public GlobalModelAttribute(UserService userService, NotificationService notificationService) {
		this.userService = userService;
		this.notificationService = notificationService;
	}

	@ModelAttribute
	public void addUserToModel(Model model, HttpSession session) {
		String role = (String) session.getAttribute("role");
		String username = (String) session.getAttribute("username");
		if (username != null && "USER".equals(role)) {
			User user = userService.getUserByUsername(username);
			model.addAttribute("user", user);
			List<Notification> unreadNotifications = notificationService.getUnreadNotifications(user);
			model.addAttribute("unreadNotifications", unreadNotifications);
		}
	}

}
