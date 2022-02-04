package com.olivtopa.poseidon;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.olivtopa.poseidon.domain.Rating;
import com.olivtopa.poseidon.exceptions.DataNotFoundException;
import com.olivtopa.poseidon.repositories.RatingRepository;
import static com.olivtopa.poseidon.FormatToUrlEncoded.getUrlEncoded;

@SpringBootTest
@AutoConfigureMockMvc
public class RatingIT {

	@Autowired
	RatingRepository ratingRepository;
	@Autowired
	MockMvc mockMvc;

	private final static String homeUrl = "/rating/list";
	private final static String createFormUrl = "/rating/add";
	private final static String createUrl = "/rating/validate";
	private final static String updateFormUrl = "/rating/update/{id}";
	private final static String updateUrl = "/rating/update/{id}";
	private final static String deleteUrl = "/rating/delete/{id}";

	private Rating buildValid() {
		Rating valid = new Rating();
		valid.setOrderNumber(10);
		return valid;
	}

	@Test
	public void home() throws Exception {
		ratingRepository.save(buildValid());

		mockMvc.perform(get(homeUrl).with(user("testAdmin").password("test").roles("USER"))).andExpect(status().isOk())
				.andExpect(model().attribute("rating", iterableWithSize(1))).andExpect(view().name("rating/list"));

		mockMvc.perform(get(homeUrl).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void addCurveForm() throws Exception {
		mockMvc.perform(get(createFormUrl).with(user("userTest").roles("USER"))).andExpect(status().isOk())
				.andExpect(view().name("rating/add"));

		mockMvc.perform(get(createFormUrl).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@Transactional
	public void validate() throws Exception {
		Rating valid = buildValid();
		Rating invalid = new Rating();

		mockMvc.perform(post(createUrl).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(valid)))
				.andExpect(status().isFound()).andExpect(redirectedUrl(homeUrl));

		mockMvc.perform(post(createUrl).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(invalid)))
				.andExpect(status().isOk());
	}

	@Test
	public void showUpdateForm() throws Exception {

		Rating rating = ratingRepository.save(buildValid());

		mockMvc.perform(get(updateFormUrl, rating.getId()).with(user("userTest").roles("USER")))
				.andExpect(status().isOk()).andExpect(view().name("rating/update"));

		mockMvc.perform(get(updateFormUrl, rating.getId()).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));

		mockMvc.perform(get(updateFormUrl, 999).with(user("userTest").roles("USER"))).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));

	}

	@Test
	@Transactional
	public void updateCurve() throws Exception {

		ratingRepository.save(buildValid());

		Rating valid = new Rating();
		valid.setOrderNumber(9);
		Rating invalid = new Rating();
		
		mockMvc.perform(post(updateUrl, 1).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(valid)))
				.andExpect(status().isFound()).andExpect(redirectedUrl(homeUrl));

		mockMvc.perform(post(updateUrl, 1).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(invalid)))
				.andExpect(status().isOk());
	}

	@Test
	@Transactional
	public void deleteCurve() throws Exception {

		Rating rating = ratingRepository.save(buildValid());

		mockMvc.perform(get(deleteUrl, rating.getId()).with(user("userTest").roles("USER")))
				.andExpect(redirectedUrl(homeUrl)).andExpect(status().isFound());

		assertTrue(ratingRepository.findById(rating.getId()).isEmpty());

		mockMvc.perform(get(deleteUrl, 1).with(user("userTest").roles("USER"))).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));

	}

}
