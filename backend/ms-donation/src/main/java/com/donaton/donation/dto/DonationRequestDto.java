package com.donaton.donation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para registrar o actualizar una donación")
public class DonationRequestDto {

	@NotBlank
	@Schema(description = "Nombre del recurso donado", example = "Alimentos no perecederos")
	private String resourceName;

	@NotNull
	@Min(1)
	@Schema(description = "Cantidad donada del recurso", example = "150", minimum = "1")
	private Integer quantity;

	@NotBlank
	@Schema(description = "Origen de la donación (donante individual, empresa u organización)", example = "Empresa Alimentos del Sur S.A.")
	private String origin;

	@NotNull
	@Schema(description = "Fecha en que se registró la donación", example = "2026-06-08")
	private LocalDate donationDate;

	@NotBlank
	@Schema(description = "Centro de acopio asignado para almacenar la donación", example = "Centro Norte Valparaíso")
	private String warehouseName;
}
