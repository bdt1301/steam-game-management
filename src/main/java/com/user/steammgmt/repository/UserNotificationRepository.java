package com.user.steammgmt.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.steammgmt.model.User;
import com.user.steammgmt.model.UserNotification;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

    List<UserNotification> findByUser(User user);

    List<UserNotification> findByUserAndIsReadFalse(User user);

    Optional<UserNotification> findByUserAndNotificationId(User user, Long notificationId);

}
