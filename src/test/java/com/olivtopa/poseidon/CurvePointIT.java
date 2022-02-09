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

import com.olivtopa.poseidon.domain.CurvePoint;
import com.olivtopa.poseidon.exceptions.DataNotFoundException;
import com.olivtopa.poseidon.repositories.CurvePointRepository;
import static com.olivtopa.poseidon.FormatToUrlEncoded.getUrlEncoded;

@SpringBootTest
@AutoConfigureMockMvc
public class CurvePointIT {

	@Autowired
	CurvePointRepository curvePointRepository;
	@Autowired
	MockMvc mockMvc;

	private final static String homeUrl = "/curvePoint/list";
	private final static String createFormUrl = "/curvePoint/add";
	private final static String createUrl = "/curvePoint/validate";
	private final static String updateFormUrl = "/curvePoint/update/{id}";
	private final static String updateUrl = "/curvePoint/update/{id}";
	private final static String deleteUrl = "/curvePoint/delete/{id}";

	private CurvePoint buildValid() {
		CurvePoint valid = new CurvePoint();
		valid.setCurveId(10);
		valid.setTerm(15.3);
		valid.setValue(32.0);
		return valid;
	}

	@Test
	public void home() throws Exception {
		curvePointRepository.save(buildValid());

		mockMvc.perform(get(homeUrl).with(user("testAdmin").password("test").roles("USER"))).andExpect(status().isOk())
				.andExpect(model().attribute("curvePoints", iterableWithSize(1)))
				.andExpect(view().name("curvePoint/list"));

		mockMvc.perform(get(homeUrl).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void addCurveForm() throws Exception {
		mockMvc.perform(get(createFormUrl).with(user("userTest").roles("USER"))).andExpect(status().isOk())
				.andExpect(view().name("curvePoint/add"));

		mockMvc.perform(get(createFormUrl).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@Transactional
	public void validate() throws Exception {
		CurvePoint valid = buildValid();
		CurvePoint invalid = new CurvePoint();
		invalid.setCurveId(null);
		invalid.setTerm(null);

		mockMvc.perform(post(createUrl).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(valid)))
				.andExpect(status().isFound()).andExpect(redirectedUrl(homeUrl));

		mockMvc.perform(post(createUrl).with(user("userTest").roles("USER"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).content(getUrlEncoded(invalid)))
				.andExpect(status().isOk());
	}

	@Test
	public void showUpdateForm() throws Exception {

		CurvePoint curve = curvePointRepository.save(buildValid());

		mockMvc.perform(get(updateFormUrl, curve.getId()).with(user("userTest").roles("USER")))
				.andExpect(status().isOk()).andExpect(view().name("curvePoint/update"));

		mockMvc.perform(get(updateFormUrl, curve.getId()).with(anonymous())).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));

		mockMvc.perform(get(updateFormUrl, 999).with(user("userTest").roles("USER"))).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));

	}

	@Test
	@Transactional
	public void updateCurve() throws Exception {

		curvePointRepository.save(buildValid());

		CurvePoint valid = new CurvePoint();
		valid.setCurveId(9);
		valid.setTerm(12.3);
		valid.setValue(16.3);
		CurvePoint invalid = new CurvePoint();
		invalid.setCurveId(null);

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

		CurvePoint curve = curvePointRepository.save(buildValid());

		mockMvc.perform(get(deleteUrl, curve.getId()).with(user("userTest").roles("USER")))
				.andExpect(redirectedUrl(homeUrl)).andExpect(status().isFound());

		assertTrue(curvePointRepository.findById(curve.getId()).isEmpty());

		mockMvc.perform(get(deleteUrl, 1).with(user("userTest").roles("USER"))).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DataNotFoundException));

	}

}
