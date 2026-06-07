package com.donaton.necessity;

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
class NecessityApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void crudFlow() throws Exception {
		String body = objectMapper.writeValueAsString(Map.of(
				"resourceName", "Frazadas",
				"quantity", 50,
				"location", "Valparaíso, sector Playa Ancha",
				"reportedDate", LocalDate.of(2026, 5, 10).toString(),
				"reportedBy", "Municipalidad de Valparaíso"));

		String created = mockMvc.perform(post("/api/v1/necessities")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.resourceName").value("Frazadas"))
				.andReturn()
				.getResponse()
				.getContentAsString();

		Long id = objectMapper.readTree(created).get("id").asLong();

		mockMvc.perform(get("/api/v1/necessities"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));

		mockMvc.perform(get("/api/v1/necessities/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$.quantity").value(50));

		String updateBody = objectMapper.writeValueAsString(Map.of(
				"resourceName", "Frazadas",
				"quantity", 30,
				"location", "Valparaíso, sector Barrio Puerto",
				"reportedDate", LocalDate.of(2026, 5, 11).toString(),
				"reportedBy", "Centro Investigador Regional"));

		mockMvc.perform(put("/api/v1/necessities/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.quantity").value(30))
				.andExpect(jsonPath("$.location").value("Valparaíso, sector Barrio Puerto"));

		mockMvc.perform(delete("/api/v1/necessities/" + id)).andExpect(status().isNoContent());

		mockMvc.perform(get("/api/v1/necessities/" + id)).andExpect(status().isNotFound());
	}
}
