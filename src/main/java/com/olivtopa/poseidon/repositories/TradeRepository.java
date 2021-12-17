package com.olivtopa.poseidon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olivtopa.poseidon.domain.Trade;

public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
