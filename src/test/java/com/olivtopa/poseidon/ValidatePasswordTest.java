package com.olivtopa.poseidon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.passay.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.olivtopa.poseidon.domain.User;
import com.olivtopa.poseidon.validation.contraints.PasswordConstraintValidator;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ValidatePasswordTest {

	@Test
	public void validatePassword_Null() {
		PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();
		// Given
		User user = new User();
		user.setUsername("Oliv");
		user.setFullname("toto");
		user.setRole("role1");
		// When
		user.setPassword("");
		// Then
		Assertions.assertFalse(passwordConstraintValidator.isValid(user.getPassword(), null));
	}
}
