package com.donaton.bff.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

import com.donaton.bff.exception.UpstreamServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;

final class RestClientHelper {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private RestClientHelper() {
	}

	static RestClient.ResponseSpec withErrorHandling(RestClient.ResponseSpec spec) {
		return spec.onStatus(HttpStatusCode::isError, (request, response) -> {
			throw new UpstreamServiceException(response.getStatusCode(), readMessage(response));
		});
	}

	private static String readMessage(ClientHttpResponse response) throws IOException {
		byte[] bytes = response.getBody().readAllBytes();
		if (bytes.length == 0) {
			return response.getStatusCode().toString();
		}
		String raw = new String(bytes, StandardCharsets.UTF_8);
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = MAPPER.readValue(raw, Map.class);
			Object message = map.get("message");
			if (message instanceof String text && !text.isBlank()) {
				return text;
			}
		} catch (Exception ignored) {
			// usar texto crudo
		}
		return raw;
	}
}
