package org.example.service;

import org.example.enums.CarStatus;
import org.example.model.Car;
import org.example.model.User;
import org.example.repository.CarRepo;
import org.example.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CarService {
    private static final Logger logger = LoggerFactory.getLogger(CarService.class);
    private final CarRepo carRepository;
    private final UserRepo userRepository;

    public CarService(CarRepo carRepository, UserRepo userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public Car registerCar(Car car) {
        logger.info("Registering new car with registration number: {}", car.getRegistrationNumber());
        if (car.getOwner() == null || car.getOwner().getId() == null) {
            logger.warn("Owner ID is missing for car registration");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Owner ID is missing");
        }

        // Fetch the user from the database
        User owner = userRepository.findById(car.getOwner().getId())
                .orElseThrow(() -> {
                    logger.error("Owner with ID {} not found", car.getOwner().getId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found");
                });

        // Ensure the user has the CAR_OWNER role
        if (owner.getRole() != User.Role.CAR_OWNER) {
            logger.warn("User with ID {} is not an owner and cannot register a car", owner.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not an owner and cannot register a car");
        }

        // Check if a car with the same registration number exists
        if (carRepository.existsByRegistrationNumber(car.getRegistrationNumber())) {
            logger.warn("Car with registration number {} is already registered", car.getRegistrationNumber());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Car with this registration number is already registered");
        }

        // Set the validated owner
        car.setOwner(owner);
        Car savedCar = carRepository.save(car);
        logger.info("Car with registration number {} registered successfully", savedCar.getRegistrationNumber());
        return savedCar;
    }

    public List<Car> getAvailableCars() {
        logger.info("Fetching available cars with status: {}", CarStatus.IDLE);
        List<Car> availableCars = carRepository.findByStatus(CarStatus.IDLE);
        logger.info("Found {} available cars", availableCars.size());
        return availableCars;
    }
}
