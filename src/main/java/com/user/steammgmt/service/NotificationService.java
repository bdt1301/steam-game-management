package com.user.steammgmt.service;

import com.user.steammgmt.model.Notification;
import com.user.steammgmt.model.User;
import com.user.steammgmt.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

	private final NotificationRepository notificationRepository;

	public NotificationService(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}

	public List<Notification> getNotifications(User user) {
		return notificationRepository.findByUser(user);
	}

	public List<Notification> getUnreadNotifications(User user) {
		return notificationRepository.findByUserAndIsReadFalse(user);
	}

	public Notification getNotificationById(Long id) {
		return notificationRepository.findById(id).orElse(null);
	}

	public void markAsRead(Notification notification) {
		notification.setRead(true);
		notificationRepository.save(notification);
	}

	public void markAllRead(User user) {
		List<Notification> unreadNotifications = getUnreadNotifications(user);
		for (Notification notification : unreadNotifications) {
			notification.setRead(true);
			notificationRepository.save(notification);
		}
	}
}