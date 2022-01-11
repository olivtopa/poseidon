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

import com.olivtopa.poseidon.domain.CurvePoint;
import com.olivtopa.poseidon.repositories.CurvePointRepository;
import com.olivtopa.poseidon.services.CurveService;

@Controller
public class CurveController {
	@Autowired
	CurveService curveService;
	@Autowired
	CurvePointRepository curvePointRepository;

	@RequestMapping("/curvePoint/list")
	public String home(Model model) {
		curveService.getAllCurve();
		return "curvePoint/list";
	}

	@GetMapping("/curvePoint/add")
	public String addBidForm(CurvePoint bid) {
		return "curvePoint/add";
	}

	@PostMapping("/curvePoint/validate")
	public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
		if (!result.hasErrors()) {
			curveService.save(curvePoint);
			model.addAttribute("curves", curveService.getAllCurve());
		}
		return "curvePoint/add";
	}

	@GetMapping("/curvePoint/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
		CurvePoint curve = curvePointRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid CurvePoint Id :" + id));
		model.addAttribute("curvePoint", curve);
		return "curvePoint/update";
	}

	@PostMapping("/curvePoint/update/{id}")
	public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "curvePoint/update";
		}
		curvePoint.setCurveId(id);
		model.addAttribute("curvepoint", curvePoint);
		return "redirect:/curvePoint/list";
	}

	@GetMapping("/curvePoint/delete/{id}")
	public String deleteBid(@PathVariable("id") Integer id, Model model) {
		CurvePoint curve = curvePointRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid bidList Id :" + id));
		curveService.deleteBid(curve);
		model.addAttribute("curvePoint", curveService.getAllCurve());
		return "redirect:/curvePoint/list";
	}
}
