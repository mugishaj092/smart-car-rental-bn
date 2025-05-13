package com.carrental.smartcar.dto;

import jakarta.validation.constraints.*;

public class CarRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Model is required")
    private String model;

    @Min(value = 1886, message = "Year must be a valid year")
    private int year;

    @NotBlank(message = "Fuel type is required")
    private String fuelType;

    @NotBlank(message = "Transmission is required")
    private String transmission;

    private boolean available;

    // Getters and setters

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getFuelType() {
        return fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
