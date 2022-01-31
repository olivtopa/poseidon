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

import com.olivtopa.poseidon.domain.Trade;
import com.olivtopa.poseidon.repositories.TradeRepository;
import com.olivtopa.poseidon.services.TradeService;

@Controller
public class TradeController {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeController.class);
	
	    @Autowired
	    private TradeService tradeService;
	    @Autowired
	    TradeRepository tradeRepository;

	    @RequestMapping("/trade/list")
	    public String home(Model model){
	    	logger.info("Trade List page");
	    	model.addAttribute("trade", tradeService.getAllTrade());
	        return "trade/list";
	    }

	    @GetMapping("/trade/add")
	    public String addTrade(Trade trade) {
	    	logger.info("display trade form");
	        return "trade/add";
	    }

	    @PostMapping("/trade/validate")
	    public String validate(@Valid Trade trade, BindingResult result, Model model) {
	    	logger.info("Adding Trade {}: ", trade);
	    	if (!result.hasErrors()) {
				tradeService.save(trade);
				model.addAttribute("trade", tradeService.getAllTrade());
				logger.info("trade added ! : id = {} ", trade.getTradeId());
				return "redirect:/trade/list";
			}
	    	logger.info("Adding Trade error ! {} ", result);
	        return "trade/add";
	    }

	    @GetMapping("/trade/update/{tradeId}")
	    public String showUpdateForm(@PathVariable("tradeId") Integer tradeId, Model model) {
	    	logger.info("Display update Trade form");
	    	Trade trade = tradeService.getTradeById(tradeId);
			model.addAttribute("trade", trade);
	        return "trade/update";
	    }

	    @PostMapping("/trade/update/{tradeId}")
	    public String updateTrade(@PathVariable("tradeId") Integer tradeId, @Valid Trade trade,
	                             BindingResult result, Model model) {
	    	if (result.hasErrors()) {
	    		logger.info("Trade update error ! : {}", result);
				return "trade/update";
			}
			logger.info("Trade updating ...{}",trade);
	    	trade.setTradeId(tradeId);
	    	tradeService.save(trade);
			model.addAttribute("trade", tradeService.getAllTrade());
			logger.info("Trade updated ! : {}", trade.getTradeId());
	        return "redirect:/trade/list";
	    }

	    @GetMapping("/trade/delete/{tradeId}")
	    public String deleteTrade(@PathVariable("tradeId") Integer tradeId, Model model) {
	    	logger.info("Deleting Trade {} ...", tradeId);
	    	Trade trade = tradeService.getTradeById(tradeId);
	    	tradeService.deleteBid(trade);
			model.addAttribute("trade", tradeService.getAllTrade());
			logger.info("Trade deleted !");
	        return "redirect:/trade/list";
	    }

}
