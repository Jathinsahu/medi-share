package com.medshare.service;

import com.medshare.dto.AuthRequest;
import com.medshare.dto.AuthResponse;
import com.medshare.dto.UserDto;
import com.medshare.exception.ServiceException;
import com.medshare.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse authenticate(AuthRequest authRequest) {
        logger.info("AuthService.authenticate() called with email: {}", authRequest.getEmail());
        
        try {
            logger.info("Attempting authentication with AuthenticationManager...");
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );
            logger.info("AuthenticationManager authentication successful!");

            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Security context updated with authentication");

            // Generate JWT token
            logger.info("Generating JWT token...");
            String jwt = jwtTokenService.generateToken(authRequest.getEmail());
            logger.info("JWT token generated successfully, length: {}", jwt.length());

            logger.info("Fetching user DTO by email: {}", authRequest.getEmail());
            Optional<UserDto> userDtoOpt = userService.findByEmail(authRequest.getEmail());
            if (userDtoOpt.isPresent()) {
                UserDto userDto = userDtoOpt.get();
                logger.info("User found! ID: {}, Name: {}, Type: {}", 
                    userDto.getUserId(), userDto.getFullName(), userDto.getUserType());
                logger.info("Returning AuthResponse with token, userId, and userType");
                // Convert UUID to Long for the response
                Long userId = userDto.getUserId() != null ? userDto.getUserId().getMostSignificantBits() : null;
                return new AuthResponse(jwt, "Authentication successful", userId, userDto.getUserType());
            } else {
                logger.error("User not found after authentication - this shouldn't happen!");
                throw new ServiceException("User not found after authentication");
            }
        } catch (Exception e) {
            logger.error("=== AUTHENTICATION FAILED ===");
            logger.error("Exception type: {}", e.getClass().getSimpleName());
            logger.error("Exception message: {}", e.getMessage());
            logger.error("Stack trace:", e);
            throw new ServiceException("Authentication failed: " + e.getMessage());
        }
    }

    public AuthResponse register(String email, String password, String fullName, String phone, User.UserType userType) {
        logger.info("AuthService.register() called");
        logger.info("Parameters - Email: {}, FullName: {}, Phone: {}, UserType: {}", 
            email, fullName, phone, userType);
        
        try {
            logger.info("Calling userService.createUser...");
            UserDto userDto = userService.createUser(email, password, fullName, phone, userType);
            logger.info("User created successfully! ID: {}, Name: {}", userDto.getUserId(), userDto.getFullName());
            
            logger.info("Generating JWT token for registered user...");
            String jwt = jwtTokenService.generateToken(email);
            logger.info("JWT token generated, length: {}", jwt.length());
            
            logger.info("Registration process completed successfully");
            // Convert UUID to Long for the response
            Long userId = userDto.getUserId() != null ? userDto.getUserId().getMostSignificantBits() : null;
            return new AuthResponse(jwt, "Registration successful", userId, userDto.getUserType());
        } catch (Exception e) {
            logger.error("=== REGISTRATION FAILED IN AUTH SERVICE ===");
            logger.error("Exception type: {}", e.getClass().getSimpleName());
            logger.error("Exception message: {}", e.getMessage());
            logger.error("Stack trace:", e);
            throw e;
        }
    }
}