package com.carrental.smartcar.controller;

import com.carrental.smartcar.dto.*;
import com.carrental.smartcar.model.User;
import com.carrental.smartcar.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class UserController {

    @GetMapping("/test")
    public String test(){
        return "Test working";
    }

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody User user) {
        try {
            return userService.registerUser(user);
        } catch (MessagingException e) {
            throw new RuntimeException("Registration successful, but email couldn't be sent. Please try again later.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequest loginRequest) {
        return userService.loginUser(loginRequest);
    }

    @GetMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        boolean isVerified = userService.verifyAccount(token);

        if (isVerified) {
            return ResponseEntity.ok("Account successfully verified.");
        } else {
            return ResponseEntity.status(400).body("Invalid or expired token.");
        }
    }

    @PutMapping("/edit-user/{userId}")
    public ResponseEntity<?> updateProfile(
            @PathVariable UUID userId,
            @ModelAttribute UpdateProfileRequest request,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        return userService.updateUserProfile(userId, request, imageFile);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(
            @ModelAttribute UpdateProfileRequest request,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        return userService.updateLoggedInUserProfile(request, imageFile);
    }

    @GetMapping("/users")
    public ResponseEntity<UsersResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        return userService.deleteUser(userId);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getMyProfile() {
        return userService.getLoggedInUserProfile();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        return userService.forgotPassword(email);
    }

}