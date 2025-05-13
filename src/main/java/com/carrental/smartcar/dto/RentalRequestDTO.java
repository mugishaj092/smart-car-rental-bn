package com.carrental.smartcar.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class RentalRequestDTO {
    private UUID carId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
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
}
