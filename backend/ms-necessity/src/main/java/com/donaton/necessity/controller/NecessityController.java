package com.donaton.necessity.controller;

import com.donaton.necessity.dto.NecessityRequestDto;
import com.donaton.necessity.dto.NecessityResponseDto;
import com.donaton.necessity.service.NecessityService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/necessities")
@RequiredArgsConstructor
public class NecessityController {

	private final NecessityService necessityService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public NecessityResponseDto create(@Valid @RequestBody NecessityRequestDto request) {
		return necessityService.create(request);
	}

	@GetMapping
	public List<NecessityResponseDto> list() {
		return necessityService.findAll();
	}

	@GetMapping("/{id}")
	public NecessityResponseDto getById(@PathVariable Long id) {
		return necessityService.findById(id);
	}

	@PutMapping("/{id}")
	public NecessityResponseDto update(@PathVariable Long id, @Valid @RequestBody NecessityRequestDto request) {
		return necessityService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		necessityService.deleteById(id);
	}
}
