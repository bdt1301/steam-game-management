package com.user.steammgmt.service;

import com.user.steammgmt.model.Category;
import com.user.steammgmt.model.Game;
import com.user.steammgmt.model.Notification;
import com.user.steammgmt.model.Publisher;
import com.user.steammgmt.model.Record;
import com.user.steammgmt.model.User;
import com.user.steammgmt.repository.CategoryRepository;
import com.user.steammgmt.repository.GameRepository;
import com.user.steammgmt.repository.NotificationRepository;
import com.user.steammgmt.repository.PublisherRepository;
import com.user.steammgmt.repository.RecordRepository;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

	private final GameRepository gameRepository;
	private final PublisherRepository publisherRepository;
	private final CategoryRepository categoryRepository;
	private final RecordRepository recordRepository;
	private final NotificationRepository notificationRepository;

	public GameService(GameRepository gameRepository, PublisherRepository publisherRepository,
			CategoryRepository categoryRepository, RecordRepository recordRepository,
			NotificationRepository notificationRepository) {
		this.gameRepository = gameRepository;
		this.publisherRepository = publisherRepository;
		this.categoryRepository = categoryRepository;
		this.recordRepository = recordRepository;
		this.notificationRepository = notificationRepository;
	}

	// Lấy danh sách tất cả các game
	public List<Game> getAllGames() {
		return gameRepository.findAll();
	}

	public void saveGame(Game game) {
		gameRepository.save(game);
	}

	// Thêm hoặc cập nhật một game
	public void addGame(Game game, List<Long> categoryIds) {
		String publisherId = game.getPublisher().getPublisherId();
		Publisher publisher = publisherRepository.findById(publisherId).orElseThrow();
		game.setPublisher(publisher);

		List<Category> selectedCategories;
		if (categoryIds != null && !categoryIds.isEmpty()) {
			selectedCategories = categoryIds.stream()
					.map(categoryId -> categoryRepository.findById(categoryId).orElseThrow())
					.collect(Collectors.toList());
		} else {
			selectedCategories = new ArrayList<>();
		}
		game.setCategories(selectedCategories);

		gameRepository.save(game);
		recordRepository.save(new Record("Game", String.valueOf(game.getAppId()), "Add", new Date()));
	}

	public void updateGame(Long appId, Game updatedGame, List<Long> categoryIds) {
		Game existingGame = getGameById(appId);

		double oldPrice = existingGame.getPrice();
		double newPrice = updatedGame.getPrice();
		double discountPercent = Math.abs(Math.round(((newPrice / oldPrice) - 1) * 100));
		if (newPrice < oldPrice) {
			for (User user : existingGame.getUsers()) {
				String message = existingGame.getTitle() + " is " + (int)discountPercent + "% off!";
				String redirectLink = "/games/details/" + existingGame.getAppId();
				String imageNotification = updatedGame.getAppImage();
				Notification notification = new Notification(user, message, redirectLink, imageNotification);
				notificationRepository.save(notification);
			}
		}

		existingGame.setTitle(updatedGame.getTitle());
		existingGame.setReleaseDate(updatedGame.getReleaseDate());
		existingGame.setPrice(updatedGame.getPrice());
		existingGame.setPeakPlayers(updatedGame.getPeakPlayers());
		existingGame.setAboutDescription(updatedGame.getAboutDescription());
		existingGame.setPublisher(updatedGame.getPublisher());
		existingGame.setAppImage(updatedGame.getAppImage());

		List<Category> selectedCategories;
		if (categoryIds != null && !categoryIds.isEmpty()) {
			selectedCategories = categoryIds.stream()
					.map(categoryId -> categoryRepository.findById(categoryId).orElseThrow())
					.collect(Collectors.toList());
		} else {
			selectedCategories = new ArrayList<>();
		}
		existingGame.setCategories(selectedCategories);

		gameRepository.save(existingGame);
		recordRepository.save(new Record("Game", String.valueOf(appId), "Update", new Date()));
	}

	// Lấy game theo ID
	@Transactional
	public Game getGameById(Long appId) {
		Game game = gameRepository.findById(appId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid game Id: " + appId));
		Hibernate.initialize(game.getCategories());
		return game;
	}

	// Xóa game theo ID
	public void deleteGame(Long appId) {
		gameRepository.deleteById(appId);
		recordRepository.save(new Record("Game", String.valueOf(appId), "Delete", new Date()));
	}

	// Lấy game theo Publisher ID
	public List<Game> getGamesByPublisherId(String publisherId) {
		return gameRepository.findByPublisherPublisherId(publisherId);
	}

	// Lấy game theo Category ID
	public List<Game> getGamesByCategoryId(Long categoryId) {
		return gameRepository.findByCategories_CategoryId(categoryId);
	}
}
