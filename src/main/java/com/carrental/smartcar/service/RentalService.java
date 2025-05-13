package com.carrental.smartcar.service;

import com.carrental.smartcar.dto.*;
import com.carrental.smartcar.helper.IntouchPayRequest;
import com.carrental.smartcar.model.Car;
import com.carrental.smartcar.model.Rental;
import com.carrental.smartcar.model.RentalStatus;
import com.carrental.smartcar.model.User;
import com.carrental.smartcar.repository.CarRepository;
import com.carrental.smartcar.repository.RentalRepository;
import com.carrental.smartcar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    public RentalCreationResponse createRental(RentalRequestDTO request) throws Exception {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("User not found");
        }

        // Fetch the selected car
        Optional<Car> optionalCar = carRepository.findById(request.getCarId());
        if (optionalCar.isEmpty()) {
            throw new Exception("Car not found");
        }

        Car car = optionalCar.get();
        if (!car.isAvailable()) {
            throw new Exception("Car is not available");
        }

        // Calculate rental duration and amount
        long days = java.time.Duration.between(request.getStartTime(), request.getEndTime()).toDays();
        if (days <= 0) days = 1;

        double totalAmount = days * car.getAmountPerDay();

        // Request payment via IntouchPay
        Map<String, Object> response = IntouchPayRequest.requestPayment(
                "testa",
                "71c931d4966984a90cee2bcc2953ce432899122b0f16778e5f4845d5ea18f7e6",
                user.getPhoneNumber(),
                totalAmount,
                "https://your-callback-url.com/"
        );

        String responseMessage = String.valueOf(response.get("message"));
        String responseCode = String.valueOf(response.get("responsecode"));
        System.out.println("response message: " + responseMessage);

        if (responseCode.equals("2300") || responseCode.equals("2200")) {
            throw new Exception("Payment failed: " + responseMessage);
        }

        // Create rental and save
        Rental rental = new Rental(user, car, request.getStartTime(), request.getEndTime());
        rental.setTotalAmount(totalAmount);
        rental.setStatus(RentalStatus.PENDING);
        rentalRepository.save(rental);

        // Update car availability
        car.setAvailable(false);
        carRepository.save(car);

        // Build DTOs
        CarDTO carDTO = new CarDTO(
                car.getId(),
                car.getModel(),
                car.getName(),
                car.isAvailable(),
                car.getAmountPerDay()
        );

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getUsername(),
                user.getPhoneNumber()
        );

        RentalResponseDTO rentalDto = new RentalResponseDTO(
                rental.getId(),
                carDTO,
                userDTO,
                rental.getStatus(),
                rental.getStartTime(),
                rental.getEndTime(),
                rental.getTotalAmount()
        );

        return new RentalCreationResponse("Rental created successfully", "success", rentalDto);
    }


    public RentalListResponseDTO<RentalResponseDTO> getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();

        List<RentalResponseDTO> rentalDTOs = rentals.stream().map(rental -> {
            Car car = rental.getCar();
            User user = rental.getUser();

            CarDTO carDTO = new CarDTO(
                    car.getId(),
                    car.getModel(),
                    car.getName(),
                    car.isAvailable(),
                    car.getAmountPerDay()
            );

            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getFirstName(),
                    user.getPhoneNumber()
            );

            return new RentalResponseDTO(
                    rental.getId(),
                    carDTO,
                    userDTO,
                    rental.getStatus(),
                    rental.getStartTime(),
                    rental.getEndTime(),
                    rental.getTotalAmount()
            );
        }).collect(Collectors.toList());

        return new RentalListResponseDTO<>("All rentals fetched successfully", "success", rentalDTOs);
    }

    public RentalListResponseDTO<RentalResponseDTO> getRentalsForLoggedInUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new Exception("User not found");
        }

        List<Rental> rentals = rentalRepository.findByUser(user);

        List<RentalResponseDTO> rentalDTOs = rentals.stream().map(rental -> {
            Car car = rental.getCar();

            CarDTO carDTO = new CarDTO(
                    car.getId(),
                    car.getModel(),
                    car.getName(),
                    car.isAvailable(),
                    car.getAmountPerDay()
            );

            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getFirstName()+user.getLastName(),
                    user.getPhoneNumber()
            );

            return new RentalResponseDTO(
                    rental.getId(),
                    carDTO,
                    userDTO,
                    rental.getStatus(),
                    rental.getStartTime(),
                    rental.getEndTime(),
                    rental.getTotalAmount()
            );
        }).collect(Collectors.toList());

        return new RentalListResponseDTO<>("My rentals fetched successfully", "success", rentalDTOs);
    }

    public RentalResponseDTO updateRentalStatus(UUID rentalId, RentalStatus newStatus) throws Exception {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) {
            throw new Exception("Rental not found");
        }

        Rental rental = optionalRental.get();
        rental.setStatus(newStatus);
        rentalRepository.save(rental);

        Car car = rental.getCar();
        User user = rental.getUser();

        CarDTO carDTO = new CarDTO(
                car.getId(),
                car.getModel(),
                car.getName(),
                car.isAvailable(),
                car.getAmountPerDay()
        );

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName()+" "+user.getLastName(),
                user.getPhoneNumber()
        );

        return new RentalResponseDTO(
                rental.getId(),
                carDTO,
                userDTO,
                rental.getStatus(),
                rental.getStartTime(),
                rental.getEndTime(),
                rental.getTotalAmount()
        );
    }

    public void deleteRental(UUID id) throws Exception {
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        if (optionalRental.isEmpty()) {
            throw new Exception("Rental not found");
        }

        Rental rental = optionalRental.get();

        // Optional: Make the car available again after deleting rental
        Car car = rental.getCar();
        car.setAvailable(true);
        carRepository.save(car);

        rentalRepository.delete(rental);
    }
    public RentalResponseDTO getRentalById(UUID id) throws Exception {
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        if (optionalRental.isEmpty()) {
            throw new Exception("Rental not found");
        }

        Rental rental = optionalRental.get();

        CarDTO carDTO = new CarDTO(
                        rental.getCar().getId(),
                        rental.getCar().getName(),
                        rental.getCar().getModel(),
                rental.getCar().isAvailable(),
                rental.getCar().getAmountPerDay()
                );

        UserDTO userDTO = new UserDTO(
                rental.getUser().getId(),
                rental.getUser().getUsername(),
                rental.getUser().getPhoneNumber(),
                rental.getUser().getFirstName()
        );

        return new RentalResponseDTO(
                rental.getId(),
                carDTO,
                userDTO,
                rental.getStatus(),
                rental.getStartTime(),
                rental.getEndTime(),
                rental.getTotalAmount()
        );
    }

}