package com.user.steammgmt.service;

import com.user.steammgmt.model.Notification;
import com.user.steammgmt.model.User;
import com.user.steammgmt.model.UserNotification;
import com.user.steammgmt.repository.NotificationRepository;
import com.user.steammgmt.repository.UserNotificationRepository;
import com.user.steammgmt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;

    // Lấy tất cả notification của user
    public List<UserNotification> getNotifications(User user) {
        return userNotificationRepository.findByUser(user);
    }

    // Lấy notification chưa đọc
    public List<UserNotification> getUnreadNotifications(User user) {
        return userNotificationRepository.findByUserAndIsReadFalse(user);
    }

    // Lấy notification master theo id
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    // Đánh dấu 1 notification đã đọc
    public UserNotification markAsRead(User user, Long notificationId) {
        UserNotification userNotification = userNotificationRepository.findByUserAndNotificationId(user, notificationId)
                .orElse(null);
        if (userNotification != null && !userNotification.isRead()) {
            userNotification.setRead(true);
            userNotification.setReadAt(new Date());
            userNotificationRepository.save(userNotification);
        }
        return userNotification;
    }

    // Đánh dấu tất cả đã đọc
    public void markAllRead(User user) {
        List<UserNotification> unreadNotifications = userNotificationRepository.findByUserAndIsReadFalse(user);
        for (UserNotification un : unreadNotifications) {
            un.setRead(true);
            un.setReadAt(new Date());
        }
        userNotificationRepository.saveAll(unreadNotifications);
    }

    public void createAndDispatch(Notification notification) {

        notificationRepository.save(notification);

        if (notification.getRedirectLink() == null || notification.getRedirectLink().trim().isEmpty()) {
            notification.setRedirectLink("/");
        }

        List<User> users;
        if (notification.getTargetRole().equals("ALL")) {
            users = userRepository.findAll();
        } else {
            users = userRepository.findByRole(notification.getTargetRole());
        }

        List<UserNotification> mappings = users.stream()
                .map(user -> new UserNotification(user, notification))
                .toList();

        userNotificationRepository.saveAll(mappings);
    }

}
