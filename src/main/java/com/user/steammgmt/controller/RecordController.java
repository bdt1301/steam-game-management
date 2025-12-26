package com.user.steammgmt.controller;

import com.user.steammgmt.repository.RecordRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/records")
public class RecordController {
	private final RecordRepository recordRepository;

	public RecordController(RecordRepository recordRepository) {
		this.recordRepository = recordRepository;
	}

	@GetMapping
	public String listRecords(Model model) {
		model.addAttribute("records", recordRepository.findAll());
		return "records";
	}
}
