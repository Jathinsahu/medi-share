package com.medshare.service;

import com.medshare.dto.CreateMedicineRequest;
import com.medshare.dto.ImpactMetrics;
import com.medshare.dto.MedicineDto;
import com.medshare.dto.RequestMedicineRequest;
import com.medshare.dto.UserDto;
import com.medshare.exception.ServiceException;
import com.medshare.model.DonationRequest;
import com.medshare.model.Medicine;
import com.medshare.model.User;
import com.medshare.repository.DonationRequestRepository;
import com.medshare.repository.MedicineRepository;
import com.medshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public MedicineDto create(CreateMedicineRequest request, User user) {
        // Validate expiry date is in the future
        if (request.getExpiryDate().isBefore(LocalDate.now())) {
            throw new ServiceException("Expiry date must be in the future");
        }

        Medicine medicine = new Medicine(
                request.getMedicineName(),
                request.getGenericName(),
                request.getManufacturer(),
                request.getBatchNumber(),
                request.getExpiryDate(),
                request.getQuantity(),
                request.getUnit(),
                request.getCategory(),
                request.getStorageCondition(),
                request.getPrescriptionRequired(),
                user
        );

        medicine.setImageUrls(request.getImageUrls());
        Medicine savedMedicine = medicineRepository.save(medicine);

        return convertToDto(savedMedicine);
    }

    public Page<MedicineDto> search(String name, String category, Double lat, Double lng, Integer radiusKm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDate currentDate = LocalDate.now();

        Page<Medicine> medicines;

        if (name != null && !name.isEmpty()) {
            medicines = medicineRepository.findByMedicineNameContainingAndStatusAndExpiryDateAfter(name, currentDate, pageable);
        } else if (category != null && !category.isEmpty()) {
            medicines = medicineRepository.findByCategoryAndStatusAndExpiryDateAfter(category, currentDate, pageable);
        } else {
            medicines = medicineRepository.findByStatusAndExpiryDateAfter(currentDate, pageable);
        }

        return medicines.map(this::convertToDto);
    }

    public Page<MedicineDto> findAllAvailable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDate currentDate = LocalDate.now();

        Page<Medicine> medicines = medicineRepository.findByStatusAndExpiryDateAfter(currentDate, pageable);
        return medicines.map(this::convertToDto);
    }

    public List<MedicineDto> findByDonor(UUID donorId) {
        User donor = userService.findByIdOrThrow(donorId);
        List<Medicine> medicines = medicineRepository.findByDonorAndAvailable(donor);
        return medicines.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<MedicineDto> findById(UUID medicineId) {
        return medicineRepository.findById(medicineId)
                .map(this::convertToDto);
    }

    public DonationRequest createRequest(UUID medicineId, User user, RequestMedicineRequest request) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ServiceException("Medicine not found with id: " + medicineId));

        // Check if already reserved
        Optional<DonationRequest> existingRequest = donationRequestRepository.findByMedicineAndRequester(medicine, user);
        if (existingRequest.isPresent() && existingRequest.get().getStatus() == DonationRequest.RequestStatus.pending) {
            throw new ServiceException("Request already exists for this medicine");
        }

        DonationRequest donationRequest = new DonationRequest(medicine, user, request.getReason(), request.getUrgency());
        return donationRequestRepository.save(donationRequest);
    }
    
    public DonationRequest updateRequestStatus(UUID requestId, User user, DonationRequest.RequestStatus newStatus) {
        DonationRequest request = donationRequestRepository.findById(requestId)
            .orElseThrow(() -> new ServiceException("Request not found"));
        
        // Check if user is authorized to update status
        // Either the donor (owner of medicine) or the requester can update certain statuses
        boolean isDonor = request.getMedicine().getDonor().getUserId().equals(user.getUserId());
        boolean isRequester = request.getRequester().getUserId().equals(user.getUserId());
        
        if (!isDonor && !isRequester) {
            throw new ServiceException("You are not authorized to update this request");
        }
        
        // Update status
        request.setStatus(newStatus);
        
        // Update medicine status based on request status
        Medicine medicine = request.getMedicine();
        if (newStatus == DonationRequest.RequestStatus.accepted) {
            medicine.setStatus(Medicine.Status.reserved);
        } else if (newStatus == DonationRequest.RequestStatus.completed) {
            medicine.setStatus(Medicine.Status.donated);
        } else if (newStatus == DonationRequest.RequestStatus.rejected) {
            // If request is rejected, make medicine available again
            medicine.setStatus(Medicine.Status.available);
        }
        
        medicineRepository.save(medicine);
        return donationRequestRepository.save(request);
    }
    
    public List<DonationRequest> getRequestsForUserAsDonor(UUID donorId) {
        User donor = userRepository.findById(donorId)
            .orElseThrow(() -> new ServiceException("Donor not found"));
        
        return donationRequestRepository.findByMedicine_DonorOrderByCreatedAtDesc(donor);
    }
    
    public List<DonationRequest> getRequestsForUserAsRequester(UUID requesterId) {
        User requester = userRepository.findById(requesterId)
            .orElseThrow(() -> new ServiceException("Requester not found"));
        
        return donationRequestRepository.findByRequesterOrderByCreatedAtDesc(requester);
    }
    
    public ImpactMetrics calculateImpact() {
        Long totalMedicinesListed = medicineRepository.count();
        Long totalMedicinesDonated = medicineRepository.countDonatedMedicines();
        Long totalMedicinesAvailable = medicineRepository.countAvailableMedicines();

        // Note: We'll need to enhance this to calculate actual value saved
        // For now, we'll use a placeholder calculation
        Double totalValueSaved = totalMedicinesDonated != null ? totalMedicinesDonated * 500.0 : 0.0; // Placeholder value
        Long totalPeopleHelped = totalMedicinesDonated; // Assuming each donation helps one person

        // Count unique donors and receivers
        // This would require additional queries to donation completed table
        Long totalDonors = 0L; // Placeholder
        Long totalReceivers = 0L; // Placeholder

        return new ImpactMetrics(
                totalMedicinesListed,
                totalMedicinesDonated,
                totalMedicinesAvailable,
                totalValueSaved,
                totalPeopleHelped,
                totalDonors,
                totalReceivers
        );
    }

    public MedicineDto convertToDto(Medicine medicine) {
        UserDto donorDto = convertUserToDto(medicine.getDonor());

        return new MedicineDto(
                medicine.getMedicineId(),
                medicine.getMedicineName(),
                medicine.getGenericName(),
                medicine.getManufacturer(),
                medicine.getBatchNumber(),
                medicine.getExpiryDate(),
                medicine.getQuantity(),
                medicine.getUnit(),
                medicine.getCategory(),
                medicine.getStorageCondition(),
                medicine.getPrescriptionRequired(),
                medicine.getImageUrls(),
                medicine.getStatus(),
                medicine.getVerificationStatus(),
                donorDto,
                medicine.getCreatedAt(),
                medicine.getUpdatedAt()
        );
    }

    private UserDto convertUserToDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone(),
                user.getUserType(),
                user.getLocationLat(),
                user.getLocationLng(),
                user.getAddress(),
                user.getVerified(),
                user.getCreatedAt(),
                user.getLastLogin()
        );
    }
}