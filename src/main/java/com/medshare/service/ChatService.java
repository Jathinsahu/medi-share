package com.medshare.service;

import com.medshare.dto.ChatMessageDto;
import com.medshare.model.ChatMessage;
import com.medshare.model.DonationRequest;
import com.medshare.model.User;
import com.medshare.repository.ChatMessageRepository;
import com.medshare.repository.DonationRequestRepository;
import com.medshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatService {
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    @Autowired
    private DonationRequestRepository donationRequestRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public ChatMessageDto sendMessage(String content, UUID senderId, UUID recipientId, UUID requestId) {
        // Validate users exist
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(recipientId)
            .orElseThrow(() -> new RuntimeException("Recipient not found"));
        
        // Validate donation request exists
        DonationRequest donationRequest = donationRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Donation request not found"));
        
        // Create chat message
        ChatMessage chatMessage = new ChatMessage(content, sender, recipient, donationRequest);
        
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        
        return convertToDto(savedMessage);
    }
    
    public List<ChatMessageDto> getChatHistory(UUID requestId) {
        List<ChatMessage> messages = chatMessageRepository.findByDonationRequestIdOrderByCreatedAtAsc(requestId);
        
        return messages.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    public Page<ChatMessageDto> getChatHistory(UUID requestId, Pageable pageable) {
        Page<ChatMessage> messages = chatMessageRepository.findByDonationRequest_RequestIdOrderByCreatedAtAsc(requestId, pageable);
        
        List<ChatMessageDto> messageDtos = messages.getContent().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        
        return new PageImpl<>(messageDtos, pageable, messages.getTotalElements());
    }
    
    public ChatMessageDto convertToDto(ChatMessage message) {
        return new ChatMessageDto(
            message.getMessageId(),
            message.getContent(),
            message.getCreatedAt(),
            message.getSender().getUserId(),
            message.getSender().getFullName(),
            message.getRecipient().getUserId(),
            message.getRecipient().getFullName(),
            message.getDonationRequest().getRequestId()
        );
    }
}