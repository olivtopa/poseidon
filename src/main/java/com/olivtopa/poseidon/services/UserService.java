package com.olivtopa.poseidon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olivtopa.poseidon.domain.User;
import com.olivtopa.poseidon.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public Iterable<User> getUsers() {
		return userRepository.findAll();
	}

}
