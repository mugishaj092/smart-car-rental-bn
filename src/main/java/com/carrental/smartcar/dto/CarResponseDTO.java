package com.carrental.smartcar.dto;

public class CarResponseDTO<T> {
    private String status;
    private String message;
    private T car;

    public CarResponseDTO(String status, String message, T car) {
        this.status = status;
        this.message = message;
        this.car = car;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getCar() {
        return car;
    }

    public void setCar(T car) {
        this.car = car;
    }
}
