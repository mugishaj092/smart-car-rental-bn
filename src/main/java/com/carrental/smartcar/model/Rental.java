package com.carrental.smartcar.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id")
    private Car car;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private RentalStatus status = RentalStatus.PENDING;

    public Rental() {}

    public Rental(User user, Car car, LocalDateTime startTime, LocalDateTime endTime) {
        this.user = user;
        this.car = car;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
}
