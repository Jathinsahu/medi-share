package com.medshare.controller;

import com.medshare.config.UserDetailsImpl;
import com.medshare.dto.ChatMessageDto;
import com.medshare.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    
    @Autowired
    private ChatService chatService;
    
    // Send a new chat message
    @PostMapping("/send")
    public ResponseEntity<ChatMessageDto> sendMessage(
            @RequestBody Map<String, Object> messageRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        String content = (String) messageRequest.get("content");
        UUID recipientId = UUID.fromString((String) messageRequest.get("recipientId"));
        UUID requestId = UUID.fromString((String) messageRequest.get("requestId"));
        
        ChatMessageDto message = chatService.sendMessage(content, userDetails.getId(), recipientId, requestId);
        
        return ResponseEntity.ok(message);
    }
    
    // Get chat history for a specific request
    @GetMapping("/history/{requestId}")
    public ResponseEntity<Page<ChatMessageDto>> getChatHistory(
            @PathVariable UUID requestId,
            Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        // TODO: Add authorization check to ensure user is involved in the request
        
        Page<ChatMessageDto> messages = chatService.getChatHistory(requestId, pageable);
        return ResponseEntity.ok(messages);
    }
    
    // Get chat history for a specific request (non-paginated)
    @GetMapping("/history/{requestId}/all")
    public ResponseEntity<Iterable<ChatMessageDto>> getAllChatHistory(
            @PathVariable UUID requestId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        // TODO: Add authorization check to ensure user is involved in the request
        
        var messages = chatService.getChatHistory(requestId);
        return ResponseEntity.ok(messages);
    }
    
    // Get user's conversations
    @GetMapping("/conversations")
    public ResponseEntity<List<Map<String, Object>>> getUserConversations(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        List<Map<String, Object>> conversations = chatService.getUserConversations(userDetails.getId());
        return ResponseEntity.ok(conversations);
    }

}