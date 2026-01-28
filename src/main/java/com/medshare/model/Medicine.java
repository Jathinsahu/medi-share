package com.medshare.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "medicines")
public class Medicine {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "medicine_id")
    private UUID medicineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    private User donor;

    @Column(name = "medicine_name", nullable = false)
    private String medicineName;

    @Column(name = "generic_name")
    private String genericName;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit")
    private String unit; // tablets, ml, strips

    @Column(name = "category")
    private String category; // antibiotic, painkiller, etc.

    @Column(name = "storage_condition")
    private String storageCondition;

    @Column(name = "prescription_required")
    private Boolean prescriptionRequired;

    @ElementCollection
    @Column(name = "image_url")
    @CollectionTable(name = "medicine_images", joinColumns = @JoinColumn(name = "medicine_id"))
    private List<String> imageUrls; // array of S3 URLs

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status; // available, reserved, donated, expired, rejected

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private VerificationStatus verificationStatus; // pending, approved, rejected

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Status {
        available, reserved, donated, expired, rejected
    }

    public enum VerificationStatus {
        pending, approved, rejected
    }

    // Constructors
    public Medicine() {}

    public Medicine(String medicineName, String genericName, String manufacturer, String batchNumber,
                    LocalDate expiryDate, Integer quantity, String unit, String category,
                    String storageCondition, Boolean prescriptionRequired, User donor) {
        this.medicineName = medicineName;
        this.genericName = genericName;
        this.manufacturer = manufacturer;
        this.batchNumber = batchNumber;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.unit = unit;
        this.category = category;
        this.storageCondition = storageCondition;
        this.prescriptionRequired = prescriptionRequired;
        this.donor = donor;
        this.status = Status.available;
        this.verificationStatus = VerificationStatus.pending;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(UUID medicineId) {
        this.medicineId = medicineId;
    }

    public User getDonor() {
        return donor;
    }

    public void setDonor(User donor) {
        this.donor = donor;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStorageCondition() {
        return storageCondition;
    }

    public void setStorageCondition(String storageCondition) {
        this.storageCondition = storageCondition;
    }

    public Boolean getPrescriptionRequired() {
        return prescriptionRequired;
    }

    public void setPrescriptionRequired(Boolean prescriptionRequired) {
        this.prescriptionRequired = prescriptionRequired;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}