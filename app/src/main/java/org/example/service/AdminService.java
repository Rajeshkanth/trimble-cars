package org.example.service;

import org.example.model.*;
import org.example.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
    private final UserRepo userRepository;
    private final CarRepo carRepository;
    private final LeaseRepo leaseRepository;

    public AdminService(UserRepo userRepository, CarRepo carRepository, LeaseRepo leaseRepository) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.leaseRepository = leaseRepository;
    }

    // Get all users
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    // Get user by ID
    public User getUserById(Long id) {
        logger.info("Fetching user with ID {}", id);
        return userRepository.findById(id).orElseThrow(() -> {
            logger.error("User with ID {} not found", id);
            return new RuntimeException("User not found");
        });
    }

    // Delete a user
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID {}", id);
        if (!userRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existing user with ID {}", id);
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
        logger.info("User with ID {} deleted successfully", id);
    }

    // Get all cars
    public List<Car> getAllCars() {
        logger.info("Fetching all cars");
        return carRepository.findAll();
    }

    // Delete a car
    public void deleteCar(Long id) {
        logger.info("Deleting car with ID {}", id);
        if (!carRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existing car with ID {}", id);
            throw new RuntimeException("Car not found");
        }
        carRepository.deleteById(id);
        logger.info("Car with ID {} deleted successfully", id);
    }

    //  Get all leases
    public List<Lease> getAllLeases() {
        logger.info("Fetching all leases");
        return leaseRepository.findAll();
    }

    //  End a lease manually
    public void endLease(Long leaseId) {
        logger.info("Ending lease with ID {}", leaseId);
        Optional<Lease> leaseOptional = leaseRepository.findById(leaseId);
        if (leaseOptional.isPresent()) {
            Lease lease = leaseOptional.get();
            lease.setActive(false);
            leaseRepository.save(lease);
            logger.info("Lease with ID {} ended successfully", leaseId);
        } else {
            logger.error("Lease with ID {} not found", leaseId);
            throw new RuntimeException("Lease not found");
        }
    }
}
