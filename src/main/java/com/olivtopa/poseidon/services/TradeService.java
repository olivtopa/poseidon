package com.olivtopa.poseidon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olivtopa.poseidon.domain.Trade;
import com.olivtopa.poseidon.exceptions.DataNotFoundException;
import com.olivtopa.poseidon.repositories.TradeRepository;

@Service
public class TradeService {
	
	@Autowired
	private TradeRepository tradeRepository;
	
	public Iterable<Trade> getAllTrade() {
		return tradeRepository.findAll();
	}
	
	public Trade getTradeById(Integer tradeId) {
		return tradeRepository.findById(tradeId).orElseThrow(()->new DataNotFoundException(String.format("Trade %d not found",tradeId)));
	}

	public void save(Trade trade) {
		tradeRepository.save(trade);
	}

	public void deleteBid(Trade trade) {
		tradeRepository.delete(trade);
	}

}
