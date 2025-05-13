package com.carrental.smartcar.dto;

public class RentalStatusUpdateResponse {
    private String message;
    private String status;
    private RentalResponseDTO rental;

    public RentalStatusUpdateResponse(String message, String status, RentalResponseDTO rental) {
        this.message = message;
        this.status = status;
        this.rental = rental;
    }

    // Getters and setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RentalResponseDTO getRental() {
        return rental;
    }

    public void setRental(RentalResponseDTO rental) {
        this.rental = rental;
    }
}
