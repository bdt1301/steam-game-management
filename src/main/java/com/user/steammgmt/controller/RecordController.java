package com.user.steammgmt.controller;

import com.user.steammgmt.repository.RecordRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordController {

	private final RecordRepository recordRepository;

	@GetMapping
	public String listRecords(Model model) {
		model.addAttribute("records", recordRepository.findAll());
		return "user/records";
	}
    
}
