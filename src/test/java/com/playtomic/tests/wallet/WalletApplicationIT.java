package com.playtomic.tests.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.playtomic.tests.wallet.dto.TopUpRequest;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@ActiveProfiles(profiles = "test")
public class WalletApplicationIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
    private ObjectMapper jacksonObjectMapper;

	@BeforeEach
	public void setUp() {
		walletRepository.deleteAll();
	}

	@Test
	@Transactional
	public void testTopUpWalletIntegration() throws Exception {
		Wallet wallet = new Wallet();
		wallet.setBalance(BigDecimal.ZERO);
		wallet = walletRepository.save(wallet);

		TopUpRequest topUpRequest = new TopUpRequest();
		topUpRequest.setCreditCard("4242424242424242");
		topUpRequest.setAmount(BigDecimal.TEN);

		stubFor(WireMock.post(urlEqualTo("/"))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withBody("{\"id\": \"some-id\", \"status\": \"succeeded\"}")
						.withStatus(200)));

		mockMvc.perform(post("/" + wallet.getId() + "/top-up")
						.contentType("application/json")
						.content(jacksonObjectMapper.writeValueAsString(topUpRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.balance").value(10.00));

		Wallet updatedWallet = walletRepository.findById(wallet.getId()).orElseThrow();
		assertEquals(1, updatedWallet.getTransactions().size());
		assertEquals("some-id", updatedWallet.getTransactions().get(0).getPspReference());
	}
}
