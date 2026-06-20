package com.donaton.donation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Donación registrada en el sistema")
public class DonationResponseDto {

	@Schema(description = "Identificador único de la donación", example = "1")
	private Long id;

	@Schema(description = "Nombre del recurso donado", example = "Kits de higiene personal")
	private String resourceName;

	@Schema(description = "Cantidad donada del recurso", example = "350")
	private Integer quantity;

	@Schema(description = "Origen de la donación", example = "Donante individual - María González")
	private String origin;

	@Schema(description = "Fecha en que se registró la donación", example = "2026-06-08")
	private LocalDate donationDate;

	@Schema(description = "Centro de acopio asignado", example = "Centro Viña del Mar")
	private String warehouseName;
}
