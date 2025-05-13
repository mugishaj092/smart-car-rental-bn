package com.carrental.smartcar.repository;

import com.carrental.smartcar.model.Rental;
import com.carrental.smartcar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RentalRepository extends JpaRepository<Rental, UUID> {
    List<Rental> findByUserId(UUID userId);
    List<Rental> findByCarId(UUID carId);

    List<Rental> findByUser(User user);
}
