package com.olivtopa.poseidon;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.olivtopa.poseidon.domain.User;
import com.olivtopa.poseidon.validation.contraints.PasswordConstraintValidator;

@ExtendWith(MockitoExtension.class)
public class ValidatePasswordTest {

	@Mock
	ConstraintValidatorContext constraintValidatorContext;
	@Mock
	ConstraintViolationBuilder constraintViolationBuilder;

	PasswordConstraintValidator passwordConstraintValidator = new PasswordConstraintValidator();

	@Test
	public void goodPassword() {
		// Given
		User user = new User();
		user.setUsername("Oliv");
		user.setFullname("toto");
		user.setRole("role1");
		user.setPassword("Maison2@");
		// When + Then
		Assertions.assertTrue(passwordConstraintValidator.isValid(user.getPassword(), constraintValidatorContext));
	}

	@Test
	public void tooShortPassword() {

		// Given
		User user = new User();
		user.setUsername("Oliv");
		user.setFullname("toto");
		user.setRole("role1");
		user.setPassword("Maiso@");
		Mockito.when(constraintValidatorContext.buildConstraintViolationWithTemplate(ArgumentMatchers.anyString()))
				.thenReturn(constraintViolationBuilder);
		// When + Then
		Assertions.assertFalse(passwordConstraintValidator.isValid(user.getPassword(), constraintValidatorContext));
	}
	
	@Test
	public void noSpecialCaractPassword() {

		// Given
		User user = new User();
		user.setUsername("Oliv");
		user.setFullname("toto");
		user.setRole("role1");
		user.setPassword("Maison21");
		Mockito.when(constraintValidatorContext.buildConstraintViolationWithTemplate(ArgumentMatchers.anyString()))
				.thenReturn(constraintViolationBuilder);
		// When + Then
		Assertions.assertFalse(passwordConstraintValidator.isValid(user.getPassword(), constraintValidatorContext));
	}
	@Test
	public void noDigitCaractPassword() {

		// Given
		User user = new User();
		user.setUsername("Oliv");
		user.setFullname("toto");
		user.setRole("role1");
		user.setPassword("Maisson@");
		Mockito.when(constraintValidatorContext.buildConstraintViolationWithTemplate(ArgumentMatchers.anyString()))
				.thenReturn(constraintViolationBuilder);
		// When + Then
		Assertions.assertFalse(passwordConstraintValidator.isValid(user.getPassword(), constraintValidatorContext));
	}
	@Test
	public void noUpercaseCaractPassword() {

		// Given
		User user = new User();
		user.setUsername("Oliv");
		user.setFullname("toto");
		user.setRole("role1");
		user.setPassword("maison@21");
		Mockito.when(constraintValidatorContext.buildConstraintViolationWithTemplate(ArgumentMatchers.anyString()))
				.thenReturn(constraintViolationBuilder);
		// When + Then
		Assertions.assertFalse(passwordConstraintValidator.isValid(user.getPassword(), constraintValidatorContext));
	}
	@Test
	public void noLetterCaractPassword() {

		// Given
		User user = new User();
		user.setUsername("Oliv");
		user.setFullname("toto");
		user.setRole("role1");
		user.setPassword("123456789@");
		Mockito.when(constraintValidatorContext.buildConstraintViolationWithTemplate(ArgumentMatchers.anyString()))
				.thenReturn(constraintViolationBuilder);
		// When + Then
		Assertions.assertFalse(passwordConstraintValidator.isValid(user.getPassword(), constraintValidatorContext));
	}
}