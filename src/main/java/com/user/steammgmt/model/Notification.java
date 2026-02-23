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

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private String message;

	@Column(nullable = false)
	private String redirectLink;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(length = 1000)
	private String image;

	private boolean isRead;

	public Notification() {
		this.createdAt = new Date();
		this.isRead = false;
	}

	public Notification(User user, String message, String redirectLink, String image) {
		this.user = user;
		this.message = message;
		this.redirectLink = redirectLink;
		this.image = image;
		this.createdAt = new Date();
		this.isRead = false;
	}

}