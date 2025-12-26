package com.user.steammgmt.controller;

import com.user.steammgmt.model.User;
import com.user.steammgmt.service.CategoryService;
import com.user.steammgmt.service.NavigationService;
import com.user.steammgmt.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
	private final UserService userService;
	private final CategoryService categoryService;
	private final NavigationService navigationService;

	public UserController(UserService userService, CategoryService categoryService,
			NavigationService navigationService) {
		this.userService = userService;
		this.categoryService = categoryService;
		this.navigationService = navigationService;
	}

	@GetMapping("/users")
	public String listUsers(Model model) {
		model.addAttribute("users", userService.getAllUsers());
		return "users";
	}

	@GetMapping("/user/delete/{id}")
	public String deleteUser(@PathVariable("id") Long userId, HttpServletRequest request, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			userService.deleteUser(userId);
			redirectAttributes.addFlashAttribute("success", "Tài khoản người dùng đã được xóa thành công!");
		} catch (Exception e) {
			System.err.println("Error deleting user: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi xóa tài khoản người dùng!");
		}
		return "redirect:/users";
	}

	@GetMapping("/user/details")
	public String userDetails(@RequestParam("username") String username, Model model) {

		User user = userService.getUserByUsername(username);

		model.addAttribute("user", user);
		model.addAttribute("categories", categoryService.getAllCategories());

		return "/user/user_details";
	}

	@PostMapping("/user/addFavoriteGame")
	public String addFavoriteGames(@RequestParam Long gameId, @AuthenticationPrincipal UserDetails userDetails) {

		userService.addFavoriteGame(userDetails.getUsername(), gameId);
		return "redirect:/games/details/" + gameId;
	}

	@PostMapping("/user/removeFavoriteGame")
	public String removeFromFavorites(@RequestParam Long gameId, @AuthenticationPrincipal UserDetails userDetails,
			HttpSession session, HttpServletRequest request) {

		navigationService.saveURL(session, "previousURL", request.getHeader("Referer"));

		userService.removeFavoriteGame(userDetails.getUsername(), gameId);

		return navigationService.resolveRedirectURL(session, "previousURL", List.of(), "/");
	}

	@PostMapping("/user/update-profile")
	public String updateProfile(@RequestParam String fullName, @RequestParam String email,
			@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {

		boolean success = userService.updateProfile(userDetails.getUsername(), fullName, email);
		
		if (success) {
			redirectAttributes.addFlashAttribute("success", "Profile đã được cập nhật thành công!");
		} else {
			redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng bởi tài khoản khác.");
		}

		return "redirect:/user/details?username=" + userDetails.getUsername();
	}

	@PostMapping("/user/change-password")
	public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
			@RequestParam String confirmPassword, @AuthenticationPrincipal UserDetails userDetails,
			RedirectAttributes redirectAttributes) {

		if (!newPassword.equals(confirmPassword)) {
			redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không khớp.");
			return "redirect:/user/details?username=" + userDetails.getUsername();
		}

		boolean success = userService.changePassword(userDetails.getUsername(), oldPassword, newPassword);

		redirectAttributes.addFlashAttribute(success ? "success" : "error",
				success ? "Đổi mật khẩu thành công!" : "Mật khẩu cũ không đúng.");

		return "redirect:/user/details?username=" + userDetails.getUsername();
	}

}