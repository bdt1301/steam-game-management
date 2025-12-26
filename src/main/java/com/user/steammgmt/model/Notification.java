package com.user.steammgmt.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notification")
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

	// Getters & Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRedirectLink() {
		return redirectLink;
	}

	public void setRedirectLink(String redirectLink) {
		this.redirectLink = redirectLink;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean read) {
		isRead = read;
	}
}