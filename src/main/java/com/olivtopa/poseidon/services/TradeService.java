package com.olivtopa.poseidon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olivtopa.poseidon.domain.Trade;
import com.olivtopa.poseidon.repositories.TradeRepository;

@Service
public class TradeService {
	
	@Autowired
	TradeRepository tradeRepository;
	
	public Iterable<Trade> getAllTrade() {
		return tradeRepository.findAll();
	}

	public void save(Trade trade) {
		tradeRepository.save(trade);
	}

	public void deleteBid(Trade trade) {
		tradeRepository.delete(trade);
	}

}
