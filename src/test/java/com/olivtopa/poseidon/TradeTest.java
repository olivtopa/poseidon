package com.olivtopa.poseidon;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.olivtopa.poseidon.domain.Trade;
import com.olivtopa.poseidon.repositories.TradeRepository;




@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradeTest {
	@Autowired
	private TradeRepository tradeRepository;

	@Test
	public void tradeTest() {
		Double buyQuantity = null;
		Trade trade = new Trade("Trade Account", "Type",buyQuantity);

		// Save
		trade = tradeRepository.save(trade);
		Assertions.assertThat(trade.getTradeId()).isNotNull();
		Assertions.assertThat(trade.getAccount()).isEqualTo("Trade Account");

		// Update
		trade.setAccount("Trade Account Update");
		trade = tradeRepository.save(trade);
		Assertions.assertThat(trade.getAccount()).isEqualTo("Trade Account Update");

		// Find
		List<Trade> listResult = tradeRepository.findAll();
		Assertions.assertThat(listResult.size()).isGreaterThan(0);

		// Delete
		Integer id = trade.getTradeId();
		tradeRepository.delete(trade);
		Optional<Trade> tradeList = tradeRepository.findById(id);
		Assertions.assertThat(!tradeList.isPresent());
	}
}
