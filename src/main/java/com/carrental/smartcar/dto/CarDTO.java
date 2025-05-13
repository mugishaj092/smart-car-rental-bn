package com.carrental.smartcar.dto;

import java.util.UUID;

public class CarDTO {
    private UUID id;
    private String model;
    private String brand;
    private boolean available;
    private double amountPerDay;

    public CarDTO(UUID id, String model, String brand, boolean available, double amountPerDay) {
        this.id = id;
        this.model = model;
        this.brand = brand;
        this.available = available;
        this.amountPerDay = amountPerDay;
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getAmountPerDay() {
        return amountPerDay;
    }

    public void setAmountPerDay(double amountPerDay) {
        this.amountPerDay = amountPerDay;
    }
}
