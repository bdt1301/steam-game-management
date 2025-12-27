package com.user.steammgmt.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

	public NotificationController(UserService userService, NotificationService notificationService,
			NavigationService navigationService) {
		this.userService = userService;
		this.notificationService = notificationService;
		this.navigationService = navigationService;
	}

	// Hiển thị danh sách notifications
	@GetMapping
	public String listNotifications(Model model, HttpSession session,
			@AuthenticationPrincipal UserDetails userDetails) {

		if (userDetails != null) {
			User user = userService.getUserByUsername(userDetails.getUsername());
			List<Notification> notifications = notificationService.getNotifications(user);
			model.addAttribute("notifications", notifications);
		} else {
			model.addAttribute("notifications", Collections.emptyList());
		}

		return "user/notifications";
	}

	// Đánh dấu tất cả là đã đọc
	@GetMapping("/markAllRead")
	public String markAllRead(HttpSession session, HttpServletRequest request,
			@AuthenticationPrincipal UserDetails userDetails) {
		navigationService.saveURL(session, "previousURL", request.getHeader("Referer"));
		if (userDetails != null) {
			User user = userService.getUserByUsername(userDetails.getUsername());
			notificationService.markAllRead(user);
		}
		return navigationService.resolveRedirectURL(session, "previousURL", List.of(), "/notifications");
	}

	// Đánh dấu một notification là đã đọc
	@PostMapping("/markAsRead/{id}")
	public String markAsRead(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

		Notification notification = notificationService.getNotificationById(id);

		if (notification != null && userDetails != null) {
			if (notification.getUser().getUsername().equals(userDetails.getUsername())) {
				notificationService.markAsRead(notification);
				if (notification.getRedirectLink() != null) {
					return "redirect:" + notification.getRedirectLink();
				}
			}
		}

		return "redirect:/notifications";
	}
}
