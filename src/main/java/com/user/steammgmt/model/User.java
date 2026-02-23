package com.user.steammgmt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users_steammgmt")
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String role; // ROLE_USER, ROLE_ADMIN

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String fullName;

	@Column(length = 1000)
	private String avatarUrl;

	@ManyToMany
	@JoinTable(name = "user_favorite_games", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "game_id"))
	private List<Game> favoriteGames;

	public User() {
	}

	public User(String username, String password, String role, String fullName, String email, String avatarUrl) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.fullName = fullName;
		this.email = email;
		this.avatarUrl = avatarUrl;
	}

}
