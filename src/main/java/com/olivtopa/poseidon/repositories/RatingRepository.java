package com.olivtopa.poseidon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olivtopa.poseidon.domain.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

}
