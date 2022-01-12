package com.olivtopa.poseidon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olivtopa.poseidon.domain.RuleName;
import com.olivtopa.poseidon.repositories.RuleNameRepository;

@Service
public class RuleNameService {

	@Autowired
	RuleNameRepository ruleNameRepository;
	
	public Iterable<RuleName> getAllRuleName() {
		return ruleNameRepository.findAll();
	}

	public void save(RuleName ruleName) {
		ruleNameRepository.save(ruleName);
	}

	public void deleteBid(RuleName ruleName) {
		ruleNameRepository.delete(ruleName);
	}

}
