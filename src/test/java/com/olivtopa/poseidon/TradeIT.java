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
import com.olivtopa.poseidon.domain.Trade;
import com.olivtopa.poseidon.exceptions.DataNotFoundException;
import com.olivtopa.poseidon.repositories.TradeRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TradeIT {

	@Autowired
	TradeRepository tradeRepository;
	@Autowired
	MockMvc mockMvc;

	private final static String homeUrl = "/trade/list";
	private final static String createFormUrl = "/trade/add";
	private final static String createUrl = "/trade/validate";
	private final static String updateFormUrl = "/trade/update/{id}";
	private final static String updateUrl = "/trade/update/{id}";
	private final static String deleteUrl = "/trade/delete/{id}";

	@Test
	public void home() throws Exception {
		tradeRepository.save(buildValid());

		mockMvc.perform(get(homeUrl).with(user("testAdmin").password("test").roles("USER"))).andExpect(status().isOk())
				.andExpect(model().attribute("trade", iterableWithSize(1))).andExpect(view().name("trade/list"));

		mockMvc.perform(get(homeUrl).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void addTradeForm() throws Exception {
		mockMvc.perform(get(createFormUrl).with(user("userTest").roles("USER"))).andExpect(status().isOk())
				.andExpect(view().name("trade/add"));

		mockMvc.perform(get(createFormUrl).with(user("userTest").roles("USER"))).andExpect(status().isOk())
				.andExpect(view().name("trade/add"));

		mockMvc.perform(get(createFormUrl).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@Transactional
	public void validate() throws Exception {

		Trade valid = buildValid();
		valid.setAccount("account");
		valid.setType("Type");
		valid.setBuyQuantity(12.9);
		Trade invalid = new Trade();

		mockMvc.perform(post(createUrl).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(valid)))
				.andExpect(status().isFound()).andExpect(redirectedUrl(homeUrl));

		mockMvc.perform(post(createUrl).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(invalid)))
				.andExpect(status().isOk());

	}

	private Trade buildValid() {
		Trade valid = new Trade();
		valid.setAccount("account");
		valid.setType("type");
		valid.setTradeId(14);
		valid.setBuyQuantity(34.8);
		return valid;
	}

	@Test
	public void showUpdateForm() throws Exception {

		Trade trade = tradeRepository.save(buildValid());

		mockMvc.perform(get(updateFormUrl, trade.getTradeId()).with(user("userTest").roles("USER")))
				.andExpect(status().isOk()).andExpect(view().name("trade/update"));

		mockMvc.perform(get(updateFormUrl, trade.getTradeId()).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));

		mockMvc.perform(get(updateFormUrl, 999).with(user("userTest").roles("USER"))).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));
	}

	@Test
	@Transactional
	public void updateTrade() throws Exception {

		tradeRepository.save(buildValid());

		Trade valid = new Trade();
		valid.setTradeId(3);
		valid.setAccount("account");
		valid.setType("type");
		valid.setBuyQuantity(56.6);
		Trade invalid = new Trade();

		mockMvc.perform(post(updateUrl, 1).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(valid)))
				.andExpect(status().isFound()).andExpect(redirectedUrl(homeUrl));

		mockMvc.perform(post(updateUrl, 1).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(invalid)))
				.andExpect(status().isOk());
	}

	@Test
	@Transactional
	public void deleteTrade() throws Exception {

		Trade trade = tradeRepository.save(buildValid());

		mockMvc.perform(get(deleteUrl, trade.getTradeId()).with(user("userTest").roles("USER")))
				.andExpect(redirectedUrl(homeUrl)).andExpect(status().isFound());

		assertTrue(tradeRepository.findById(trade.getTradeId()).isEmpty());

		mockMvc.perform(get(deleteUrl, 1).with(user("userTest").roles("USER"))).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));
	}

}
