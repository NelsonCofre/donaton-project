package com.donaton.logistic;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class LogisticApiIntegrationTest {

	private static final String COLLECTION_CENTERS = "/api/v1/logistics/collection-centers";
	private static final String INVENTORIES = "/api/v1/logistics/inventories";
	private static final String SHIPMENTS = "/api/v1/logistics/shipments";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void collectionCenterCrudFlow() throws Exception {
		String body = objectMapper.writeValueAsString(Map.of(
				"name", "Centro Norte",
				"location", "Av. Principal 100, Santiago"));

		String created = mockMvc.perform(post(COLLECTION_CENTERS)
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("Centro Norte"))
				.andReturn()
				.getResponse()
				.getContentAsString();

		Long id = objectMapper.readTree(created).get("id").asLong();

		mockMvc.perform(get(COLLECTION_CENTERS))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));

		mockMvc.perform(get(COLLECTION_CENTERS + "/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.location").value("Av. Principal 100, Santiago"));

		String updateBody = objectMapper.writeValueAsString(Map.of(
				"name", "Centro Norte Ampliado",
				"location", "Av. Principal 200, Santiago"));

		mockMvc.perform(put(COLLECTION_CENTERS + "/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Centro Norte Ampliado"));

		mockMvc.perform(delete(COLLECTION_CENTERS + "/" + id)).andExpect(status().isNoContent());

		mockMvc.perform(get(COLLECTION_CENTERS + "/" + id)).andExpect(status().isNotFound());
	}

	@Test
	void inventoryCrudFlow() throws Exception {
		Long centerId = createCollectionCenter("Centro Inventario", "Región Metropolitana");

		String body = objectMapper.writeValueAsString(Map.of(
				"centerId", centerId,
				"resource", "Agua embotellada",
				"quantity", 120));

		String created = mockMvc.perform(post(INVENTORIES)
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.centerId").value(centerId.intValue()))
				.andExpect(jsonPath("$.quantity").value(120))
				.andReturn()
				.getResponse()
				.getContentAsString();

		Long id = objectMapper.readTree(created).get("id").asLong();

		mockMvc.perform(get(INVENTORIES + "/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resource").value("Agua embotellada"));

		String updateBody = objectMapper.writeValueAsString(Map.of(
				"centerId", centerId,
				"resource", "Frazadas",
				"quantity", 50));

		mockMvc.perform(put(INVENTORIES + "/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resource").value("Frazadas"))
				.andExpect(jsonPath("$.quantity").value(50));

		mockMvc.perform(delete(INVENTORIES + "/" + id)).andExpect(status().isNoContent());

		mockMvc.perform(get(INVENTORIES + "/" + id)).andExpect(status().isNotFound());
	}

	@Test
	void shipmentCrudFlow() throws Exception {
		Long centerId = createCollectionCenter("Centro Envíos", "Valparaíso");

		String body = objectMapper.writeValueAsString(Map.of(
				"date", LocalDate.of(2026, 5, 15).toString(),
				"status", "PLANNED",
				"centerId", centerId));

		String created = mockMvc.perform(post(SHIPMENTS)
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.status").value("PLANNED"))
				.andExpect(jsonPath("$.centerId").value(centerId.intValue()))
				.andReturn()
				.getResponse()
				.getContentAsString();

		Long id = objectMapper.readTree(created).get("id").asLong();

		mockMvc.perform(get(SHIPMENTS + "/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.date").value("2026-05-15"));

		String updateBody = objectMapper.writeValueAsString(Map.of(
				"date", LocalDate.of(2026, 5, 16).toString(),
				"status", "IN_TRANSIT",
				"centerId", centerId));

		mockMvc.perform(put(SHIPMENTS + "/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("IN_TRANSIT"))
				.andExpect(jsonPath("$.date").value("2026-05-16"));

		mockMvc.perform(delete(SHIPMENTS + "/" + id)).andExpect(status().isNoContent());

		mockMvc.perform(get(SHIPMENTS + "/" + id)).andExpect(status().isNotFound());
	}

	@Test
	void createInventoryWithUnknownCenterReturns404() throws Exception {
		String body = objectMapper.writeValueAsString(Map.of(
				"centerId", 99999,
				"resource", "Arroz",
				"quantity", 10));

		mockMvc.perform(post(INVENTORIES)
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isNotFound());
	}

	@Test
	void createShipmentWithUnknownCenterReturns404() throws Exception {
		String body = objectMapper.writeValueAsString(Map.of(
				"date", LocalDate.of(2026, 5, 20).toString(),
				"status", "PLANNED",
				"centerId", 99999));

		mockMvc.perform(post(SHIPMENTS)
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isNotFound());
	}

	@Test
	void getNonExistentResourcesReturns404() throws Exception {
		mockMvc.perform(get(COLLECTION_CENTERS + "/99999")).andExpect(status().isNotFound());
		mockMvc.perform(get(INVENTORIES + "/99999")).andExpect(status().isNotFound());
		mockMvc.perform(get(SHIPMENTS + "/99999")).andExpect(status().isNotFound());
	}

	@Test
	void createWithInvalidBodyReturns400() throws Exception {
		String invalidCenter = objectMapper.writeValueAsString(Map.of(
				"name", "",
				"location", "Sin nombre"));

		mockMvc.perform(post(COLLECTION_CENTERS)
						.contentType(MediaType.APPLICATION_JSON)
						.content(invalidCenter))
				.andExpect(status().isBadRequest());

		Long centerId = createCollectionCenter("Centro Validación", "Biobío");

		String invalidInventory = objectMapper.writeValueAsString(Map.of(
				"centerId", centerId,
				"resource", "Medicamentos",
				"quantity", 0));

		mockMvc.perform(post(INVENTORIES)
						.contentType(MediaType.APPLICATION_JSON)
						.content(invalidInventory))
				.andExpect(status().isBadRequest());
	}

	private Long createCollectionCenter(String name, String location) throws Exception {
		String body = objectMapper.writeValueAsString(Map.of("name", name, "location", location));
		String response = mockMvc.perform(post(COLLECTION_CENTERS)
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();
		JsonNode node = objectMapper.readTree(response);
		return node.get("id").asLong();
	}
}
