package com.medshare.controller;

import com.medshare.dto.AuthRequest;
import com.medshare.dto.AuthResponse;
import com.medshare.model.User;
import com.medshare.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        logger.info("=== LOGIN REQUEST RECEIVED ===");
        logger.info("Email: {}", authRequest.getEmail());
        logger.info("Password length: {}", authRequest.getPassword() != null ? authRequest.getPassword().length() : 0);
        
        try {
            logger.info("Calling authService.authenticate...");
            AuthResponse response = authService.authenticate(authRequest);
            logger.info("Authentication successful!");
            logger.info("Generated token: {}...", response.getToken().substring(0, Math.min(20, response.getToken().length())));
            logger.info("User ID: {}", response.getUserId());
            logger.info("User Type: {}", response.getUserType());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("=== LOGIN FAILED ===");
            logger.error("Error details:", e);
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("******************************************************************");
        logger.info("=== REGISTER REQUEST RECEIVED at /register ===");
        logger.info("******************************************************************");
        logger.info("=== REGISTRATION REQUEST RECEIVED ===");
        logger.info("Email: {}", registerRequest.getEmail());
        logger.info("Full Name: {}", registerRequest.getFullName());
        logger.info("Phone: {}", registerRequest.getPhone());
        logger.info("User Type: {}", registerRequest.getUserType());
        logger.info("Password length: {}", registerRequest.getPassword() != null ? registerRequest.getPassword().length() : 0);
        
        try {
            logger.info("Calling authService.register...");
            logger.info("Parameters: email={}, fullName={}, phone={}, userType={}", 
                registerRequest.getEmail(), 
                registerRequest.getFullName(), 
                registerRequest.getPhone(), 
                registerRequest.getUserType());
            
            AuthResponse response = authService.register(
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getFullName(),
                registerRequest.getPhone(),
                registerRequest.getUserType()
            );
            
            logger.info("Registration successful!");
            logger.info("Generated token: {}...", response.getToken().substring(0, Math.min(20, response.getToken().length())));
            logger.info("User ID: {}", response.getUserId());
            logger.info("User Type: {}", response.getUserType());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("=== REGISTRATION FAILED ===");
            logger.error("Error details:", e);
            logger.error("Stack trace:", e);
            throw e;
        }
    }
    
    public static class RegisterRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;
        
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password should be at least 6 characters")
        private String password;
        
        @NotBlank(message = "Full name is required")
        private String fullName;
        
        private String phone;
        
        @NotNull(message = "User type is required")
        private User.UserType userType;
        
        // Getters and setters
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
        
        public String getFullName() {
            return fullName;
        }
        
        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
        
        public String getPhone() {
            return phone;
        }
        
        public void setPhone(String phone) {
            this.phone = phone;
        }
        
        public User.UserType getUserType() {
            return userType;
        }
        
        public void setUserType(User.UserType userType) {
            this.userType = userType;
        }
    }
}