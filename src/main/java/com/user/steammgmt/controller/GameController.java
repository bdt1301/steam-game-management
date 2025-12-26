package com.user.steammgmt.controller;

import com.user.steammgmt.model.Category;
import com.user.steammgmt.model.Game;
import com.user.steammgmt.model.Publisher;
import com.user.steammgmt.model.User;
import com.user.steammgmt.service.CategoryService;
import com.user.steammgmt.service.GameService;
import com.user.steammgmt.service.NavigationService;
import com.user.steammgmt.service.PublisherService;
import com.user.steammgmt.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/games")
public class GameController {

	private final GameService gameService;
	private final PublisherService publisherService;
	private final CategoryService categoryService;
	private final UserService userService;
	private final NavigationService navigationService;

	public GameController(GameService gameService, PublisherService publisherService, CategoryService categoryService,
			UserService userService, NavigationService navigationService) {
		this.gameService = gameService;
		this.publisherService = publisherService;
		this.categoryService = categoryService;
		this.userService = userService;
		this.navigationService = navigationService;
	}

	// Hiển thị danh sách game
	@GetMapping
	public String listGames(Model model) {
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("games", gameService.getAllGames());
		return "/game/games";
	}

	// Hiển thị thông tin chi tiết game
	@GetMapping("/details/{id}")
	public String showGameDetails(@PathVariable("id") Long appId, Model model, HttpServletRequest request,
			HttpSession session, @AuthenticationPrincipal UserDetails userDetails) {
		navigationService.saveURL(session, "previousURL", request.getHeader("Referer"));
		model.addAttribute("game", gameService.getGameById(appId));

		if (userDetails != null) {
			User user = userService.getUserByUsername(userDetails.getUsername());
			model.addAttribute("user", user);
		}

		return "/game/game_details";
	}

	// Hiển thị form thêm mới hoặc chỉnh sửa game
	@GetMapping({ "/new", "/edit/{id}" })
	public String showAddEditGameForm(@PathVariable(required = false) Long id,
			@RequestParam(required = false) String publisherId, Model model, HttpServletRequest request,
			HttpSession session) {
		navigationService.saveURL(session, "previousURL", request.getHeader("Referer"));

		Game game;
		List<Long> existingCategoryIds;
		if (id != null) {
			game = gameService.getGameById(id);
			existingCategoryIds = game.getCategories().stream().map(Category::getCategoryId)
					.collect(Collectors.toList());
		} else {
			game = new Game();
			existingCategoryIds = List.of();
		}

		if (publisherId != null) {
			Publisher publisher = publisherService.getPublisherById(publisherId);
			game.setPublisher(publisher);
		}

		List<Publisher> publishers = publisherService.getAllPublishers();
		List<Category> categories = categoryService.getAllCategories();

		model.addAttribute("publishers", publishers);
		model.addAttribute("categories", categories);
		model.addAttribute("existingCategories", existingCategoryIds);
		model.addAttribute("game", game);

		return "game/add_edit_game";
	}

	// Xử lý form thêm mới game
	@PostMapping
	public String addGame(@ModelAttribute Game game, @RequestParam(required = false) List<Long> categoryIds,
			@RequestParam(required = false) String images, HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			gameService.addGame(game, categoryIds);
			redirectAttributes.addFlashAttribute("success", "Game đã được thêm thành công!");
		} catch (Exception e) {
			System.err.println("Error adding game: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi thêm game!");
		}
		return navigationService.resolveRedirectURL(session, "previousURL", List.of(), "/games");
	}

	@PostMapping("/{appId}")
	public String updateGame(@PathVariable Long appId, @ModelAttribute("game") Game updatedGame,
			@RequestParam(required = false) List<Long> categoryIds, @RequestParam(required = false) String images,
			HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			gameService.updateGame(appId, updatedGame, categoryIds);
			redirectAttributes.addFlashAttribute("success", "Game đã được cập nhật thành công!");
		} catch (Exception e) {
			System.err.println("Error updating game: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi cập nhật game!");
		}
		return navigationService.resolveRedirectURL(session, "previousURL", List.of(), "/games");
	}

	// Xóa game
	@GetMapping("/delete/{id}")
	public String deleteGame(@PathVariable("id") Long appId, HttpServletRequest request, HttpSession session,
			RedirectAttributes redirectAttributes) {
		String currentURL = request.getHeader("Referer");
		if (currentURL.contains("/publishers/details")) {
			navigationService.saveURL(session, "previousURL", currentURL);
		}
		try {
			gameService.deleteGame(appId);
			redirectAttributes.addFlashAttribute("success", "Game đã được xóa thành công!");
		} catch (Exception e) {
			System.err.println("Error deleting game: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi xóa game!");
		}
		return navigationService.resolveRedirectURL(session, "previousURL", List.of("/games/details", "/games/edit"),
				"/games");
	}

}
