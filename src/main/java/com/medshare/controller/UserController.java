package com.medshare.controller;

import com.medshare.dto.UserDto;
import com.medshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getCurrentUserProfile(@AuthenticationPrincipal UUID userId) {
        Optional<UserDto> userDto = userService.findById(userId);
        return userDto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable UUID userId) {
        Optional<UserDto> userDto = userService.findById(userId);
        return userDto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}