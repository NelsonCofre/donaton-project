package com.donaton.donation.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationResponseDto {

	private Long id;
	private String resourceName;
	private Integer quantity;
	private String origin;
	private LocalDate donationDate;
	private String warehouseName;
}
