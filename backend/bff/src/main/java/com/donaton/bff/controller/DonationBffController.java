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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.donaton.bff.client.DonationServiceClient;
import com.donaton.bff.dto.api.FrontendDonationDtos.CreateDonacionRequest;
import com.donaton.bff.dto.api.FrontendDonationDtos.DonacionResponse;
import com.donaton.bff.mapper.DonationMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Donaciones (Frontend)", description = "CRUD de donaciones expuesto al navegador; requiere JWT válido")
@Validated
@RestController
@RequestMapping("/api/donations")
public class DonationBffController {

	private final DonationServiceClient donationServiceClient;

	public DonationBffController(DonationServiceClient donationServiceClient) {
		this.donationServiceClient = donationServiceClient;
	}

	@Operation(
		summary = "Listar donaciones",
		description = "Obtiene todas las donaciones desde donation-service con formato adaptado al frontend.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Listado de donaciones",
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = DonacionResponse.class)))),
		@ApiResponse(responseCode = "401", description = "Token ausente o inválido")
	})
	@GetMapping
	public List<DonacionResponse> list(
		@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authorization
	) {
		return donationServiceClient.list(authorization).stream()
			.map(DonationMapper::toFrontend)
			.toList();
	}

	@Operation(
		summary = "Obtener donación por ID",
		description = "Consulta una donación específica por su identificador.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Donación encontrada",
			content = @Content(schema = @Schema(implementation = DonacionResponse.class))),
		@ApiResponse(responseCode = "401", description = "Token ausente o inválido"),
		@ApiResponse(responseCode = "404", description = "Donación no encontrada")
	})
	@GetMapping("/{id}")
	public DonacionResponse getById(
		@Parameter(description = "Identificador de la donación", example = "1") @PathVariable long id,
		@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authorization
	) {
		return DonationMapper.toFrontend(donationServiceClient.getById(id, authorization));
	}

	@Operation(
		summary = "Registrar donación",
		description = "Crea una donación en donation-service a partir del contrato simplificado del frontend.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Donación creada",
			content = @Content(schema = @Schema(implementation = DonacionResponse.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "401", description = "Token ausente o inválido")
	})
	@PostMapping
	public ResponseEntity<DonacionResponse> create(
		@Valid @RequestBody CreateDonacionRequest request,
		@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authorization
	) {
		var created = donationServiceClient.create(
			DonationMapper.toServiceRequest(request),
			authorization
		);
		return ResponseEntity.status(HttpStatus.CREATED).body(DonationMapper.toFrontend(created));
	}

	@Operation(
		summary = "Actualizar donación",
		description = "Reemplaza los datos de una donación existente.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Donación actualizada",
			content = @Content(schema = @Schema(implementation = DonacionResponse.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "401", description = "Token ausente o inválido"),
		@ApiResponse(responseCode = "404", description = "Donación no encontrada")
	})
	@PutMapping("/{id}")
	public DonacionResponse update(
		@Parameter(description = "Identificador de la donación", example = "1") @PathVariable long id,
		@Valid @RequestBody CreateDonacionRequest request,
		@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authorization
	) {
		var updated = donationServiceClient.update(
			id,
			DonationMapper.toServiceRequest(request),
			authorization
		);
		return DonationMapper.toFrontend(updated);
	}

	@Operation(
		summary = "Eliminar donación",
		description = "Elimina una donación existente por su identificador.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Donación eliminada"),
		@ApiResponse(responseCode = "401", description = "Token ausente o inválido"),
		@ApiResponse(responseCode = "404", description = "Donación no encontrada")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
		@Parameter(description = "Identificador de la donación", example = "1") @PathVariable long id,
		@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authorization
	) {
		donationServiceClient.delete(id, authorization);
		return ResponseEntity.noContent().build();
	}
}
