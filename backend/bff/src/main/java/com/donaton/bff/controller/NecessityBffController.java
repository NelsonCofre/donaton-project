package com.donaton.bff.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.donaton.bff.client.NecessityServiceClient;
import com.donaton.bff.dto.api.FrontendNecessityDtos.CreateNecesidadRequest;
import com.donaton.bff.dto.api.FrontendNecessityDtos.NecesidadResponse;
import com.donaton.bff.mapper.NecessityMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Necesidades (Frontend)", description = "CRUD de necesidades adaptado para el frontend")
@Validated
@RestController
@RequestMapping("/api/v1/necessities")
public class NecessityBffController {

	private final NecessityServiceClient necessityServiceClient;

	public NecessityBffController(NecessityServiceClient necessityServiceClient) {
		this.necessityServiceClient = necessityServiceClient;
	}

	@GetMapping
	public List<NecesidadResponse> list() {
		return necessityServiceClient.list().stream()
			.map(NecessityMapper::toFrontend)
			.toList();
	}

	@GetMapping("/{id}")
	public NecesidadResponse getById(@PathVariable long id) {
		return NecessityMapper.toFrontend(necessityServiceClient.getById(id));
	}

	@PostMapping
	public ResponseEntity<NecesidadResponse> create(@Valid @RequestBody CreateNecesidadRequest request) {
		var created = necessityServiceClient.create(NecessityMapper.toServiceRequest(request));
		return ResponseEntity.status(HttpStatus.CREATED).body(NecessityMapper.toFrontend(created));
	}

	@PutMapping("/{id}")
	public NecesidadResponse update(
		@PathVariable long id,
		@Valid @RequestBody CreateNecesidadRequest request
	) {
		var updated = necessityServiceClient.update(id, NecessityMapper.toServiceRequest(request));
		return NecessityMapper.toFrontend(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id) {
		necessityServiceClient.delete(id);
		return ResponseEntity.noContent().build();
	}
}
