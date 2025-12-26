package com.user.steammgmt.repository;

import com.user.steammgmt.model.Notification;
import com.user.steammgmt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByUser(User user);
	List<Notification> findByUserAndIsReadFalse(User user);
}