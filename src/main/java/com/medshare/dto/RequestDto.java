package com.medshare.dto;

import com.medshare.model.DonationRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public class RequestDto {
    private UUID requestId;
    private MedicineDto medicine;
    private UserDto requester;
    private DonationRequest.RequestStatus status;
    private String reason;
    private DonationRequest.UrgencyLevel urgency;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    // Constructors
    public RequestDto() {}

    public RequestDto(UUID requestId, MedicineDto medicine, UserDto requester, 
                     DonationRequest.RequestStatus status, String reason, 
                     DonationRequest.UrgencyLevel urgency, LocalDateTime createdAt, 
                     LocalDateTime completedAt) {
        this.requestId = requestId;
        this.medicine = medicine;
        this.requester = requester;
        this.status = status;
        this.reason = reason;
        this.urgency = urgency;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    // Getters and Setters
    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public MedicineDto getMedicine() {
        return medicine;
    }

    public void setMedicine(MedicineDto medicine) {
        this.medicine = medicine;
    }

    public UserDto getRequester() {
        return requester;
    }

    public void setRequester(UserDto requester) {
        this.requester = requester;
    }

    public DonationRequest.RequestStatus getStatus() {
        return status;
    }

    public void setStatus(DonationRequest.RequestStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DonationRequest.UrgencyLevel getUrgency() {
        return urgency;
    }

    public void setUrgency(DonationRequest.UrgencyLevel urgency) {
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