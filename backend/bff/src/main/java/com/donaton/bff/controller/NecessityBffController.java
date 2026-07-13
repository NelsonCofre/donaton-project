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

import com.donaton.bff.dto.api.FrontendNecessityDtos.CreateNecesidadRequest;
import com.donaton.bff.dto.api.FrontendNecessityDtos.NecesidadResponse;
import com.donaton.bff.service.NecessityBffService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Necesidades (Frontend)", description = "CRUD de necesidades adaptado para el frontend")
@Validated
@RestController
@RequestMapping("/api/v1/necessities")
public class NecessityBffController {

	private final NecessityBffService necessityBffService;

	public NecessityBffController(NecessityBffService necessityBffService) {
		this.necessityBffService = necessityBffService;
	}

	@GetMapping
	public List<NecesidadResponse> list() {
		return necessityBffService.list();
	}

	@GetMapping("/{id}")
	public NecesidadResponse getById(@PathVariable long id) {
		return necessityBffService.getById(id);
	}

	@PostMapping
	public ResponseEntity<NecesidadResponse> create(@Valid @RequestBody CreateNecesidadRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(necessityBffService.create(request));
	}

	@PutMapping("/{id}")
	public NecesidadResponse update(
		@PathVariable long id,
		@Valid @RequestBody CreateNecesidadRequest request
	) {
		return necessityBffService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id) {
		necessityBffService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
