package com.foodbridge.controller;

import com.foodbridge.model.Donation;
import com.foodbridge.model.User;
import com.foodbridge.repository.DonationRepository;
import com.foodbridge.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
public class DonationController {
    private final DonationRepository donations;
    private final UserRepository users;

    public DonationController(DonationRepository donations, UserRepository users) {
        this.donations = donations;
        this.users = users;
    }

    record DonationRequest(
        String foodName,
        Integer quantity,
        String description,
        String location,
        String pickupTime
    ) {}

    @GetMapping
    public List<Donation> getAvailable() {
        return donations.findByStatusOrderByCreatedAtDesc(Donation.Status.AVAILABLE);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DonationRequest request, HttpSession session) {
        Object id = session.getAttribute("userId");
        if (id == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Please login first"));
        }

        User donor = users.findById((Long) id).orElse(null);
        if (donor == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid session"));
        }

        Donation d = new Donation();
        d.setFoodName(request.foodName());
        d.setQuantity(request.quantity());
        d.setDescription(request.description());
        d.setLocation(request.location());
        d.setPickupTime(request.pickupTime());
        d.setDonor(donor);

        return ResponseEntity.ok(donations.save(d));
    }

    @PostMapping("/{id}/claim")
    public ResponseEntity<?> claim(@PathVariable Long id, HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Please login first"));
        }

        Donation d = donations.findById(id).orElse(null);
        User user = users.findById((Long) userId).orElse(null);

        if (d == null || user == null) {
            return ResponseEntity.notFound().build();
        }

        if (d.getStatus() != Donation.Status.AVAILABLE) {
            return ResponseEntity.badRequest().body(Map.of("message", "Donation is no longer available"));
        }

        d.setClaimedBy(user);
        d.setStatus(Donation.Status.CLAIMED);
        return ResponseEntity.ok(donations.save(d));
    }
}
