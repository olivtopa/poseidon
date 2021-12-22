package com.olivtopa.poseidon.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.olivtopa.poseidon.domain.BidList;
import com.olivtopa.poseidon.repositories.BidListRepository;
import com.olivtopa.poseidon.services.BidListService;

@Controller
public class BidListController {
	@Autowired
	private BidListService bidListService;
	private BidListRepository bidListRepository;

	@RequestMapping("/bidList/list")
	public String home(Model model) {
		bidListService.getAllBid();
		return "bidList/list";
	}

	@GetMapping("/bidList/add")
	public String addBidForm(BidList bid) {
		return "bidList/add";
	}

	@PostMapping("/bidList/validate")
	public String validate(@Valid BidList bid, BindingResult result, Model model) {
		if (!result.hasErrors()) {
			bidListRepository.save(bid);
			model.addAttribute("bids", bidListService.getAllBid());
		}
		return "bidList/add";
	}

	@GetMapping("/bidList/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
		BidList bid = bidListRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid bidList Id :" + id));
		model.addAttribute("bidList", bid);
		return "bidList/update";
	}

	@PostMapping("/bidList/update/{id}")
	public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "bidList/update";
		}
		bidList.setBidListId(id);
		model.addAttribute("bidList", bidList);
		return "redirect:/bidList/list";
	}

	@GetMapping("/bidList/delete/{id}")
	public String deleteBid(@PathVariable("id") Integer id, Model model) {
		BidList bid = bidListRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid bidList Id :" + id));
		bidListService.deleteBid(bid);
		model.addAttribute("bid",bidListService.getAllBid());
		return "redirect:/bidList/list";
	}
}
