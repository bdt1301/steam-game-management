package com.user.steammgmt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "games")
@Getter
@Setter
public class Game {
	@Id
	private Long appId;

	@Column(nullable = false)
	private String title;

	@Column(length = 1000)
	private String aboutDescription;

	@Column(length = 5000)
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

}
