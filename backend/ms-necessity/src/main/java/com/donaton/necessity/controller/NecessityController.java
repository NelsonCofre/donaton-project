package com.donaton.necessity.controller;

import com.donaton.necessity.dto.NecessityRequestDto;
import com.donaton.necessity.dto.NecessityResponseDto;
import com.donaton.necessity.service.NecessityService;
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

@Tag(name = "Necesidades", description = "CRUD de necesidades en terreno reportadas en situaciones de emergencia")
@RestController
@RequestMapping("/api/v1/necessities")
@RequiredArgsConstructor
public class NecessityController {

	private final NecessityService necessityService;

	@Operation(summary = "Registrar necesidad", description = "Crea una nueva necesidad en terreno con recurso, cantidad, ubicación y fecha de reporte.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Necesidad creada",
			content = @Content(schema = @Schema(implementation = NecessityResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
	})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public NecessityResponseDto create(@Valid @RequestBody NecessityRequestDto request) {
		return necessityService.create(request);
	}

	@Operation(summary = "Listar necesidades", description = "Obtiene todas las necesidades registradas, incluidos los datos de semilla Flyway.")
	@ApiResponse(responseCode = "200", description = "Listado de necesidades",
		content = @Content(array = @ArraySchema(schema = @Schema(implementation = NecessityResponseDto.class))))
	@GetMapping
	public List<NecessityResponseDto> list() {
		return necessityService.findAll();
	}

	@Operation(summary = "Obtener necesidad por ID", description = "Consulta una necesidad específica por su identificador.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Necesidad encontrada",
			content = @Content(schema = @Schema(implementation = NecessityResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "Necesidad no encontrada")
	})
	@GetMapping("/{id}")
	public NecessityResponseDto getById(
		@Parameter(description = "Identificador de la necesidad", example = "1") @PathVariable Long id
	) {
		return necessityService.findById(id);
	}

	@Operation(summary = "Actualizar necesidad", description = "Reemplaza los datos de una necesidad existente.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Necesidad actualizada",
			content = @Content(schema = @Schema(implementation = NecessityResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "404", description = "Necesidad no encontrada")
	})
	@PutMapping("/{id}")
	public NecessityResponseDto update(
		@Parameter(description = "Identificador de la necesidad", example = "1") @PathVariable Long id,
		@Valid @RequestBody NecessityRequestDto request
	) {
		return necessityService.update(id, request);
	}

	@Operation(summary = "Eliminar necesidad", description = "Elimina una necesidad existente por su identificador.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Necesidad eliminada"),
		@ApiResponse(responseCode = "404", description = "Necesidad no encontrada")
	})
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(
		@Parameter(description = "Identificador de la necesidad", example = "1") @PathVariable Long id
	) {
		necessityService.deleteById(id);
	}
}
