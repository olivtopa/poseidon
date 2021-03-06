package com.olivtopa.poseidon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olivtopa.poseidon.domain.Rating;
import com.olivtopa.poseidon.exceptions.DataNotFoundException;
import com.olivtopa.poseidon.repositories.RatingRepository;

@Service
public class RatingService {
	
	@Autowired
	RatingRepository ratingRepository;
	
	public Iterable<Rating> getAllRating() {
		return ratingRepository.findAll();
	}
	
	public Rating getRatingById(Integer id) {
		return ratingRepository.findById(id).orElseThrow(()->new DataNotFoundException("Invalid Rating Id :" + id));
	}

	public void save(Rating rating) {
		ratingRepository.save(rating);
	}

	public void deleteBid(Rating rating) {
		ratingRepository.delete(rating);
	}

}
