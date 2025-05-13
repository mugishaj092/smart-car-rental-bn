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
}
