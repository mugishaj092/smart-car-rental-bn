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
import java.util.List;
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

    @GetMapping
    public ResponseEntity<CarResponseDTO<List<Car>>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return ResponseEntity.ok(new CarResponseDTO<>("success", "All cars retrieved", cars));
    }

    @GetMapping("/{carId}")
    public ResponseEntity<CarResponseDTO<Car>> getCarById(@PathVariable UUID carId) {
        Car car = carService.getCarById(carId);
        return ResponseEntity.ok(new CarResponseDTO<>("success", "Car retrieved successfully", car));
    }

    @PutMapping("/{carId}")
    public ResponseEntity<CarResponseDTO<Car>> updateCar(
            @PathVariable UUID carId,
            @RequestBody @Valid Car updatedCarData
    ) {
        try {
            Car updatedCar = carService.updateCar(carId, updatedCarData);
            return ResponseEntity.ok(new CarResponseDTO<>("success", "Car updated successfully", updatedCar));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new CarResponseDTO<>("fail", e.getReason(), null));
        }
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<CarResponseDTO<Void>> deleteCar(@PathVariable UUID carId) {
        try {
            carService.deleteCar(carId);
            return ResponseEntity.ok(new CarResponseDTO<>("success", "Car deleted successfully", null));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new CarResponseDTO<>("fail", e.getReason(), null));
        }
    }

    @PatchMapping("/{carId}/availability")
    public ResponseEntity<CarResponseDTO<Car>> setCarAvailability(
            @PathVariable UUID carId,
            @RequestParam boolean available
    ) {
        try {
            Car updatedCar = carService.setCarAvailability(carId, available);
            return ResponseEntity.ok(new CarResponseDTO<>("success", "Car availability updated", updatedCar));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new CarResponseDTO<>("fail", e.getReason(), null));
        }
    }

    @GetMapping("/available")
    public ResponseEntity<CarResponseDTO<List<Car>>> getAvailableCars() {
        List<Car> cars = carService.getAvailableCars();
        return ResponseEntity.ok(new CarResponseDTO<>("success", "Available cars retrieved", cars));
    }

    @GetMapping("/unavailable")
    public ResponseEntity<CarResponseDTO<List<Car>>> getUnavailableCars() {
        List<Car> cars = carService.getUnavailableCars();
        return ResponseEntity.ok(new CarResponseDTO<>("success", "Unavailable cars retrieved", cars));
    }

}
