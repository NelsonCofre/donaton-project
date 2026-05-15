package com.donaton.donation.dto;

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
public class DonationRequestDto {

	@NotBlank
	private String resourceName;

	@NotNull
	@Min(1)
	private Integer quantity;

	@NotBlank
	private String origin;

	@NotNull
	private LocalDate donationDate;

	@NotBlank
	private String warehouseName;
}
