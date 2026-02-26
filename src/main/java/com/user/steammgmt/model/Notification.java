package com.user.steammgmt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    // ROLE_USER, ROLE_ADMIN, ALL
    @Column(nullable = false)
    private String targetRole;

    private String redirectLink;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    public Notification() {
        this.createdAt = new Date();
    }

    public Notification(String message, String redirectLink, String targetRole, User createdBy) {
        this.message = message;
        this.redirectLink = redirectLink;
        this.targetRole = targetRole;
        this.createdBy = createdBy;
        this.createdAt = new Date();
    }

}
