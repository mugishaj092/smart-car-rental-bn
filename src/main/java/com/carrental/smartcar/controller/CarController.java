package com.carrental.smartcar.controller;

import com.carrental.smartcar.dto.CarRequestDTO;
import com.carrental.smartcar.dto.CarResponseDTO;
import com.carrental.smartcar.model.Car;
import com.carrental.smartcar.service.CarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/cars")
@Validated
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping
    public ResponseEntity<CarResponseDTO<Car>> addCar(@RequestBody Car car) {
        try {
            Car savedCar = carService.addCar(car);
            URI location = URI.create("/api/cars/" + savedCar.getId());

            CarResponseDTO<Car> response = new CarResponseDTO<>("success", "Car created successfully", savedCar);
            return ResponseEntity.created(location).body(response);

        } catch (Exception e) {
            CarResponseDTO<Car> errorResponse = new CarResponseDTO<>("fail", "Failed to create car: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{carId}/image")
    public ResponseEntity<CarResponseDTO<Car>> updateCarImage(
            @PathVariable UUID carId,
            @RequestParam("image") MultipartFile imageFile
    ) {
        try {
            Car updatedCar = carService.updateCarImage(carId, imageFile);
            CarResponseDTO<Car> response = new CarResponseDTO<>("success", "Car image updated successfully", updatedCar);
            return ResponseEntity.ok().body(response);
        } catch (ResponseStatusException e) {
            CarResponseDTO<Car> errorResponse = new CarResponseDTO<>("fail", "Failed to update car image: " + e.getReason(), null);
            return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
        } catch (Exception e) {
            CarResponseDTO<Car> errorResponse = new CarResponseDTO<>("fail", "Failed to update car image: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}
