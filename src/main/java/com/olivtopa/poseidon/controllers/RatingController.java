package com.olivtopa.poseidon.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.olivtopa.poseidon.domain.Rating;
import com.olivtopa.poseidon.exceptions.DataNotFoundException;
import com.olivtopa.poseidon.repositories.RatingRepository;
import com.olivtopa.poseidon.services.RatingService;

@Controller
public class RatingController {
	
	private static final Logger logger = LoggerFactory.getLogger(RatingController.class);
	
	@Autowired
	private RatingService ratingService;
	@Autowired
	private RatingRepository ratingRepository;

	@RequestMapping("/rating/list")
	public String home(Model model) {
		logger.info("Rating List page");
		model.addAttribute("rating",ratingService.getAllRating());
		return "rating/list";
	}

	@GetMapping("/rating/add")
	public String addRatingForm(Rating rating) {
		logger.info("display Add Rating form");
		return "rating/add";
	}

	@PostMapping("/rating/validate")
	public String validate(@Valid Rating rating, BindingResult result, Model model) {
		logger.info("Adding rating {}: ", rating);
		if (!result.hasErrors()) {
			ratingService.save(rating);
			model.addAttribute("rating", ratingService.getAllRating());
			logger.info("Rating added ! : {}", rating.getId());
			return "redirect:/rating/list";
		}
		logger.info("Adding rating error ! {}", result);
		return "rating/add";
	}

	@GetMapping("/rating/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
		logger.info("Display update rating form");
		Rating rating = ratingRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Invalid Rating Id :" + id));
		model.addAttribute("rating", rating);
		return "rating/update";
	}

	@PostMapping("/rating/update/{id}")
	public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			logger.info("Rating update error : {}!", result);
			return "rating/update";
		}
		logger.info("Rating updating ...{}", rating);
		rating.setId(id);
		ratingService.save(rating);
		model.addAttribute("rating", rating);
		logger.info("Rating updated : {}", id);
		return "redirect:/rating/list";
	}

	@GetMapping("/rating/delete/{id}")
	public String deleteRating(@PathVariable("id") Integer id, Model model) {
		logger.info("deleting rating :{} ", id);
		Rating rating = ratingRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Invalid rating Id :" + id));
		ratingService.deleteBid(rating);
		model.addAttribute("rating", ratingService.getAllRating());
		logger.info("Rating deleted !");
		return "redirect:/rating/list";
	}
}
