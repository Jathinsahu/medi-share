package com.medshare.repository;

import com.medshare.model.ChatMessage;
import com.medshare.model.DonationRequest;
import com.medshare.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.donationRequest.requestId = :requestId ORDER BY cm.createdAt ASC")
    List<ChatMessage> findByDonationRequestIdOrderByCreatedAtAsc(@Param("requestId") UUID requestId);
    
    Page<ChatMessage> findByDonationRequest_RequestIdOrderByCreatedAtAsc(UUID requestId, Pageable pageable);
    
    List<ChatMessage> findByDonationRequestAndSender(DonationRequest donationRequest, User sender);
    
    List<ChatMessage> findByDonationRequestAndRecipient(DonationRequest donationRequest, User recipient);
}