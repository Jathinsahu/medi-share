package com.medshare.dto;

import com.medshare.model.Medicine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class MedicineDto {
    private UUID medicineId;
    private String medicineName;
    private String genericName;
    private String manufacturer;
    private String batchNumber;
    private LocalDate expiryDate;
    private Integer quantity;
    private String unit;
    private String category;
    private String storageCondition;
    private Boolean prescriptionRequired;
    private List<String> imageUrls;
    private Medicine.Status status;
    private Medicine.VerificationStatus verificationStatus;
    private UserDto donor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public MedicineDto() {}

    public MedicineDto(UUID medicineId, String medicineName, String genericName, String manufacturer,
                      String batchNumber, LocalDate expiryDate, Integer quantity, String unit, String category,
                      String storageCondition, Boolean prescriptionRequired, List<String> imageUrls,
                      Medicine.Status status, Medicine.VerificationStatus verificationStatus, UserDto donor,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.medicineId = medicineId;
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
        this.imageUrls = imageUrls;
        this.status = status;
        this.verificationStatus = verificationStatus;
        this.donor = donor;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public UUID getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(UUID medicineId) {
        this.medicineId = medicineId;
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

    public Medicine.Status getStatus() {
        return status;
    }

    public void setStatus(Medicine.Status status) {
        this.status = status;
    }

    public Medicine.VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(Medicine.VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public UserDto getDonor() {
        return donor;
    }

    public void setDonor(UserDto donor) {
        this.donor = donor;
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