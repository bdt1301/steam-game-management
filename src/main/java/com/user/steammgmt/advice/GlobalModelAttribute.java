package com.user.steammgmt.advice;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.user.steammgmt.model.Notification;
import com.user.steammgmt.model.User;
import com.user.steammgmt.service.NotificationService;
import com.user.steammgmt.service.UserService;

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
	public void addUserAndNotificationsToModel(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails != null) {
			User user = userService.getUserByUsername(userDetails.getUsername());
			model.addAttribute("user", user);
			List<Notification> unreadNotifications = notificationService.getUnreadNotifications(user);
			model.addAttribute("unreadNotifications", unreadNotifications);
		} else {
			model.addAttribute("unreadNotifications", Collections.emptyList());
		}
	}
}
