package com.user.steammgmt.model;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_notifications")
@Getter
@Setter
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @Column(nullable = false)
    private boolean isRead = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date readAt;

    public UserNotification() {
    }

    public UserNotification(User user, Notification notification) {
        this.user = user;
        this.notification = notification;
        this.isRead = false;
    }
    
}
