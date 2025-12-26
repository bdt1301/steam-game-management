package com.user.steammgmt.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.steammgmt.model.Game;
import com.user.steammgmt.model.Record;
import com.user.steammgmt.model.User;
import com.user.steammgmt.repository.GameRepository;
import com.user.steammgmt.repository.RecordRepository;
import com.user.steammgmt.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final GameRepository gameRepository;
	private final PasswordEncoder passwordEncoder;
	private final RecordRepository recordRepository;

	public UserService(UserRepository userRepository, GameRepository gameRepository, PasswordEncoder passwordEncoder,
			RecordRepository recordRepository) {
		this.userRepository = userRepository;
		this.gameRepository = gameRepository;
		this.passwordEncoder = passwordEncoder;
		this.recordRepository = recordRepository;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found with username: " + username));
	}

	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
		recordRepository.save(new Record("User", String.valueOf(userId), "Delete", new Date()));
	}

	public boolean register(User user) {
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			return false;
		}

		if (userRepository.existsByEmail(user.getEmail())) {
			throw new IllegalArgumentException("EMAIL_EXISTS");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		user.setAvatarUrl("/images/default_avatar_user.jpg");

		userRepository.save(user);

		return true;
	}

	public boolean updateProfile(String username, String fullName, String email) {
		if (userRepository.existsByEmailAndUsernameNot(email, username)) {
			return false;
		}

		userRepository.findByUsername(username).ifPresent(user -> {
			user.setFullName(fullName);
			user.setEmail(email);
			userRepository.save(user);
		});
		
		return true;
	}

	public boolean changePassword(String username, String oldPassword, String newPassword) {
		User user = userRepository.findByUsername(username).orElse(null);
		if (user == null)
			return false;

		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			return false;
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		return true;
	}

	public void addFavoriteGame(String username, Long gameId) {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		Optional<Game> optionalGame = gameRepository.findById(gameId);

		if (optionalUser.isPresent() && optionalGame.isPresent()) {
			User user = optionalUser.get();
			Game game = optionalGame.get();

			user.getFavoriteGames().add(game);
			userRepository.save(user);
		}
	}

	public void removeFavoriteGame(String username, Long gameId) {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		Optional<Game> optionalGame = gameRepository.findById(gameId);

		if (optionalUser.isPresent() && optionalGame.isPresent()) {
			User user = optionalUser.get();
			Game game = optionalGame.get();

			user.getFavoriteGames().remove(game);
			userRepository.save(user);
		}
	}
}
