package com.foodbridge.controller;

import com.foodbridge.repository.DonationRepository;
import com.foodbridge.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final DonationRepository donations;
    private final UserRepository users;

    public StatsController(DonationRepository donations, UserRepository users) {
        this.donations = donations;
        this.users = users;
    }

    @GetMapping
    public Map<String, Object> stats() {
        long available = donations.countByStatus(com.foodbridge.model.Donation.Status.AVAILABLE);
        long claimed = donations.countByStatus(com.foodbridge.model.Donation.Status.CLAIMED);
        long completed = donations.countByStatus(com.foodbridge.model.Donation.Status.COMPLETED);

        return Map.of(
            "donorsAndUsers", users.count(),
            "availableDonations", available,
            "claimedDonations", claimed,
            "completedDonations", completed
        );
    }
}
