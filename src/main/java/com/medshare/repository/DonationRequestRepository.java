package com.medshare.repository;

import com.medshare.model.DonationRequest;
import com.medshare.model.Medicine;
import com.medshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DonationRequestRepository extends JpaRepository<DonationRequest, UUID> {
    
    @Query("SELECT dr FROM DonationRequest dr WHERE dr.medicine = :medicine AND dr.status = 'pending'")
    List<DonationRequest> findByMedicineAndPendingStatus(@Param("medicine") Medicine medicine);
    
    @Query("SELECT dr FROM DonationRequest dr WHERE dr.requester = :requester AND dr.status = 'pending'")
    List<DonationRequest> findByRequesterAndPendingStatus(@Param("requester") User requester);
    
    @Query("SELECT dr FROM DonationRequest dr WHERE dr.medicine.donor = :donor AND dr.status = 'pending'")
    List<DonationRequest> findByDonorAndPendingStatus(@Param("donor") User donor);
    
    Optional<DonationRequest> findByMedicineAndRequester(Medicine medicine, User requester);
}