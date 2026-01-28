package com.medshare.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "donation_requests")
public class DonationRequest {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "request_id")
    private UUID requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status; // pending, accepted, rejected, completed

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason; // why they need it

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency")
    private UrgencyLevel urgency; // low, medium, high

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public enum RequestStatus {
        pending, accepted, rejected, completed
    }

    public enum UrgencyLevel {
        low, medium, high
    }

    // Constructors
    public DonationRequest() {}

    public DonationRequest(Medicine medicine, User requester, String reason, UrgencyLevel urgency) {
        this.medicine = medicine;
        this.requester = requester;
        this.reason = reason;
        this.urgency = urgency;
        this.status = RequestStatus.pending;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UrgencyLevel getUrgency() {
        return urgency;
    }

    public void setUrgency(UrgencyLevel urgency) {
        this.urgency = urgency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}