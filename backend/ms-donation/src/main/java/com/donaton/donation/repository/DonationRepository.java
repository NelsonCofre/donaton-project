package com.donaton.donation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.donaton.donation.model.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}
