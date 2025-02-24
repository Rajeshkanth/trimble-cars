package org.example.service;

import org.example.dto.LeasedCarDTO;
import org.example.model.Car;
import org.example.model.Lease;
import org.example.model.User;
import org.example.repository.CarRepo;
import org.example.repository.LeaseRepo;
import org.example.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaseService {

    private static final Logger logger = LoggerFactory.getLogger(LeaseService.class);

    @Autowired
    private CarRepo carRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private LeaseRepo leaseRepository;

    public Lease leaseCar(Long carId, Long customerId, LocalDate startDate, LocalDate endDate) {
        logger.info("Leasing car {} to customer {} from {} to {}", carId, customerId, startDate, endDate);
        long activeLeaseCount = leaseRepository.countByCustomerIdAndStatus(customerId, Lease.LeaseStatus.ACTIVE);
        if (activeLeaseCount >= 2) {
            logger.warn("Customer {} has reached the max limit of active leases", customerId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Max 2 active leases allowed per customer.");
        }

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> {
                    logger.error("Car {} not found", carId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found");
                });

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> {
                    logger.error("Customer {} not found", customerId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
                });

        if (customer.getRole() != User.Role.END_CUSTOMER) {
            logger.warn("User {} is not a customer and cannot lease cars", customerId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only customers can lease cars");
        }

        if (!car.isAvailableForLease()) {
            logger.warn("Car {} is already leased", carId);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Car is already leased");
        }

        Lease lease = new Lease();
        lease.setCar(car);
        lease.setCustomer(customer);
        lease.setStartDate(startDate.atStartOfDay());
        lease.setEndDate(endDate.atStartOfDay());
        lease.setStatus(Lease.LeaseStatus.ACTIVE);

        // ✅ Set car availability to false
        car.setAvailableForLease(false);
        carRepository.save(car);
        leaseRepository.save(lease);
        logger.info("Lease created successfully: Lease ID {}", lease.getId());
        return lease;

    }

    // ✅ End lease for the car
    public Lease endLease(Long leaseId) {
        logger.info("Ending lease with ID {}", leaseId);
        Lease lease = leaseRepository.findById(leaseId)
                .orElseThrow(() -> {
                    logger.error("Lease {} not found", leaseId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Lease not found");
                });
        lease.setStatus(Lease.LeaseStatus.ENDED);
        lease.getCar().setAvailableForLease(true); // Car becomes available again
        leaseRepository.save(lease);
        logger.info("Lease {} ended successfully", leaseId);
        return lease;
    }

    // ✅ Fetch leased car details by lease ID
    public LeasedCarDTO getLeasedCarById(Long leaseId) {
        logger.info("Fetching lease details for ID {}", leaseId);
        Lease lease = leaseRepository.findById(leaseId)
                .orElseThrow(() -> {
                    logger.error("Lease {} not found", leaseId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Lease not found");
                });
        return convertToDTO(lease);
    }

    // ✅ Get all leased cars
    public List<LeasedCarDTO> getAllLeasedCars() {
        logger.info("Fetching all leased cars");
        List<Lease> leases = leaseRepository.findAll();
        return leases.stream().map(this::convertToDTO).toList();
    }

    // ✅ Get all leased cars by customer
    public List<LeasedCarDTO> getLeasedCarsByCustomer(Long customerId) {
        logger.info("Fetching leased cars for customer {}", customerId);
        List<Lease> leases = leaseRepository.findByCustomerId(customerId);
        return leases.stream().map(this::convertToDTO).toList();
    }

    private LeasedCarDTO convertToDTO(Lease lease) {
        return new LeasedCarDTO(
                lease.getId(),
                lease.getCar().getId(),
                lease.getCustomer().getId(),
                lease.getStartDate().toLocalDate(),
                lease.getEndDate().toLocalDate(),
                lease.getStatus()
        );
    }

}
