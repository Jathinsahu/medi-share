package com.medshare.repository;

import com.medshare.model.Message;
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
public interface MessageRepository extends JpaRepository<Message, UUID> {
    
    Page<Message> findByRecipientOrderByCreatedAtDesc(User recipient, Pageable pageable);
    
    Page<Message> findBySenderOrderByCreatedAtDesc(User sender, Pageable pageable);
    
    @Query("SELECT m FROM Message m WHERE m.recipient = :user OR m.sender = :user ORDER BY m.createdAt DESC")
    Page<Message> findAllByUser(@Param("user") User user, Pageable pageable);
    
    Long countByRecipientAndReadStatus(User recipient, Boolean readStatus);
    
    List<Message> findByRelatedMedicine_MedicineId(UUID medicineId);
}