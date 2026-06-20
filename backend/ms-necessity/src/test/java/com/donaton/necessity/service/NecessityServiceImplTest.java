package com.donaton.necessity.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.donaton.necessity.dto.NecessityRequestDto;
import com.donaton.necessity.dto.NecessityResponseDto;
import com.donaton.necessity.exception.ResourceNotFoundException;
import com.donaton.necessity.model.Necessity;
import com.donaton.necessity.repository.NecessityRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NecessityServiceImplTest {

	@Mock
	private NecessityRepository necessityRepository;

	@InjectMocks
	private NecessityServiceImpl necessityService;

	@Test
	void createPersistsAndReturnsResponse() {
		var request = sampleRequest();
		var saved = sampleEntity(1L);

		when(necessityRepository.save(any(Necessity.class))).thenReturn(saved);

		NecessityResponseDto response = necessityService.create(request);

		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getResourceName()).isEqualTo("Frazadas");
	}

	@Test
	void findAllMapsEntities() {
		when(necessityRepository.findAll()).thenReturn(List.of(sampleEntity(1L)));

		List<NecessityResponseDto> responses = necessityService.findAll();

		assertThat(responses).hasSize(1);
	}

	@Test
	void findByIdThrowsWhenMissing() {
		when(necessityRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> necessityService.findById(99L))
			.isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void updateModifiesExistingNecessity() {
		var existing = sampleEntity(2L);
		var request = NecessityRequestDto.builder()
			.resourceName("Agua")
			.quantity(80)
			.location("Valparaíso")
			.reportedDate(LocalDate.of(2026, 6, 2))
			.reportedBy("Municipalidad")
			.build();

		when(necessityRepository.findById(2L)).thenReturn(Optional.of(existing));
		when(necessityRepository.save(existing)).thenReturn(existing);

		NecessityResponseDto response = necessityService.update(2L, request);

		assertThat(response.getQuantity()).isEqualTo(80);
		assertThat(existing.getLocation()).isEqualTo("Valparaíso");
	}

	@Test
	void deleteByIdRemovesExistingNecessity() {
		when(necessityRepository.existsById(4L)).thenReturn(true);

		necessityService.deleteById(4L);

		verify(necessityRepository).deleteById(4L);
	}

	@Test
	void deleteByIdThrowsWhenMissing() {
		when(necessityRepository.existsById(5L)).thenReturn(false);

		assertThatThrownBy(() -> necessityService.deleteById(5L))
			.isInstanceOf(ResourceNotFoundException.class);

		verify(necessityRepository, never()).deleteById(5L);
	}

	private NecessityRequestDto sampleRequest() {
		return NecessityRequestDto.builder()
			.resourceName("Frazadas")
			.quantity(50)
			.location("Concepción")
			.reportedDate(LocalDate.of(2026, 5, 10))
			.reportedBy("ONG")
			.build();
	}

	private Necessity sampleEntity(Long id) {
		return Necessity.builder()
			.id(id)
			.resourceName("Frazadas")
			.quantity(50)
			.location("Concepción")
			.reportedDate(LocalDate.of(2026, 5, 10))
			.reportedBy("ONG")
			.build();
	}
}
