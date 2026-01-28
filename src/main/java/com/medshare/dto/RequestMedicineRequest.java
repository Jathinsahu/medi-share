package com.medshare.dto;

import com.medshare.model.DonationRequest;

public class RequestMedicineRequest {
    private String reason;
    private DonationRequest.UrgencyLevel urgency;

    // Constructors
    public RequestMedicineRequest() {}

    public RequestMedicineRequest(String reason, DonationRequest.UrgencyLevel urgency) {
        this.reason = reason;
        this.urgency = urgency;
    }

    // Getters and Setters
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
}