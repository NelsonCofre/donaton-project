package com.donaton.necessity.model;

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
@Table(name = "necessities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Necessity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "resource_name", nullable = false)
	private String resourceName;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private String location;

	@Column(name = "reported_date", nullable = false)
	private LocalDate reportedDate;

	@Column(name = "reported_by", nullable = false)
	private String reportedBy;
}
