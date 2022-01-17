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

import com.olivtopa.poseidon.domain.Trade;
import com.olivtopa.poseidon.repositories.TradeRepository;
import com.olivtopa.poseidon.services.TradeService;

@Controller
public class TradeController {
	    @Autowired
	    private TradeService tradeService;
	    @Autowired
	    TradeRepository tradeRepository;

	    @RequestMapping("/trade/list")
	    public String home(Model model){
	    	model.addAttribute("trade", tradeService.getAllTrade());
	        return "trade/list";
	    }

	    @GetMapping("/trade/add")
	    public String addTrade(Trade trade) {
	        return "trade/add";
	    }

	    @PostMapping("/trade/validate")
	    public String validate(@Valid Trade trade, BindingResult result, Model model) {
	    	if (!result.hasErrors()) {
				tradeService.save(trade);
				model.addAttribute("trade", tradeService.getAllTrade());
			}
	        return "trade/add";
	    }

	    @GetMapping("/trade/update/{id}")
	    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
	    	Trade trade = tradeRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid Trade Id :" + id));
			model.addAttribute("trade", trade);
	        return "trade/update";
	    }

	    @PostMapping("/trade/update/{id}")
	    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
	                             BindingResult result, Model model) {
	    	if (result.hasErrors()) {
				return "rating/update";
			}
	    	trade.setTradeId(id);
			model.addAttribute("trade", trade);
	        return "redirect:/trade/list";
	    }

	    @GetMapping("/trade/delete/{id}")
	    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
	    	Trade trade = tradeRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid trade Id :" + id));
	    	tradeService.deleteBid(trade);
			model.addAttribute("trade", tradeService.getAllTrade());
	        return "redirect:/trade/list";
	    }

}
