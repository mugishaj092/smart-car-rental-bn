package com.carrental.smartcar.dto;

import com.carrental.smartcar.model.RentalStatus;

public class RentalStatusUpdateRequest {
    private RentalStatus status;

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }
}
