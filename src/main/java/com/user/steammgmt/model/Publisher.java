package com.user.steammgmt.model;

import jakarta.persistence.*;

@Entity
@Table(name = "publishers")
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

	public String getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getLinkWebsite() {
		return linkWebsite;
	}

	public void setLinkWebsite(String linkWebsite) {
		this.linkWebsite = linkWebsite;
	}

	public String getPublisherInfo() {
		return publisherInfo;
	}

	public void setPublisherInfo(String publisherInfo) {
		this.publisherInfo = publisherInfo;
	}

	public String getPublisherImage() {
		return publisherImage;
	}

	public void setPublisherImage(String publisherImage) {
		this.publisherImage = publisherImage;
	}

	public Game getFeaturedGame() {
		return featuredGame;
	}

	public void setFeaturedGame(Game featuredGame) {
		this.featuredGame = featuredGame;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}
}
