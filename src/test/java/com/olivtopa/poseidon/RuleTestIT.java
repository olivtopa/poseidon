package com.olivtopa.poseidon;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static com.olivtopa.poseidon.FormatToUrlEncoded.getUrlEncoded;
import com.olivtopa.poseidon.domain.RuleName;
import com.olivtopa.poseidon.exceptions.DataNotFoundException;
import com.olivtopa.poseidon.repositories.RuleNameRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class RuleTestIT {

	@Autowired
	RuleNameRepository ruleNameRepository;
	@Autowired
	MockMvc mockMvc;

	private final static String homeUrl = "/ruleName/list";
	private final static String createFormUrl = "/ruleName/add";
	private final static String createUrl = "/ruleName/validate";
	private final static String updateFormUrl = "/ruleName/update/{id}";
	private final static String updateUrl = "/ruleName/update/{id}";
	private final static String deleteUrl = "/ruleName/delete/{id}";

	@Test
	public void home() throws Exception {
		ruleNameRepository.save(buildValid());

		mockMvc.perform(get(homeUrl).with(user("testAdmin").password("test").roles("USER"))).andExpect(status().isOk())
				.andExpect(model().attribute("ruleName", iterableWithSize(1))).andExpect(view().name("ruleName/list"));

		mockMvc.perform(get(homeUrl).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void addBidForm() throws Exception {
		mockMvc.perform(get(createFormUrl).with(user("userTest").roles("USER"))).andExpect(status().isOk())
				.andExpect(view().name("ruleName/add"));

		mockMvc.perform(get(createFormUrl).with(user("userTest").roles("USER"))).andExpect(status().isOk())
				.andExpect(view().name("ruleName/add"));

		mockMvc.perform(get(createFormUrl).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@Transactional
	public void validate() throws Exception {

		RuleName valid = buildValid();
		RuleName invalid = new RuleName();
		invalid.setName("");
		invalid.setDescription("");

		mockMvc.perform(post(createUrl).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(valid)))
				.andExpect(status().isFound()).andExpect(redirectedUrl(homeUrl));

		mockMvc.perform(post(createUrl).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(invalid)))
				.andExpect(status().isOk());

	}

	private RuleName buildValid() {
		RuleName valid = new RuleName();
		valid.setName("Myname");
		valid.setDescription("Description");
		return valid;
	}

	@Test
	public void showUpdateForm() throws Exception {

		RuleName ruleName = ruleNameRepository.save(buildValid());

		mockMvc.perform(get(updateFormUrl, ruleName.getId()).with(user("userTest").roles("USER")))
				.andExpect(status().isOk()).andExpect(view().name("ruleName/update"));

		mockMvc.perform(get(updateFormUrl, ruleName.getId()).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));

		mockMvc.perform(get(updateFormUrl, 999).with(user("userTest").roles("USER"))).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));
	}

	@Test
	@Transactional
	public void updaterule() throws Exception {

		ruleNameRepository.save(buildValid());

		RuleName valid = new RuleName();
		valid.setName("Myname");
		valid.setDescription("Description");
		RuleName invalid = new RuleName();
		invalid.setName("");
		invalid.setDescription("");

		mockMvc.perform(post(updateUrl, 1).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(valid)))
				.andExpect(status().isFound()).andExpect(redirectedUrl("/ruleName/list"));

		mockMvc.perform(post(updateUrl, 1).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(invalid)))
				.andExpect(status().isOk());
	}

	@Test
	@Transactional
	public void deleterule() throws Exception {

		RuleName ruleName = ruleNameRepository.save(buildValid());

		mockMvc.perform(get(deleteUrl, ruleName.getId()).with(user("userTest").roles("USER")))
				.andExpect(redirectedUrl(homeUrl)).andExpect(status().isFound());

		assertTrue(ruleNameRepository.findById(ruleName.getId()).isEmpty());

		mockMvc.perform(get(deleteUrl, 1).with(user("userTest").roles("USER"))).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));
	}

}
