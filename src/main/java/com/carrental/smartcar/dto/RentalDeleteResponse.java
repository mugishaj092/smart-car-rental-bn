package com.carrental.smartcar.dto;

public class RentalDeleteResponse {
    private String message;
    private String status;

    public RentalDeleteResponse(String message, String status) {
        this.message = message;
        this.status = status;
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
}
