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
import com.olivtopa.poseidon.services.BidListService;

@Controller
public class BidListController {
	@Autowired
	private BidListService bidListService;

	@Autowired

	@RequestMapping("/bidList/list")
	public String home(Model model) {
		model.addAttribute("bidlist", bidListService.getAllBid());
		return "bidList/list";
	}

	@GetMapping("/bidList/add")
	public String addBidList(BidList bid) {
		return "bidList/add";
	}

	@PostMapping("/bidList/validate")
	public String validate(@Valid BidList bid, BindingResult result, Model model) {
		if (!result.hasErrors()) {
			bidListService.save(bid);
			model.addAttribute("bidlist", bidListService.getAllBid());
			return "redirect:/bidList/list";
		}
		return "bidList/add";
	}

	@GetMapping("/bidList/update/{bidListId}")
	public String showUpdateForm(@PathVariable("bidListId") Integer bidListId, Model model) {
		BidList bid = bidListService.getBidListById(bidListId);
		model.addAttribute("bidList", bid);
		return "bidList/update";
	}

	@PostMapping("/bidList/update/{bidListId}")
	public String updateBid(@PathVariable("bidListId") Integer bidListId, @Valid BidList bidList, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "bidList/update";
		}
		bidList.setBidListId(bidListId);
		model.addAttribute("bidlist", bidListService.getAllBid());
		return "redirect:/bidList/list";
	}

	@GetMapping("/bidList/delete/{bidListId}")
	public String deleteBid(@PathVariable("bidListId") Integer bidListId, Model model) {
		BidList bid = bidListService.getBidListById(bidListId);
		bidListService.deleteBid(bid);
		model.addAttribute("bidlist", bidListService.getAllBid());
		return "redirect:/bidList/list";
	}
}
