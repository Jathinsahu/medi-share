package com.medshare.controller;

import com.medshare.config.UserDetailsImpl;
import com.medshare.dto.CreateMedicineRequest;
import com.medshare.dto.ImpactMetrics;
import com.medshare.dto.MedicineDto;
import com.medshare.dto.RequestMedicineRequest;
import com.medshare.model.DonationRequest;
import com.medshare.model.User;
import com.medshare.service.MedicineService;
import com.medshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private UserService userService;

    // CREATE - Donor lists medicine
    @PostMapping
    public ResponseEntity<MedicineDto> createMedicine(
            @Valid @RequestBody CreateMedicineRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userService.findByIdOrThrow(userDetails.getId());
        // Validate expiry date is future (handled in service)
        // Check if image uploaded (would be validated in request)
        MedicineDto medicine = medicineService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(medicine);
    }

    // READ - Search medicines
    @GetMapping
    public ResponseEntity<Page<MedicineDto>> searchMedicines(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            @RequestParam(required = false) Integer radiusKm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        // Implement geo-spatial search (basic implementation here)
        // Filter by expiry > today
        // Sort by proximity + expiry date
        Page<MedicineDto> medicines = medicineService.search(name, category, lat, lng, radiusKm, page, size);
        return ResponseEntity.ok(medicines);
    }

    // READ - Get all available medicines
    @GetMapping("/available")
    public ResponseEntity<Page<MedicineDto>> getAllAvailableMedicines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<MedicineDto> medicines = medicineService.findAllAvailable(page, size);
        return ResponseEntity.ok(medicines);
    }

    // READ - Get medicine by ID
    @GetMapping("/{medicineId}")
    public ResponseEntity<MedicineDto> getMedicineById(@PathVariable UUID medicineId) {
        return medicineService.findById(medicineId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE - Reserve medicine
    @PostMapping("/{medicineId}/request")
    public ResponseEntity<?> requestMedicine(
            @PathVariable UUID medicineId,
            @Valid @RequestBody RequestMedicineRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userService.findByIdOrThrow(userDetails.getId());
        // Check if already reserved (handled in service)
        // Send notification to donor (would be implemented in service)
        // Create request record
        try {
            var donationRequest = medicineService.createRequest(medicineId, user, request);
            // Return a simplified response since we don't have a specific DTO for this
            return ResponseEntity.ok("Request created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // UPDATE - Update request status
    @PutMapping("/requests/{requestId}/status")
    public ResponseEntity<?> updateRequestStatus(
            @PathVariable UUID requestId,
            @RequestBody Map<String, String> statusRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            String statusStr = statusRequest.get("status");
            DonationRequest.RequestStatus newStatus = DonationRequest.RequestStatus.valueOf(statusStr.toLowerCase());
            
            User user = userService.findByIdOrThrow(userDetails.getId());
            var updatedRequest = medicineService.updateRequestStatus(requestId, user, newStatus);
            
            return ResponseEntity.ok("Request status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating request status: " + e.getMessage());
        }
    }
    
    // GET - Get requests for donor
    @GetMapping("/requests/my-requests")
    public ResponseEntity<?> getMyRequests(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            User user = userService.findByIdOrThrow(userDetails.getId());
            var requests = medicineService.getRequestsForUserAsRequester(user.getUserId());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching requests: " + e.getMessage());
        }
    }
    
    // GET - Get requests for donor
    @GetMapping("/requests/my-donations")
    public ResponseEntity<?> getMyDonationRequests(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            User user = userService.findByIdOrThrow(userDetails.getId());
            var requests = medicineService.getRequestsForUserAsDonor(user.getUserId());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching requests: " + e.getMessage());
        }
    }
    
    // Analytics endpoint
    @GetMapping("/impact")
    public ResponseEntity<ImpactMetrics> getImpactMetrics() {
        // Total medicines donated
        // Total value saved
        // Number of people helped
        // Category breakdown
        ImpactMetrics metrics = medicineService.calculateImpact();
        return ResponseEntity.ok(metrics);
    }

    // GET medicines by donor
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<?> getMedicinesByDonor(@PathVariable UUID donorId) {
        try {
            var medicines = medicineService.findByDonor(donorId);
            return ResponseEntity.ok(medicines);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}