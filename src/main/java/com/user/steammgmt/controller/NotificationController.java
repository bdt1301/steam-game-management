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
import com.user.steammgmt.model.UserNotification;
import com.user.steammgmt.service.NavigationService;
import com.user.steammgmt.service.NotificationService;
import com.user.steammgmt.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;
    private final NavigationService navigationService;

    // Hiển thị danh sách notifications
    @GetMapping
    public String listNotifications(Model model, HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            User user = userService.getUserByUsername(userDetails.getUsername());
            List<UserNotification> userNotifications = notificationService.getNotifications(user);
            model.addAttribute("notifications", userNotifications);
        } else {
            model.addAttribute("notifications", Collections.emptyList());
        }
        return "user/notifications";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("notification", new Notification());
        return "user/add_notification";
    }

    @PostMapping("/new")
    public String createNotification(@ModelAttribute Notification notification,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getUserByUsername(userDetails.getUsername());
        notification.setCreatedBy(user);
        notificationService.createAndDispatch(notification);
        return "redirect:/notifications/new?success";
    }

    // Đánh dấu tất cả là đã đọc
    @PostMapping("/markAllRead")
    public String markAllRead(HttpSession session, HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        navigationService.saveURL(session, "previousURL", request.getHeader("Referer"));
        if (userDetails != null) {
            User user = userService.getUserByUsername(userDetails.getUsername());
            notificationService.markAllRead(user);
        }
        return navigationService.resolveRedirectURL(session, "previousURL", List.of(), "/notifications");
    }

    // Đánh dấu 1 notification là đã đọc
    @PostMapping("/markAsRead/{notificationId}")
    public String markAsRead(@PathVariable Long notificationId, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            User user = userService.getUserByUsername(userDetails.getUsername());
            UserNotification userNotification = notificationService.markAsRead(user, notificationId);
            if (userNotification != null) {
                String redirectLink = userNotification.getNotification().getRedirectLink();
                if (redirectLink != null) {
                    return "redirect:" + redirectLink;
                }
            }
        }
        return "redirect:/notifications";
    }

}
