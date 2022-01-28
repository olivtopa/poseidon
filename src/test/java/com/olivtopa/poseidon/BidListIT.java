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
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


import static com.olivtopa.poseidon.FormatToUrlEncoded.getUrlEncoded;
import com.olivtopa.poseidon.domain.BidList;
import com.olivtopa.poseidon.exceptions.DataNotFoundException;
import com.olivtopa.poseidon.repositories.BidListRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class BidListIT {

	@Autowired
	BidListRepository bidListRepository;
	@Autowired
	MockMvc mockMvc;

	private final static String homeUrl = "/bidList/list";
	private final static String createFormUrl = "/bidList/add";
	private final static String createUrl = "/bidList/validate";
	private final static String updateFormUrl = "/bidList/update/{id}";
	private final static String updateUrl = "/bidList/update/{id}";
	private final static String deleteUrl = "/bidList/delete/{id}";

	@Test
	public void home() throws Exception {
		bidListRepository.save(buildValid());

		mockMvc.perform(get(homeUrl).with(user("testAdmin").password("test").roles("USER"))).andExpect(status().isOk())
				.andExpect(model().attribute("bidlist", iterableWithSize(1))).andExpect(view().name("bidList/list"));

		mockMvc.perform(get(homeUrl).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void addBidForm() throws Exception {
		mockMvc.perform(get(createFormUrl).with(user("userTest").roles("USER"))).andExpect(status().isOk())
				.andExpect(view().name("bidList/add"));

		mockMvc.perform(get(createFormUrl).with(user("userTest").roles("USER"))).andExpect(status().isOk())
				.andExpect(view().name("bidList/add"));

		mockMvc.perform(get(createFormUrl).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@Transactional
	public void validate() throws Exception {

		BidList valid = buildValid();
		BidList invalid = new BidList();
		invalid.setAccount("account");
		invalid.setType("");

		mockMvc.perform(post(createUrl).with(user("userTest").roles("USER")).content(getUrlEncoded(valid)))
				.andExpect(status().isFound()).andExpect(redirectedUrl(homeUrl));

		mockMvc.perform(post(createUrl).with(user("userTest").roles("USER")).content(getUrlEncoded(invalid)))
				.andExpect(status().isFound());

	}

	private BidList buildValid() {
		BidList valid = new BidList();
		valid.setAccount("account");
		valid.setType("type");
		valid.setBidQuantity(22.4);
		return valid;
	}

	@Test
	public void showUpdateForm() throws Exception {

		BidList bidList = bidListRepository.save(buildValid());

		mockMvc.perform(get(updateFormUrl, bidList.getBidListId()).with(user("userTest").roles("USER")))
				.andExpect(status().isOk()).andExpect(view().name("bidList/update"));

		mockMvc.perform(get(updateFormUrl, bidList.getBidListId()).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));

		mockMvc.perform(get(updateFormUrl, 999).with(user("userTest").roles("USER"))).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));
	}

	@Test
	@Transactional
	public void updateBid() throws Exception {

		bidListRepository.save(buildValid());

		BidList valid = new BidList();
		valid.setBidListId(0);
		valid.setAccount("account");
		valid.setType("type");
		valid.setBidQuantity(22.4);
		valid.setAsk(22.4);
		BidList invalid = new BidList();
		invalid.setAccount("account");
		invalid.setType("");

		mockMvc.perform(post(updateUrl, 1).with(user("userTest").roles("USER")).content(getUrlEncoded(valid)))
				.andExpect(status().isFound()).andExpect(redirectedUrl("/bidList/list"));

		mockMvc.perform(post(updateUrl, 1).with(user("userTest").roles("USER")).content(getUrlEncoded(invalid)))
				.andExpect(status().isFound());
	}

	@Test
	@Transactional
	public void deleteBid() throws Exception {

		BidList bidList = bidListRepository.save(buildValid());

		mockMvc.perform(
				// TODO should be a delete
				get(deleteUrl, bidList.getBidListId()).with(user("userTest").roles("USER")))
				.andExpect(redirectedUrl(homeUrl)).andExpect(status().isFound());

		assertTrue(bidListRepository.findById(bidList.getBidListId()).isEmpty());

		mockMvc.perform(get(deleteUrl, 1).with(user("userTest").roles("USER"))).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));
	}

}
