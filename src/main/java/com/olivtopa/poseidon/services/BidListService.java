package com.olivtopa.poseidon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olivtopa.poseidon.domain.BidList;
import com.olivtopa.poseidon.repositories.BidListRepository;

@Service
public class BidListService {

	@Autowired
	private BidListRepository bidListRepository;

	public Iterable<BidList> getAllBid() {
		return bidListRepository.findAll();
	}

	/*
	 * public BidList getBidListById(Integer id){ return
	 * bidListRepository.findById(id);
	 *  }
	 */

	public void deleteBid(BidList bidList) {
		bidListRepository.delete(bidList);
	}

}
