package com.medshare.controller;

import com.medshare.config.UserDetailsImpl;
import com.medshare.dto.MessageDto;
import com.medshare.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    // Send a new message
    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(
            @Valid @RequestBody Map<String, Object> messageRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        String subject = (String) messageRequest.get("subject");
        String content = (String) messageRequest.get("content");
        UUID recipientId = UUID.fromString((String) messageRequest.get("recipientId"));
        UUID medicineId = messageRequest.containsKey("medicineId") ? 
            UUID.fromString((String) messageRequest.get("medicineId")) : null;
        
        MessageDto message = messageService.sendMessage(
            subject, content, userDetails.getId(), recipientId, medicineId);
        
        return ResponseEntity.ok(message);
    }
    
    // Get all messages for current user
    @GetMapping
    public ResponseEntity<Page<MessageDto>> getUserMessages(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable) {
        
        Page<MessageDto> messages = messageService.getUserMessages(userDetails.getId(), pageable);
        return ResponseEntity.ok(messages);
    }
    
    // Get received messages
    @GetMapping("/received")
    public ResponseEntity<Page<MessageDto>> getReceivedMessages(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable) {
        
        Page<MessageDto> messages = messageService.getReceivedMessages(userDetails.getId(), pageable);
        return ResponseEntity.ok(messages);
    }
    
    // Get sent messages
    @GetMapping("/sent")
    public ResponseEntity<Page<MessageDto>> getSentMessages(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable) {
        
        Page<MessageDto> messages = messageService.getSentMessages(userDetails.getId(), pageable);
        return ResponseEntity.ok(messages);
    }
    
    // Get message by ID
    @GetMapping("/{messageId}")
    public ResponseEntity<MessageDto> getMessageById(
            @PathVariable UUID messageId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        MessageDto message = messageService.getMessageById(messageId, userDetails.getId());
        return ResponseEntity.ok(message);
    }
    
    // Get unread message count
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadMessageCount(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        Long count = messageService.getUnreadMessageCount(userDetails.getId());
        Map<String, Long> response = new HashMap<>();
        response.put("unreadCount", count);
        return ResponseEntity.ok(response);
    }
    
    // Get messages for a specific medicine
    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<Page<MessageDto>> getMessagesForMedicine(
            @PathVariable UUID medicineId,
            Pageable pageable) {
        
        // This endpoint might need additional security checks
        Page<MessageDto> messages = messageService.getMessagesForMedicine(medicineId, pageable);
        return ResponseEntity.ok(messages);
    }
}