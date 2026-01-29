package com.medshare.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessageDto {
    private UUID messageId;
    private String subject;
    private String content;
    private LocalDateTime createdAt;
    private Boolean readStatus;
    private UUID senderId;
    private String senderName;
    private UUID recipientId;
    private String recipientName;
    private UUID relatedMedicineId;
    private String relatedMedicineName;

    // Constructors
    public MessageDto() {}

    public MessageDto(UUID messageId, String subject, String content, LocalDateTime createdAt, 
                     Boolean readStatus, UUID senderId, String senderName, UUID recipientId, 
                     String recipientName, UUID relatedMedicineId, String relatedMedicineName) {
        this.messageId = messageId;
        this.subject = subject;
        this.content = content;
        this.createdAt = createdAt;
        this.readStatus = readStatus;
        this.senderId = senderId;
        this.senderName = senderName;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.relatedMedicineId = relatedMedicineId;
        this.relatedMedicineName = relatedMedicineName;
    }

    // Getters and Setters
    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Boolean readStatus) {
        this.readStatus = readStatus;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public UUID getRelatedMedicineId() {
        return relatedMedicineId;
    }

    public void setRelatedMedicineId(UUID relatedMedicineId) {
        this.relatedMedicineId = relatedMedicineId;
    }

    public String getRelatedMedicineName() {
        return relatedMedicineName;
    }

    public void setRelatedMedicineName(String relatedMedicineName) {
        this.relatedMedicineName = relatedMedicineName;
    }
}