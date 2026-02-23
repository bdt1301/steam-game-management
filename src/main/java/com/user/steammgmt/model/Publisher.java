package com.user.steammgmt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "publishers")
@Getter
@Setter
public class Publisher {
	@Id
	private String publisherId;

	@Column(nullable = false)
	private String publisherName;

	@Column(length = 1000)
	private String publisherInfo;

	@Column(length = 1000)
	private String publisherImage;

	private String linkWebsite;

	@Transient
	private Game featuredGame;

	@Transient
	private int productQuantity;

	public Publisher() {
	}

	public Publisher(String publisherId, String publisherName, String linkWebsite, String publisherInfo,
			String publisherImage) {
		this.publisherId = publisherId;
		this.publisherName = publisherName;
		this.linkWebsite = linkWebsite;
		this.publisherInfo = publisherInfo;
		this.publisherImage = publisherImage;
	}

}
