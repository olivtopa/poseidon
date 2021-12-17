package com.olivtopa.poseidon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olivtopa.poseidon.domain.RuleName;

public interface RuleNameRepository extends JpaRepository<RuleName, Integer> {
}
