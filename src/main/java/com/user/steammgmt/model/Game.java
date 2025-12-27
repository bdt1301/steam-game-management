package com.user.steammgmt.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "games")
public class Game {
	@Id
	private Long appId;

	@Column(nullable = false)
	private String title;

	@Column(length = 1000)
	private String aboutDescription;

	@Column(length = 1000)
	private String appImage;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date releaseDate;

	private double price;
	private int peakPlayers;

	@ManyToOne
	@JoinColumn(name = "publisherId", referencedColumnName = "publisherId")
	private Publisher publisher;

	@ManyToMany
	@JoinTable(name = "games_categories", joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private List<Category> categories;

	@ManyToMany(mappedBy = "favoriteGames")
	private List<User> users;

	public Game() {
	}

	public Game(Long appId, String title, Date releaseDate, double price, String aboutDescription, int peakPlayers,
			String appImage) {
		this.appId = appId;
		this.title = title;
		this.releaseDate = releaseDate;
		this.price = price;
		this.peakPlayers = peakPlayers;
		this.aboutDescription = aboutDescription;
		this.appImage = appImage;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getAboutDescription() {
		return aboutDescription;
	}

	public void setAboutDescription(String aboutDescription) {
		this.aboutDescription = aboutDescription;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public int getPeakPlayers() {
		return peakPlayers;
	}

	public void setPeakPlayers(int peakPlayers) {
		this.peakPlayers = peakPlayers;
	}

	public String getAppImage() {
		return appImage;
	}

	public void setAppImage(String appImage) {
		this.appImage = appImage;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
