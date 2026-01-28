package com.medshare.repository;

import com.medshare.model.Verification;
import com.medshare.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, UUID> {
    List<Verification> findByMedicine(Medicine medicine);
    
    List<Verification> findByStatus(Verification.VerificationStatus status);
}