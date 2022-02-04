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

import com.olivtopa.poseidon.domain.CurvePoint;
import com.olivtopa.poseidon.repositories.CurvePointRepository;
import com.olivtopa.poseidon.services.CurveService;

@Controller
public class CurveController {
	
	private static final Logger logger = LoggerFactory.getLogger(CurveController.class);
	
	@Autowired
	CurveService curveService;
	@Autowired
	CurvePointRepository curvePointRepository;

	@RequestMapping("/curvePoint/list")
	public String home(Model model) {
		logger.info("Curve List page");
		model.addAttribute("curvePoints",curveService.getAllCurve());
		return "curvePoint/list";
	}

	@GetMapping("/curvePoint/add")
	public String addCurveForm(CurvePoint bid) {
		logger.info("display Add CurvePoint form");
		return "curvePoint/add";
	}

	@PostMapping("/curvePoint/validate")
	public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
		logger.info("Adding Curve Point {}: ",curvePoint);
		if (!result.hasErrors()) {
			curveService.save(curvePoint);
			model.addAttribute("curvePoints", curveService.getAllCurve());
			logger.info("CurvePoint added ! : id = {}", curvePoint.getId());
			return "redirect:/curvePoint/list";
		}
		logger.info("Adding CurvePoint error ! {} ",result);
		return "curvePoint/add";
	}

	@GetMapping("/curvePoint/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
		logger.info("Display update Curve form");
		CurvePoint curve = curveService.getCurveById(id);
		model.addAttribute("curvePoint", curve);
		return "curvePoint/update";
	}

	@PostMapping("/curvePoint/update/{id}")
	public String updateCurve(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			logger.info("CurvePoint updat error ! {} : ",result);
			return "curvePoint/update";
		}
		logger.info("CurvePoint updating ...{} ",curvePoint);
		curvePoint.setCurveId(id);
		curveService.save(curvePoint);
		model.addAttribute("curvePoints", curveService.getAllCurve());
		logger.info("CurvePoint updated ! : {}", id);
		return "redirect:/curvePoint/list";
	}

	@GetMapping("/curvePoint/delete/{id}")
	public String deleteCurve(@PathVariable("id") Integer id, Model model) {
		logger.info("Deleting CurvePoint {} ...", id);
		CurvePoint curve = curveService.getCurveById(id);
		curveService.deleteBid(curve);
		model.addAttribute("curvePoints", curveService.getAllCurve());
		logger.info("CurvePoint deleted !");
		return "redirect:/curvePoint/list";
	}
}
