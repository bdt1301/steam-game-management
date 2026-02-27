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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void createAndDispatch(Notification notification, List<Long> userIds, List<Long> gameIds) {

        notificationRepository.save(notification);

        Set<User> users;
        if (userIds != null && userIds.contains(0L)) {
            users = new HashSet<>(userRepository.findByRole("ROLE_USER"));
        } else if (userIds != null && !userIds.isEmpty()) {
            users = new HashSet<>(userRepository.findAllById(userIds));
        } else {
            users = new HashSet<>();
        }

        if (gameIds != null && !gameIds.isEmpty()) {
            Set<User> usersByGame = new HashSet<>(userRepository.findByFavoriteGames_AppIdIn(gameIds));
            if (!users.isEmpty()) {
                users.retainAll(usersByGame);
            } else {
                users = usersByGame;
            }
        }

        List<UserNotification> mappings = users.stream().map(u -> new UserNotification(u, notification)).toList();

        userNotificationRepository.saveAll(mappings);
    }

}
