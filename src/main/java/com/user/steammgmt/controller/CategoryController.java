package com.user.steammgmt.controller;

import com.user.steammgmt.model.Category;
import com.user.steammgmt.model.Game;
import com.user.steammgmt.service.CategoryService;
import com.user.steammgmt.service.GameService;
import com.user.steammgmt.service.NavigationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryService categoryService;
	private final GameService gameService;
	private final NavigationService navigationService;

	public CategoryController(CategoryService categoryService, GameService gameService,
			NavigationService navigationService) {
		this.categoryService = categoryService;
		this.gameService = gameService;
		this.navigationService = navigationService;
	}

	// Hiển thị danh sách thể loại
	@GetMapping
	public String listCategories(Model model) {
		model.addAttribute("categories", categoryService.getCategoriesWithDetails());
		return "category/categories";
	}

	// Hiển thị thông tin chi tiết thể loại
	@GetMapping("/details/{id}")
	public String showCategoryDetails(@PathVariable("id") Long categoryId, Model model, HttpServletRequest request,
			HttpSession session) {
		navigationService.saveURL(session, "previousURL", request.getHeader("Referer"));

		Category category = categoryService.getCategoryById(categoryId);

		List<Game> games = gameService.getGamesByCategoryId(categoryId);
		category.setProductQuantity(games.size());

		List<Game> allGames = gameService.getAllGames();
		
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("category", category);
		model.addAttribute("games", games);
		model.addAttribute("allGames", allGames);

		return "category/category_details";
	}

	// Hiển thị form thêm mới hoặc chỉnh sửa
	@GetMapping({ "/new", "/edit/{id}" })
	public String showAddEditCategoryForm(@PathVariable(required = false) Long id, Model model,
			HttpServletRequest request, HttpSession session) {
		navigationService.saveURL(session, "previousURL", request.getHeader("Referer"));
		Category category;
		if (id != null) {
			category = categoryService.getCategoryById(id);
		} else {
			category = new Category();
		}
		model.addAttribute("category", category);
		return "category/add_edit_category";
	}

	// Xử lý form thêm mới thể loại
	@PostMapping
	public String addCategory(@ModelAttribute Category category, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			categoryService.addCategory(category);
			redirectAttributes.addFlashAttribute("success", "Thể loại đã được thêm thành công!");
		} catch (Exception e) {
			System.err.println("Error adding category: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi thêm thể loại!");
		}
		return navigationService.resolveRedirectURL(session, "previousURL", List.of(), "/categories");
	}

	// Xử lý cập nhật thông tin thể loại
	@PostMapping("/{id}")
	public String updateCategory(@PathVariable("id") Long categoryId, @ModelAttribute Category updatedCategory,
			HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			categoryService.updateCategory(categoryId, updatedCategory);
			redirectAttributes.addFlashAttribute("success", "Thể loại đã được cập nhật thành công!");
		} catch (Exception e) {
			System.err.println("Error updating category: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi cập nhật thể loại!");
		}
		return navigationService.resolveRedirectURL(session, "previousURL", List.of(), "/categories");
	}

	// Xóa thể loại
	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable("id") Long categoryId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			categoryService.deleteCategory(categoryId);
			redirectAttributes.addFlashAttribute("success", "Thể loại đã được xóa thành công!");
		} catch (Exception e) {
			System.err.println("Error deleting category: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi xóa thể loại!");
		}
		return navigationService.resolveRedirectURL(session, "previousURL",
				List.of("/categories/details", "/categories/edit"), "/categories");
	}

	// Thêm game vào thể loại
	@PostMapping("/addGames/{id}")
	public String addGamesToCategory(@PathVariable("id") Long categoryId, @RequestParam List<Long> gameIds,
			RedirectAttributes redirectAttributes) {
		try {
			categoryService.addGamesToCategory(categoryId, gameIds);
			redirectAttributes.addFlashAttribute("success", "Đã thêm game vào thể loại thành công!");
		} catch (Exception e) {
			System.err.println("Error adding games to category: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi thêm game vào thể loại!");
		}
		return "redirect:/categories/details/" + categoryId;
	}

	// Gỡ game khỏi thể loại
	@GetMapping("/removeGame")
	public String removeGameFromCategory(@RequestParam Long categoryId, @RequestParam Long appId,
			RedirectAttributes redirectAttributes) {
		try {
			categoryService.removeGame(categoryId, appId);
			redirectAttributes.addFlashAttribute("success", "Game đã được gỡ khỏi thể loại thành công!");
		} catch (Exception e) {
			System.err.println("Error removing game from category: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi gỡ game khỏi thể loại!");
		}
		return "redirect:/categories/details/" + categoryId;
	}

}
