package com.carrental.smartcar.service;

import com.carrental.smartcar.model.Car;
import com.carrental.smartcar.repository.CarRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final Cloudinary cloudinary;

    @Autowired
    public CarService(CarRepository carRepository, Cloudinary cloudinary) {
        this.carRepository = carRepository;
        this.cloudinary = cloudinary;
    }

    public Car addCar(Car car) {
        System.out.println(car.getId());
        return carRepository.save(car);
    }

    public Car updateCarImage(UUID carId, MultipartFile imageFile) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

        if (imageFile == null || imageFile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image file is required");
        }

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    imageFile.getBytes(),
                    ObjectUtils.asMap("folder", "smartcar/cars")
            );
            String imageUrl = uploadResult.get("secure_url").toString();
            car.setImageUrl(imageUrl);

            return carRepository.save(car);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image");
        }
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(UUID carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
    }

    public Car updateCar(UUID carId, Car updatedCarData) {
        Car existingCar = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

        existingCar.setName(updatedCarData.getName());
        existingCar.setModel(updatedCarData.getModel());
        existingCar.setYear(updatedCarData.getYear());
        existingCar.setFuelType(updatedCarData.getFuelType());
        existingCar.setTransmission(updatedCarData.getTransmission());
        existingCar.setAvailable(updatedCarData.isAvailable());

        return carRepository.save(existingCar);
    }

    public void deleteCar(UUID carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
        carRepository.delete(car);
    }

    public Car setCarAvailability(UUID carId, boolean available) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

        car.setAvailable(available);
        return carRepository.save(car);
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByAvailable(true);
    }

    public List<Car> getUnavailableCars() {
        return carRepository.findByAvailable(false);
    }


}
