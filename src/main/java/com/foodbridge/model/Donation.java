package com.foodbridge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String foodName;

    @Column(nullable = false)
    private Integer quantity;

    private String description;
    private String location;
    private String pickupTime;

    @Enumerated(EnumType.STRING)
    private Status status = Status.AVAILABLE;

    @ManyToOne(optional = false)
    private User donor;

    @ManyToOne
    private User claimedBy;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status { AVAILABLE, CLAIMED, COMPLETED }

    public Donation() {}

    public Long getId() { return id; }
    public String getFoodName() { return foodName; }
    public Integer getQuantity() { return quantity; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getPickupTime() { return pickupTime; }
    public Status getStatus() { return status; }
    public User getDonor() { return donor; }
    public User getClaimedBy() { return claimedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setFoodName(String foodName) { this.foodName = foodName; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setPickupTime(String pickupTime) { this.pickupTime = pickupTime; }
    public void setStatus(Status status) { this.status = status; }
    public void setDonor(User donor) { this.donor = donor; }
    public void setClaimedBy(User claimedBy) { this.claimedBy = claimedBy; }
}
