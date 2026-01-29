package com.medshare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id")
    private UUID messageId;
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_request_id")
    private DonationRequest donationRequest;
    
    // Constructors
    public ChatMessage() {}
    
    public ChatMessage(String content, User sender, User recipient, DonationRequest donationRequest) {
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
        this.donationRequest = donationRequest;
        this.createdAt = LocalDateTime.now();
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
    
    public User getSender() {
        return sender;
    }
    
    public void setSender(User sender) {
        this.sender = sender;
    }
    
    public User getRecipient() {
        return recipient;
    }
    
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
    
    public DonationRequest getDonationRequest() {
        return donationRequest;
    }
    
    public void setDonationRequest(DonationRequest donationRequest) {
        this.donationRequest = donationRequest;
    }
}