package com.donaton.donation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.donaton.donation.dto.DonationRequestDto;
import com.donaton.donation.dto.DonationResponseDto;
import com.donaton.donation.exception.ResourceNotFoundException;
import com.donaton.donation.model.Donation;
import com.donaton.donation.repository.DonationRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DonationServiceImplTest {

	@Mock
	private DonationRepository donationRepository;

	@InjectMocks
	private DonationServiceImpl donationService;

	@Test
	void createPersistsAndReturnsResponse() {
		var request = sampleRequest();
		var saved = sampleEntity(1L);

		when(donationRepository.save(any(Donation.class))).thenReturn(saved);

		DonationResponseDto response = donationService.create(request);

		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getResourceName()).isEqualTo("Agua");
		verify(donationRepository).save(any(Donation.class));
	}

	@Test
	void findAllMapsEntities() {
		when(donationRepository.findAll()).thenReturn(List.of(sampleEntity(1L), sampleEntity(2L)));

		List<DonationResponseDto> responses = donationService.findAll();

		assertThat(responses).hasSize(2);
		assertThat(responses.get(0).getId()).isEqualTo(1L);
	}

	@Test
	void findByIdReturnsDonationWhenExists() {
		when(donationRepository.findById(3L)).thenReturn(Optional.of(sampleEntity(3L)));

		DonationResponseDto response = donationService.findById(3L);

		assertThat(response.getQuantity()).isEqualTo(10);
	}

	@Test
	void findByIdThrowsWhenMissing() {
		when(donationRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> donationService.findById(99L))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("99");
	}

	@Test
	void updateModifiesExistingDonation() {
		var existing = sampleEntity(5L);
		var request = DonationRequestDto.builder()
			.resourceName("Frazadas")
			.quantity(20)
			.origin("ONG")
			.donationDate(LocalDate.of(2026, 6, 1))
			.warehouseName("Centro Sur")
			.build();

		when(donationRepository.findById(5L)).thenReturn(Optional.of(existing));
		when(donationRepository.save(existing)).thenReturn(existing);

		DonationResponseDto response = donationService.update(5L, request);

		assertThat(response.getResourceName()).isEqualTo("Frazadas");
		assertThat(existing.getQuantity()).isEqualTo(20);
	}

	@Test
	void deleteByIdRemovesExistingDonation() {
		when(donationRepository.existsById(7L)).thenReturn(true);

		donationService.deleteById(7L);

		verify(donationRepository).deleteById(7L);
	}

	@Test
	void deleteByIdThrowsWhenMissing() {
		when(donationRepository.existsById(8L)).thenReturn(false);

		assertThatThrownBy(() -> donationService.deleteById(8L))
			.isInstanceOf(ResourceNotFoundException.class);

		verify(donationRepository, never()).deleteById(8L);
	}

	private DonationRequestDto sampleRequest() {
		return DonationRequestDto.builder()
			.resourceName("Agua")
			.quantity(10)
			.origin("Empresa")
			.donationDate(LocalDate.of(2026, 5, 10))
			.warehouseName("Centro Norte")
			.build();
	}

	private Donation sampleEntity(Long id) {
		return Donation.builder()
			.id(id)
			.resourceName("Agua")
			.quantity(10)
			.origin("Empresa")
			.donationDate(LocalDate.of(2026, 5, 10))
			.warehouseName("Centro Norte")
			.build();
	}
}
