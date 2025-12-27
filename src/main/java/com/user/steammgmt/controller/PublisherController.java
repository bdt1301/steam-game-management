package com.user.steammgmt.controller;

import com.user.steammgmt.model.Publisher;
import com.user.steammgmt.service.CategoryService;
import com.user.steammgmt.service.GameService;
import com.user.steammgmt.service.NavigationService;
import com.user.steammgmt.service.PublisherService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/publishers")
public class PublisherController {

	private final PublisherService publisherService;
	private final GameService gameService;
	private final CategoryService categoryService;
	private final NavigationService navigationService;

	public PublisherController(PublisherService publisherService, GameService gameService,
			CategoryService categoryService, NavigationService navigationService) {
		this.publisherService = publisherService;
		this.gameService = gameService;
		this.categoryService = categoryService;
		this.navigationService = navigationService;
	}

	// Hiển thị danh sách nhà phát hành
	@GetMapping
	public String listPublishers(Model model) {
		model.addAttribute("publishers", publisherService.getPublishersWithDetails());
		return "publisher/publishers";
	}

	// Hiển thị thông tin chi tiết nhà phát hành
	@GetMapping("/details/{id}")
	public String showPublisherDetails(@PathVariable("id") String publisherId, Model model) {
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("publisher", publisherService.getPublisherWithDetails(publisherId));
		model.addAttribute("games", gameService.getGamesByPublisherId(publisherId));
		return "publisher/publisher_details";
	}

	// Hiển thị form thêm mới hoặc chỉnh sửa nhà phát hành
	@GetMapping({ "/new", "/edit/{id}" })
	public String showAddEditPublisherForm(@PathVariable(required = false) String id, Model model,
			HttpServletRequest request, HttpSession session) {
		navigationService.saveURL(session, "previousURL", request.getHeader("Referer"));
		if (id != null) {
			model.addAttribute("publisher", publisherService.getPublisherById(id));
		} else {
			model.addAttribute("publisher", new Publisher());
		}
		return "publisher/add_edit_publisher";
	}

	// Xử lý form thêm mới nhà phát hành
	@PostMapping
	public String addPublisher(@ModelAttribute Publisher publisher, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			publisherService.addPublisher(publisher);
			redirectAttributes.addFlashAttribute("success", "Nhà phát hành đã được thêm thành công!");
		} catch (Exception e) {
			System.err.println("Error adding publisher: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi thêm nhà phát hành!");
		}
		return navigationService.resolveRedirectURL(session, "previousURL", List.of(), "/publishers");
	}

	// Xử lý cập nhật thông tin nhà phát hành
	@PostMapping("/{id}")
	public String updatePublisher(@PathVariable("id") String publisherId, @ModelAttribute Publisher updatedPublisher,
			HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			publisherService.updatePublisher(publisherId, updatedPublisher);
			redirectAttributes.addFlashAttribute("success", "Nhà phát hành đã được cập nhật thành công!");
		} catch (Exception e) {
			System.err.println("Error updating publisher: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi cập nhật nhà phát hành!");
		}
		return navigationService.resolveRedirectURL(session, "previousURL", List.of(), "/publishers");
	}

	// Xóa nhà phát hành
	@GetMapping("/delete/{id}")
	public String deletePublisher(@PathVariable("id") String publisherId, HttpServletRequest request,
			HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			publisherService.deletePublisher(publisherId);
			redirectAttributes.addFlashAttribute("success", "Nhà phát hành đã được xóa thành công!");
		} catch (Exception e) {
			System.err.println("Error deleting publisher: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi xóa nhà phát hành!");
		}
		return "redirect:/publishers";
	}

}
