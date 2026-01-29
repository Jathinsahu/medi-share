package com.medshare.service;

import com.medshare.dto.MessageDto;
import com.medshare.model.Message;
import com.medshare.model.Medicine;
import com.medshare.model.User;
import com.medshare.repository.MessageRepository;
import com.medshare.repository.MedicineRepository;
import com.medshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MedicineRepository medicineRepository;
    
    public MessageDto sendMessage(String subject, String content, UUID senderId, UUID recipientId, UUID medicineId) {
        // Validate users exist
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(recipientId)
            .orElseThrow(() -> new RuntimeException("Recipient not found"));
        
        // Create message
        Message message = new Message(subject, content, sender, recipient);
        
        // Set related medicine if provided
        if (medicineId != null) {
            Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
            message.setRelatedMedicine(medicine);
        }
        
        // Save message
        Message savedMessage = messageRepository.save(message);
        
        return convertToDto(savedMessage);
    }
    
    public Page<MessageDto> getUserMessages(UUID userId, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Page<Message> messages = messageRepository.findAllByUser(user, pageable);
        
        List<MessageDto> messageDtos = messages.getContent().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        
        return new PageImpl<>(messageDtos, pageable, messages.getTotalElements());
    }
    
    public Page<MessageDto> getReceivedMessages(UUID userId, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Page<Message> messages = messageRepository.findByRecipientOrderByCreatedAtDesc(user, pageable);
        
        List<MessageDto> messageDtos = messages.getContent().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        
        return new PageImpl<>(messageDtos, pageable, messages.getTotalElements());
    }
    
    public Page<MessageDto> getSentMessages(UUID userId, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Page<Message> messages = messageRepository.findBySenderOrderByCreatedAtDesc(user, pageable);
        
        List<MessageDto> messageDtos = messages.getContent().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        
        return new PageImpl<>(messageDtos, pageable, messages.getTotalElements());
    }
    
    public MessageDto getMessageById(UUID messageId, UUID userId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found"));
        
        // Check if user is authorized to view this message
        if (!message.getSender().getUserId().equals(userId) && 
            !message.getRecipient().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to view this message");
        }
        
        // Mark as read if recipient is viewing
        if (message.getRecipient().getUserId().equals(userId) && !message.getReadStatus()) {
            message.setReadStatus(true);
            messageRepository.save(message);
        }
        
        return convertToDto(message);
    }
    
    public Long getUnreadMessageCount(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        return messageRepository.countByRecipientAndReadStatus(user, false);
    }
    
    public Page<MessageDto> getMessagesForMedicine(UUID medicineId, Pageable pageable) {
        List<Message> messages = messageRepository.findByRelatedMedicine_MedicineId(medicineId);
        
        List<MessageDto> messageDtos = messages.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        
        return new PageImpl<>(messageDtos, pageable, messageDtos.size());
    }
    
    private MessageDto convertToDto(Message message) {
        return new MessageDto(
            message.getMessageId(),
            message.getSubject(),
            message.getContent(),
            message.getCreatedAt(),
            message.getReadStatus(),
            message.getSender().getUserId(),
            message.getSender().getFullName(),
            message.getRecipient().getUserId(),
            message.getRecipient().getFullName(),
            message.getRelatedMedicine() != null ? message.getRelatedMedicine().getMedicineId() : null,
            message.getRelatedMedicine() != null ? message.getRelatedMedicine().getMedicineName() : null
        );
    }
}