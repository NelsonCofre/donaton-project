package com.donaton.necessity.repository;

import com.donaton.necessity.model.Necessity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NecessityRepository extends JpaRepository<Necessity, Long> {
}
