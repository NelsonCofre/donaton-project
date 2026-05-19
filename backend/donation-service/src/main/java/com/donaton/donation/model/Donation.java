package com.donaton.donation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "donations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "resource_name", nullable = false)
	private String resourceName;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private String origin;

	@Column(name = "donation_date", nullable = false)
	private LocalDate donationDate;

	@Column(name = "warehouse_name", nullable = false)
	private String warehouseName;
}
