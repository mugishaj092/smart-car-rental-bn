package com.carrental.smartcar.dto;

import java.util.List;

public class RentalListResponseDTO<T> {
    private String message;
    private String status;
    private List<T> rentals;

    public RentalListResponseDTO(String message, String status, List<T> rentals) {
        this.message = message;
        this.status = status;
        this.rentals = rentals;
    }

    // Getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<T> getRentals() { return rentals; }
    public void setRentals(List<T> rentals) { this.rentals = rentals; }
}
