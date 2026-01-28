package com.medshare.repository;

import com.medshare.model.DonationCompleted;
import com.medshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DonationCompletedRepository extends JpaRepository<DonationCompleted, UUID> {
    
    @Query("SELECT dc FROM DonationCompleted dc WHERE dc.donor = :donor")
    List<DonationCompleted> findByDonor(User donor);
    
    @Query("SELECT dc FROM DonationCompleted dc WHERE dc.receiver = :receiver")
    List<DonationCompleted> findByReceiver(User receiver);
    
    @Query("SELECT SUM(dc.estimatedValue) FROM DonationCompleted dc")
    Double sumEstimatedValue();
}