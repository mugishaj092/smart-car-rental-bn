package com.carrental.smartcar.controller;

import com.carrental.smartcar.dto.*;
import com.carrental.smartcar.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @PostMapping
    public ResponseEntity<?> createRental(@RequestBody RentalRequestDTO request) {
        try {
            RentalCreationResponse response = rentalService.createRental(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalSingleResponseDTO<RentalResponseDTO>> getRentalById(@PathVariable UUID id) {
        try {
            RentalResponseDTO rental = rentalService.getRentalById(id);
            RentalSingleResponseDTO<RentalResponseDTO> response =
                    new RentalSingleResponseDTO<>("Rental retrieved successfully", "success", rental);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            RentalSingleResponseDTO<RentalResponseDTO> errorResponse =
                    new RentalSingleResponseDTO<>(e.getMessage(), "fail", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


    @GetMapping
    public RentalListResponseDTO<RentalResponseDTO> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/me")
    public RentalListResponseDTO<RentalResponseDTO> getMyRentals() throws Exception {
        return rentalService.getRentalsForLoggedInUser();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RentalStatusUpdateResponse> updateRentalStatus(
            @PathVariable UUID id,
            @RequestBody RentalStatusUpdateRequest request
    ) {
        try {
            RentalResponseDTO updatedRental = rentalService.updateRentalStatus(id, request.getStatus());
            return ResponseEntity.ok(new RentalStatusUpdateResponse("Rental status updated successfully", "success", updatedRental));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RentalStatusUpdateResponse(e.getMessage(), "fail", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RentalDeleteResponse> deleteRental(@PathVariable UUID id) {
        try {
            rentalService.deleteRental(id);
            return ResponseEntity.ok(new RentalDeleteResponse("Rental deleted successfully", "success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RentalDeleteResponse(e.getMessage(), "fail"));
        }
    }


}
