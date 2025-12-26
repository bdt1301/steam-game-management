package com.user.steammgmt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.user.steammgmt.model.Notification;
import com.user.steammgmt.model.User;
import com.user.steammgmt.service.NavigationService;
import com.user.steammgmt.service.NotificationService;
import com.user.steammgmt.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

	private final UserService userService;
	private final NotificationService notificationService;
	private final NavigationService navigationService;

	public NotificationController(UserService userService, NotificationService notificationService, NavigationService navigationService) {
		this.userService = userService;
		this.notificationService = notificationService;
		this.navigationService = navigationService;
	}

	@GetMapping
	public String listNotifications(Model model, HttpSession session) {
		String role = (String) session.getAttribute("role");
		String username = (String) session.getAttribute("username");
		if (username != null && "USER".equals(role)) {
			User user = userService.getUserByUsername(username);
			model.addAttribute("notifications", notificationService.getNotifications(user));
		}
		return "/user/notifications";
	}
	
	@GetMapping("/markAllRead")
	public String markAllRead(HttpSession session, HttpServletRequest request) {
		navigationService.saveURL(session, "previousURL", request.getHeader("Referer"));
		String role = (String) session.getAttribute("role");
		String username = (String) session.getAttribute("username");
		if (username != null && "USER".equals(role)) {
			User user = userService.getUserByUsername(username);
			notificationService.markAllRead(user);
		}
		return navigationService.resolveRedirectURL(session, "previousURL", List.of(), "/notifications");
	}

	@PostMapping("/markAsRead/{id}")
	public String markAsRead(@PathVariable Long id) {
		Notification notification = notificationService.getNotificationById(id);
		if (notification != null) {
			notificationService.markAsRead(notification);
		}
		return "redirect:" + notification.getRedirectLink();
	}
}
