package com.foodbridge.repository;

import com.foodbridge.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByStatusOrderByCreatedAtDesc(Donation.Status status);
    long countByStatus(Donation.Status status);
}
