package com.medshare.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChatMessageDto {
    private UUID messageId;
    private String content;
    private LocalDateTime createdAt;
    private UUID senderId;
    private String senderName;
    private UUID recipientId;
    private String recipientName;
    private UUID donationRequestId;

    // Constructors
    public ChatMessageDto() {}

    public ChatMessageDto(UUID messageId, String content, LocalDateTime createdAt, 
                         UUID senderId, String senderName, UUID recipientId, String recipientName, 
                         UUID donationRequestId) {
        this.messageId = messageId;
        this.content = content;
        this.createdAt = createdAt;
        this.senderId = senderId;
        this.senderName = senderName;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.donationRequestId = donationRequestId;
    }

    // Getters and Setters
    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
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

    public UUID getDonationRequestId() {
        return donationRequestId;
    }

    public void setDonationRequestId(UUID donationRequestId) {
        this.donationRequestId = donationRequestId;
    }
}