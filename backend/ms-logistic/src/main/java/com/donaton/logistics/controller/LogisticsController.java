package com.donaton.logistics.controller;

import com.donaton.logistics.dto.CentroAcopioRequestDto;
import com.donaton.logistics.dto.CentroAcopioResponseDto;
import com.donaton.logistics.dto.EnvioRequestDto;
import com.donaton.logistics.dto.EnvioResponseDto;
import com.donaton.logistics.dto.InventarioRequestDto;
import com.donaton.logistics.dto.InventarioResponseDto;
import com.donaton.logistics.service.LogisticsService;
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

@Tag(name = "Logística", description = "Gestión de centros de acopio, inventario y envíos")
@RestController
@RequestMapping("/api/v1/logistics")
@RequiredArgsConstructor
public class LogisticsController {

	private final LogisticsService logisticsService;

	@Operation(
		summary = "Registrar centro de acopio",
		description = "Crea un nuevo centro de acopio con nombre y ubicación.",
		tags = { "Centros de acopio" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Centro de acopio creado",
			content = @Content(schema = @Schema(implementation = CentroAcopioResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
	})
	@PostMapping("/collection-centers")
	@ResponseStatus(HttpStatus.CREATED)
	public CentroAcopioResponseDto createCollectionCenter(@Valid @RequestBody CentroAcopioRequestDto request) {
		return logisticsService.createCentroAcopio(request);
	}

	@Operation(
		summary = "Listar centros de acopio",
		description = "Obtiene todos los centros de acopio registrados.",
		tags = { "Centros de acopio" }
	)
	@ApiResponse(responseCode = "200", description = "Listado de centros de acopio",
		content = @Content(array = @ArraySchema(schema = @Schema(implementation = CentroAcopioResponseDto.class))))
	@GetMapping("/collection-centers")
	public List<CentroAcopioResponseDto> listCollectionCenters() {
		return logisticsService.findAllCentrosAcopio();
	}

	@Operation(
		summary = "Obtener centro de acopio por ID",
		description = "Consulta un centro de acopio específico por su identificador.",
		tags = { "Centros de acopio" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Centro de acopio encontrado",
			content = @Content(schema = @Schema(implementation = CentroAcopioResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "Centro de acopio no encontrado")
	})
	@GetMapping("/collection-centers/{id}")
	public CentroAcopioResponseDto getCollectionCenterById(
		@Parameter(description = "Identificador del centro de acopio", example = "1") @PathVariable Long id
	) {
		return logisticsService.findCentroAcopioById(id);
	}

	@Operation(
		summary = "Actualizar centro de acopio",
		description = "Reemplaza los datos de un centro de acopio existente.",
		tags = { "Centros de acopio" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Centro de acopio actualizado",
			content = @Content(schema = @Schema(implementation = CentroAcopioResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "404", description = "Centro de acopio no encontrado")
	})
	@PutMapping("/collection-centers/{id}")
	public CentroAcopioResponseDto updateCollectionCenter(
		@Parameter(description = "Identificador del centro de acopio", example = "1") @PathVariable Long id,
		@Valid @RequestBody CentroAcopioRequestDto request
	) {
		return logisticsService.updateCentroAcopio(id, request);
	}

	@Operation(
		summary = "Eliminar centro de acopio",
		description = "Elimina un centro de acopio existente por su identificador.",
		tags = { "Centros de acopio" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Centro de acopio eliminado"),
		@ApiResponse(responseCode = "404", description = "Centro de acopio no encontrado")
	})
	@DeleteMapping("/collection-centers/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCollectionCenter(
		@Parameter(description = "Identificador del centro de acopio", example = "1") @PathVariable Long id
	) {
		logisticsService.deleteCentroAcopioById(id);
	}

	@Operation(
		summary = "Registrar inventario",
		description = "Crea un registro de inventario asociado a un centro de acopio.",
		tags = { "Inventario" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Inventario creado",
			content = @Content(schema = @Schema(implementation = InventarioResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "404", description = "Centro de acopio no encontrado")
	})
	@PostMapping("/inventories")
	@ResponseStatus(HttpStatus.CREATED)
	public InventarioResponseDto createInventory(@Valid @RequestBody InventarioRequestDto request) {
		return logisticsService.createInventario(request);
	}

	@Operation(
		summary = "Listar inventarios",
		description = "Obtiene todos los registros de inventario.",
		tags = { "Inventario" }
	)
	@ApiResponse(responseCode = "200", description = "Listado de inventarios",
		content = @Content(array = @ArraySchema(schema = @Schema(implementation = InventarioResponseDto.class))))
	@GetMapping("/inventories")
	public List<InventarioResponseDto> listInventories() {
		return logisticsService.findAllInventarios();
	}

	@Operation(
		summary = "Obtener inventario por ID",
		description = "Consulta un registro de inventario específico por su identificador.",
		tags = { "Inventario" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Inventario encontrado",
			content = @Content(schema = @Schema(implementation = InventarioResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "Inventario no encontrado")
	})
	@GetMapping("/inventories/{id}")
	public InventarioResponseDto getInventoryById(
		@Parameter(description = "Identificador del inventario", example = "1") @PathVariable Long id
	) {
		return logisticsService.findInventarioById(id);
	}

	@Operation(
		summary = "Actualizar inventario",
		description = "Reemplaza los datos de un registro de inventario existente.",
		tags = { "Inventario" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Inventario actualizado",
			content = @Content(schema = @Schema(implementation = InventarioResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "404", description = "Inventario no encontrado")
	})
	@PutMapping("/inventories/{id}")
	public InventarioResponseDto updateInventory(
		@Parameter(description = "Identificador del inventario", example = "1") @PathVariable Long id,
		@Valid @RequestBody InventarioRequestDto request
	) {
		return logisticsService.updateInventario(id, request);
	}

	@Operation(
		summary = "Eliminar inventario",
		description = "Elimina un registro de inventario existente por su identificador.",
		tags = { "Inventario" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Inventario eliminado"),
		@ApiResponse(responseCode = "404", description = "Inventario no encontrado")
	})
	@DeleteMapping("/inventories/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteInventory(
		@Parameter(description = "Identificador del inventario", example = "1") @PathVariable Long id
	) {
		logisticsService.deleteInventarioById(id);
	}

	@Operation(
		summary = "Registrar envío",
		description = "Crea un envío asociado a un centro de acopio con fecha y estado.",
		tags = { "Envíos" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Envío creado",
			content = @Content(schema = @Schema(implementation = EnvioResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "404", description = "Centro de acopio no encontrado")
	})
	@PostMapping("/shipments")
	@ResponseStatus(HttpStatus.CREATED)
	public EnvioResponseDto createShipment(@Valid @RequestBody EnvioRequestDto request) {
		return logisticsService.createEnvio(request);
	}

	@Operation(
		summary = "Listar envíos",
		description = "Obtiene todos los envíos registrados.",
		tags = { "Envíos" }
	)
	@ApiResponse(responseCode = "200", description = "Listado de envíos",
		content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnvioResponseDto.class))))
	@GetMapping("/shipments")
	public List<EnvioResponseDto> listShipments() {
		return logisticsService.findAllEnvios();
	}

	@Operation(
		summary = "Obtener envío por ID",
		description = "Consulta un envío específico por su identificador.",
		tags = { "Envíos" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Envío encontrado",
			content = @Content(schema = @Schema(implementation = EnvioResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "Envío no encontrado")
	})
	@GetMapping("/shipments/{id}")
	public EnvioResponseDto getShipmentById(
		@Parameter(description = "Identificador del envío", example = "1") @PathVariable Long id
	) {
		return logisticsService.findEnvioById(id);
	}

	@Operation(
		summary = "Actualizar envío",
		description = "Reemplaza los datos de un envío existente, incluido su estado.",
		tags = { "Envíos" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Envío actualizado",
			content = @Content(schema = @Schema(implementation = EnvioResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "404", description = "Envío no encontrado")
	})
	@PutMapping("/shipments/{id}")
	public EnvioResponseDto updateShipment(
		@Parameter(description = "Identificador del envío", example = "1") @PathVariable Long id,
		@Valid @RequestBody EnvioRequestDto request
	) {
		return logisticsService.updateEnvio(id, request);
	}

	@Operation(
		summary = "Eliminar envío",
		description = "Elimina un envío existente por su identificador.",
		tags = { "Envíos" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Envío eliminado"),
		@ApiResponse(responseCode = "404", description = "Envío no encontrado")
	})
	@DeleteMapping("/shipments/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteShipment(
		@Parameter(description = "Identificador del envío", example = "1") @PathVariable Long id
	) {
		logisticsService.deleteEnvioById(id);
	}
}
