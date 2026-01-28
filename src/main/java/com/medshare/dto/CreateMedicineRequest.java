package com.medshare.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public class CreateMedicineRequest {
    
    @NotBlank(message = "Medicine name is required")
    private String medicineName;
    
    private String genericName;
    
    private String manufacturer;
    
    private String batchNumber;
    
    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    private String unit; // tablets, ml, strips
    
    private String category; // antibiotic, painkiller, etc.
    
    private String storageCondition;
    
    private Boolean prescriptionRequired;
    
    private List<String> imageUrls; // array of S3 URLs

    // Constructors
    public CreateMedicineRequest() {}

    public CreateMedicineRequest(String medicineName, String genericName, String manufacturer, 
                                String batchNumber, LocalDate expiryDate, Integer quantity, String unit, 
                                String category, String storageCondition, Boolean prescriptionRequired, 
                                List<String> imageUrls) {
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
    }

    // Getters and Setters
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
}