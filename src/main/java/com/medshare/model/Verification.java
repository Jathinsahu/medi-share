package com.medshare.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verifications")
public class Verification {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "verification_id")
    private UUID verificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verifier_id")
    private User verifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_type")
    private VerificationType verificationType; // photo, document, physical

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VerificationStatus status; // pending, approved, rejected

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    public enum VerificationType {
        photo, document, physical
    }

    public enum VerificationStatus {
        pending, approved, rejected
    }

    // Constructors
    public Verification() {}

    public Verification(Medicine medicine, User verifier, VerificationType verificationType, String notes) {
        this.medicine = medicine;
        this.verifier = verifier;
        this.verificationType = verificationType;
        this.notes = notes;
        this.status = VerificationStatus.pending;
    }

    // Getters and Setters
    public UUID getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(UUID verificationId) {
        this.verificationId = verificationId;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public User getVerifier() {
        return verifier;
    }

    public void setVerifier(User verifier) {
        this.verifier = verifier;
    }

    public VerificationType getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(VerificationType verificationType) {
        this.verificationType = verificationType;
    }

    public VerificationStatus getStatus() {
        return status;
    }

    public void setStatus(VerificationStatus status) {
        this.status = status;
        this.verifiedAt = LocalDateTime.now();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
}