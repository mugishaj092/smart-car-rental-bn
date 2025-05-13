package com.carrental.smartcar.dto;

import com.carrental.smartcar.model.RentalStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class RentalResponseDTO {
    private UUID rentalId;
    private CarDTO car;
    private UserDTO user;
    private RentalStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalAmount;

    public RentalResponseDTO(UUID rentalId, CarDTO car, UserDTO user, RentalStatus status,
                             LocalDateTime startTime, LocalDateTime endTime, double totalAmount) {
        this.rentalId = rentalId;
        this.car = car;
        this.user = user;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalAmount = totalAmount;
    }

    // Getters and setters

    public UUID getRentalId() {
        return rentalId;
    }

    public void setRentalId(UUID rentalId) {
        this.rentalId = rentalId;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
