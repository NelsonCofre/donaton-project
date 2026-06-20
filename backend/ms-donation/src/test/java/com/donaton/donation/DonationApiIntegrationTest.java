package com.donaton.donation;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DonationApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void crudFlow() throws Exception {
		String body = objectMapper.writeValueAsString(Map.of(
				"resourceName", "Agua embotellada",
				"quantity", 120,
				"origin", "Empresa XYZ",
				"donationDate", LocalDate.of(2026, 5, 10).toString(),
				"warehouseName", "Centro Norte"));

		String created = mockMvc.perform(post("/api/v1/donations")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.resourceName").value("Agua embotellada"))
				.andReturn()
				.getResponse()
				.getContentAsString();

		Long id = objectMapper.readTree(created).get("id").asLong();

		mockMvc.perform(get("/api/v1/donations"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));

		mockMvc.perform(get("/api/v1/donations/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$.quantity").value(120));

		String updateBody = objectMapper.writeValueAsString(Map.of(
				"resourceName", "Agua embotellada",
				"quantity", 80,
				"origin", "Empresa XYZ",
				"donationDate", LocalDate.of(2026, 5, 11).toString(),
				"warehouseName", "Centro Sur"));

		mockMvc.perform(put("/api/v1/donations/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.quantity").value(80))
				.andExpect(jsonPath("$.warehouseName").value("Centro Sur"));

		mockMvc.perform(delete("/api/v1/donations/" + id)).andExpect(status().isNoContent());

		mockMvc.perform(get("/api/v1/donations/" + id)).andExpect(status().isNotFound());
	}

	@Test
	void updateMissingDonationReturns404() throws Exception {
		String body = objectMapper.writeValueAsString(Map.of(
				"resourceName", "Agua embotellada",
				"quantity", 10,
				"origin", "Empresa XYZ",
				"donationDate", LocalDate.of(2026, 5, 10).toString(),
				"warehouseName", "Centro Norte"));

		mockMvc.perform(put("/api/v1/donations/99999")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteMissingDonationReturns404() throws Exception {
		mockMvc.perform(delete("/api/v1/donations/99999")).andExpect(status().isNotFound());
	}

	@Test
	void createWithInvalidBodyReturns400() throws Exception {
		String body = objectMapper.writeValueAsString(Map.of(
				"resourceName", "",
				"quantity", 0,
				"origin", "",
				"donationDate", LocalDate.of(2026, 5, 10).toString(),
				"warehouseName", ""));

		mockMvc.perform(post("/api/v1/donations")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isBadRequest());
	}
}
