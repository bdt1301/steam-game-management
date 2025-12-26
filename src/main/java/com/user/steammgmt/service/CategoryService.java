package com.user.steammgmt.service;

import com.user.steammgmt.model.Category;
import com.user.steammgmt.model.Game;
import com.user.steammgmt.model.Record;
import com.user.steammgmt.repository.CategoryRepository;
import com.user.steammgmt.repository.RecordRepository;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

	private final RecordRepository recordRepository;
	private final CategoryRepository categoryRepository;
	private final GameService gameService;

	public CategoryService(RecordRepository recordRepository, CategoryRepository categoryRepository,
			GameService gameService) {
		this.recordRepository = recordRepository;
		this.categoryRepository = categoryRepository;
		this.gameService = gameService;
	}

	// Lấy danh sách tất cả các thể loại game
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public List<Category> getCategoriesWithDetails() {
		List<Category> categories = getAllCategories();
		for (Category category : categories) {
			List<Game> games = gameService.getGamesByCategoryId(category.getCategoryId());
			category.setProductQuantity(games.size());
			Optional<Game> featuredGame = games.stream().max(Comparator.comparingInt(Game::getPeakPlayers));
			category.setFeaturedGame(featuredGame.orElse(null));
		}
		return categories;
	}

	// Lưu hoặc cập nhật một thể loại game
	public void addCategory(Category category) {
		categoryRepository.save(category);
		recordRepository.save(new Record("Category", String.valueOf(category.getCategoryId()), "Add", new Date()));
	}

	public void updateCategory(Long categoryId, Category updatedCategory) {
		Category category = getCategoryById(categoryId);
		category.setCategoryName(updatedCategory.getCategoryName());
		category.setCategoryDescription(updatedCategory.getCategoryDescription());
		category.setCategoryImage(updatedCategory.getCategoryImage());
		categoryRepository.save(category);
		recordRepository.save(new Record("Category", String.valueOf(categoryId), "Update", new Date()));
	}

	// Lấy thể loại game theo ID
	@Transactional
	public Category getCategoryById(Long categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow();
		Hibernate.initialize(category.getGames());
		return category;
	}

	// Xóa thể loại game theo ID
	@Transactional
	public void deleteCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow();
		for (Game game : category.getGames()) {
			game.getCategories().remove(category);
			gameService.saveGame(game);
		}
		categoryRepository.delete(category);
		recordRepository.save(new Record("Category", String.valueOf(categoryId), "Delete", new Date()));
	}

	public void addGamesToCategory(Long categoryId, List<Long> gameIds) {
		Category category = getCategoryById(categoryId);
		List<Game> games = gameIds.stream().map(gameService::getGameById).collect(Collectors.toList());
		category.getGames().addAll(games);
		for (Game game : games) {
			game.getCategories().add(category);
			gameService.saveGame(game);
		}
		categoryRepository.save(category);

		String gameIdsString = gameIds.stream().map(String::valueOf).collect(Collectors.joining(", "));
		recordRepository.save(
				new Record("Category", String.valueOf(categoryId), "Add games with IDs: " + gameIdsString, new Date()));
	}

	// Gỡ game khỏi thể loại
	@Transactional
	public void removeGame(Long categoryId, Long appId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow();
		Game game = gameService.getGameById(appId);
		category.getGames().remove(game);
		game.getCategories().remove(category);
		categoryRepository.save(category);
		gameService.saveGame(game);
		recordRepository
				.save(new Record("Category", String.valueOf(categoryId), "Remove game with ID: " + appId, new Date()));
	}

}