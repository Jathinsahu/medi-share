package com.medshare.service;

import com.medshare.config.UserDetailsImpl;
import com.medshare.dto.UserDto;
import com.medshare.exception.ServiceException;
import com.medshare.model.User;
import com.medshare.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto createUser(String email, String rawPassword, String fullName, String phone, User.UserType userType) {
        logger.info("UserService.createUser() called");
        logger.info("Parameters - Email: {}, FullName: {}, Phone: {}, UserType: {}", 
            email, fullName, phone, userType);
        logger.info("Raw password length: {}", rawPassword != null ? rawPassword.length() : 0);
        
        try {
            logger.info("Checking if user with email {} already exists...", email);
            if (userRepository.existsByEmail(email)) {
                logger.error("User with email {} already exists!", email);
                throw new ServiceException("User with email already exists: " + email);
            }
            logger.info("Email {} is available for registration", email);

            logger.info("Encoding password...");
            String encodedPassword = passwordEncoder.encode(rawPassword);
            logger.info("Password encoded successfully, length: {}", encodedPassword.length());

            logger.info("Creating new User object...");
            User user = new User(email, encodedPassword, fullName, phone, userType);
            logger.info("User object created: Email={}, FullName={}, UserType={}", 
                user.getEmail(), user.getFullName(), user.getUserType());

            logger.info("Saving user to database...");
            User savedUser = userRepository.save(user);
            logger.info("User saved successfully! Generated ID: {}", savedUser.getUserId());

            logger.info("Converting User to UserDto...");
            UserDto userDto = convertToDto(savedUser);
            logger.info("Conversion successful! Returning UserDto with ID: {}", userDto.getUserId());
            
            return userDto;
        } catch (Exception e) {
            logger.error("=== USER CREATION FAILED ===");
            logger.error("Exception type: {}", e.getClass().getSimpleName());
            logger.error("Exception message: {}", e.getMessage());
            logger.error("Stack trace:", e);
            throw e;
        }
    }

    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDto);
    }

    public Optional<UserDto> findById(UUID userId) {
        return userRepository.findById(userId)
                .map(this::convertToDto);
    }

    public UserDto convertToDto(User user) {
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

    public User findByIdOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return UserDetailsImpl.build(user);
    }
}