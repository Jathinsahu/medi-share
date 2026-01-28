package com.medshare.repository;

import com.medshare.model.Medicine;
import com.medshare.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, UUID> {
    
    @Query("SELECT m FROM Medicine m WHERE m.donor = :donor AND m.status = 'available'")
    List<Medicine> findByDonorAndAvailable(@Param("donor") User donor);
    
    @Query("SELECT m FROM Medicine m WHERE m.medicineName LIKE %:name% AND m.status = 'available' AND m.expiryDate >= :currentDate")
    Page<Medicine> findByMedicineNameContainingAndStatusAndExpiryDateAfter(
            @Param("name") String name, 
            @Param("currentDate") LocalDate currentDate, 
            Pageable pageable);
    
    @Query("SELECT m FROM Medicine m WHERE m.category = :category AND m.status = 'available' AND m.expiryDate >= :currentDate")
    Page<Medicine> findByCategoryAndStatusAndExpiryDateAfter(
            @Param("category") String category, 
            @Param("currentDate") LocalDate currentDate, 
            Pageable pageable);
    
    @Query("SELECT m FROM Medicine m WHERE m.status = 'available' AND m.expiryDate >= :currentDate")
    Page<Medicine> findByStatusAndExpiryDateAfter(
            @Param("currentDate") LocalDate currentDate, 
            Pageable pageable);
    
    @Query("SELECT COUNT(m) FROM Medicine m WHERE m.status = 'donated'")
    Long countDonatedMedicines();
    
    @Query("SELECT COUNT(m) FROM Medicine m WHERE m.status = 'available'")
    Long countAvailableMedicines();
}