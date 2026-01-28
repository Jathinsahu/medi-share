package com.medshare.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "donations_completed")
public class DonationCompleted {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "donation_id")
    private UUID donationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    private User donor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "estimated_value", precision = 10, scale = 2)
    private BigDecimal estimatedValue; // monetary value saved

    @Column(name = "feedback_rating")
    private Integer feedbackRating; // 1-5

    @Column(name = "feedback_text", columnDefinition = "TEXT")
    private String feedbackText;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // Constructors
    public DonationCompleted() {}

    public DonationCompleted(Medicine medicine, User donor, User receiver, BigDecimal estimatedValue) {
        this.medicine = medicine;
        this.donor = donor;
        this.receiver = receiver;
        this.estimatedValue = estimatedValue;
        this.completedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getDonationId() {
        return donationId;
    }

    public void setDonationId(UUID donationId) {
        this.donationId = donationId;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public User getDonor() {
        return donor;
    }

    public void setDonor(User donor) {
        this.donor = donor;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(BigDecimal estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public Integer getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(Integer feedbackRating) {
        this.feedbackRating = feedbackRating;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}