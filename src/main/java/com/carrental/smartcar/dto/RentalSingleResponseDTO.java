package com.carrental.smartcar.dto;

public class RentalSingleResponseDTO<T> {
    private String message;
    private String status;
    private T rental;

    public RentalSingleResponseDTO(String message, String status, T rental) {
        this.message = message;
        this.status = status;
        this.rental = rental;
    }

    // Getters and setters

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRental(T rental) {
        this.rental = rental;
    }
}
