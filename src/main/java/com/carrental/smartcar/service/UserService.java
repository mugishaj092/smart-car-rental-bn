package com.carrental.smartcar.service;

import com.carrental.smartcar.dto.*;
import com.carrental.smartcar.exception.EmailAlreadyInUseException;
import com.carrental.smartcar.exception.InvalidCredentialsException;
import com.carrental.smartcar.exception.UsernameAlreadyTakenException;
import com.carrental.smartcar.model.User;
import com.carrental.smartcar.model.UserRole;
import com.carrental.smartcar.repository.UserRepository;
import com.carrental.smartcar.security.JwtProvider;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String secretKey = "c29tZXNlY3VyZXJhbmRvbWtleXN0cmluZzEyMzQ1Njc4";
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private EmailService emailService;
    @Autowired
    private Cloudinary cloudinary;



    // Register a new user (signup)
    public ResponseEntity<UserResponseDTO> registerUser(User user) throws MessagingException {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UsernameAlreadyTakenException("Username already taken");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EmailAlreadyInUseException("Email already in use");
        }

        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }
        // Hash the password
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Generate the verification token and link
        String token = jwtProvider.generateToken(savedUser.getUsername());
        String verificationLink = "http://localhost:8080/auth/verify-account?token=" + token;

        // Send registration email with verification link
        emailService.sendRegistrationEmail(savedUser.getEmail(), savedUser.getUsername(), verificationLink);

        // Success message
        String successMessage = "User successfully created. Please check your email to verify your account.";

        // Return response with user details and message
        UserResponseDTO response = new UserResponseDTO(successMessage, savedUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<LoginResponseDTO> loginUser(LoginRequest loginRequest) {
        User user = null;

        if (loginRequest.getUsername() != null) {
            user = userRepository.findByUsername(loginRequest.getUsername());
        }

        if (user == null && loginRequest.getEmail() != null) {
            user = userRepository.findByEmail(loginRequest.getEmail());
        }

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtProvider.generateToken(user.getUsername());
        user.setPassword(null);
        LoginResponseDTO responseDTO = new LoginResponseDTO("Login successful", token, user);
        return ResponseEntity.ok(responseDTO);
    }

    @Transactional
    public boolean verifyAccount(String token) {
        if (jwtProvider.validateToken(token)) {
            String username = jwtProvider.getUsernameFromToken(token);
            User user = userRepository.findByUsername(username);

            if (user != null && !user.isVerified()) {
                user.setVerified(true);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public ResponseEntity<?> updateUserProfile(UUID userId, UpdateProfileRequest request, MultipartFile imageFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                        ObjectUtils.asMap("folder", "smartcar/profiles"));
                String imageUrl = uploadResult.get("secure_url").toString();
                user.setProfileImageUrl(imageUrl);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload profile image");
            }
        }

        userRepository.save(user);
        return ResponseEntity.ok("Profile updated successfully");
    }

    public ResponseEntity<?> updateLoggedInUserProfile(UpdateProfileRequest request, MultipartFile imageFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        System.out.println("Current username is: " + currentUsername);
        User user = userRepository.findByUsername(currentUsername);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                        ObjectUtils.asMap("folder", "smartcar/profiles"));
                user.setProfileImageUrl(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Image upload failed: " + e.getMessage());
            }
        }

        userRepository.save(user);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Profile updated successfully");

        return ResponseEntity.ok(response);
    }

    // Get all users
    public ResponseEntity<UsersResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        UsersResponseDTO response = new UsersResponseDTO("Users fetched successfully", users);
        return ResponseEntity.ok(response);
    }




    // Delete user by ID
    public ResponseEntity<?> deleteUser(UUID userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        System.out.println("user"+userOpt);
        if (userOpt.isPresent()) {
            userRepository.delete(userOpt.get());
            return ResponseEntity.ok(Map.of("success", true, "message", "User deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "User not found"));
        }
    }

    public ResponseEntity<?> getLoggedInUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with this email does not exist.");
        }

        // Generate token and link
        String token = jwtProvider.generateToken(user.getUsername()); // Optionally, create a custom method for reset token
        String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;

        try {
            emailService.sendPasswordResetEmail(user.getEmail(), user.getUsername(), resetLink);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send reset email.");
        }

        return ResponseEntity.ok("Password reset link sent to your email.");
    }

    public ResponseEntity<?> resetPassword(String token, String newPassword) {
        if (!jwtProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }

        String username = jwtProvider.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully.");
    }

}
