package com.medshare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id")
    private UUID messageId;
    
    @Column(name = "subject", nullable = false)
    private String subject;
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "read_status", nullable = false)
    private Boolean readStatus = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_medicine_id")
    private Medicine relatedMedicine;
    
    // Constructors
    public Message() {}
    
    public Message(String subject, String content, User sender, User recipient) {
        this.subject = subject;
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
        this.createdAt = LocalDateTime.now();
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
    
    public Medicine getRelatedMedicine() {
        return relatedMedicine;
    }
    
    public void setRelatedMedicine(Medicine relatedMedicine) {
        this.relatedMedicine = relatedMedicine;
    }
}