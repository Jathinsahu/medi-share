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

import java.util.*;

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
    
    public List<Map<String, Object>> getUserConversations(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Get all donation requests where user is either requester or donor
        List<DonationRequest> userRequestsAsRequester = donationRequestRepository.findByRequesterOrderByCreatedAtDesc(user);
        List<DonationRequest> userRequestsAsDonor = donationRequestRepository.findByMedicine_DonorOrderByCreatedAtDesc(user);
        
        // Combine and deduplicate
        Set<DonationRequest> allRequests = new HashSet<>();
        allRequests.addAll(userRequestsAsRequester);
        allRequests.addAll(userRequestsAsDonor);
        
        List<Map<String, Object>> conversations = new ArrayList<>();
        
        for (DonationRequest request : allRequests) {
            Map<String, Object> convo = new HashMap<>();
            convo.put("requestId", request.getRequestId());
            
            // Determine the other participant
            User otherParticipant = request.getRequester().getUserId().equals(userId) 
                ? request.getMedicine().getDonor() 
                : request.getRequester();
            
            convo.put("participantId", otherParticipant.getUserId());
            convo.put("participantName", otherParticipant.getFullName());
            convo.put("medicineName", request.getMedicine().getMedicineName());
            
            // Get last message
            List<ChatMessage> messages = chatMessageRepository.findByDonationRequestIdOrderByCreatedAtAsc(request.getRequestId());
            if (!messages.isEmpty()) {
                ChatMessage lastMessage = messages.get(messages.size() - 1);
                Map<String, Object> lastMsg = new HashMap<>();
                lastMsg.put("content", lastMessage.getContent());
                lastMsg.put("createdAt", lastMessage.getCreatedAt());
                lastMsg.put("senderId", lastMessage.getSender().getUserId());
                convo.put("lastMessage", lastMsg);
                
                // Count unread messages (messages from other person that aren't from current user)
                long unreadCount = messages.stream()
                    .filter(msg -> !msg.getSender().getUserId().equals(userId) && msg.getRecipient().getUserId().equals(userId))
                    .count();
                convo.put("unreadCount", unreadCount);
            }
            
            conversations.add(convo);
        }
        
        return conversations;
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