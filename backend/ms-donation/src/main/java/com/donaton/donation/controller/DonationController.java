package com.donaton.donation.controller;

import com.donaton.donation.dto.DonationRequestDto;
import com.donaton.donation.dto.DonationResponseDto;
import com.donaton.donation.service.DonationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Donaciones", description = "CRUD de donaciones de recursos recibidas por la organización")
@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
public class DonationController {

	private final DonationService donationService;

	@Operation(summary = "Registrar donación", description = "Crea una nueva donación con recurso, cantidad, origen, fecha y centro de acopio.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Donación creada",
			content = @Content(schema = @Schema(implementation = DonationResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
	})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DonationResponseDto create(@Valid @RequestBody DonationRequestDto request) {
		return donationService.create(request);
	}

	@Operation(summary = "Listar donaciones", description = "Obtiene todas las donaciones registradas.")
	@ApiResponse(responseCode = "200", description = "Listado de donaciones",
		content = @Content(array = @ArraySchema(schema = @Schema(implementation = DonationResponseDto.class))))
	@GetMapping
	public List<DonationResponseDto> list() {
		return donationService.findAll();
	}

	@Operation(summary = "Obtener donación por ID", description = "Consulta una donación específica por su identificador.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Donación encontrada",
			content = @Content(schema = @Schema(implementation = DonationResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "Donación no encontrada")
	})
	@GetMapping("/{id}")
	public DonationResponseDto getById(
		@Parameter(description = "Identificador de la donación", example = "1") @PathVariable Long id
	) {
		return donationService.findById(id);
	}

	@Operation(summary = "Actualizar donación", description = "Reemplaza los datos de una donación existente.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Donación actualizada",
			content = @Content(schema = @Schema(implementation = DonationResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "404", description = "Donación no encontrada")
	})
	@PutMapping("/{id}")
	public DonationResponseDto update(
		@Parameter(description = "Identificador de la donación", example = "1") @PathVariable Long id,
		@Valid @RequestBody DonationRequestDto request
	) {
		return donationService.update(id, request);
	}

	@Operation(summary = "Eliminar donación", description = "Elimina una donación existente por su identificador.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Donación eliminada"),
		@ApiResponse(responseCode = "404", description = "Donación no encontrada")
	})
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(
		@Parameter(description = "Identificador de la donación", example = "1") @PathVariable Long id
	) {
		donationService.deleteById(id);
	}
}
