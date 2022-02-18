package com.olivtopa.poseidon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olivtopa.poseidon.domain.RuleName;

@Repository
public interface RuleNameRepository extends JpaRepository<RuleName, Integer> {
}
