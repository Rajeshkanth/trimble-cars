package org.example.controller;

import org.example.dto.LeasedCarDTO;
import org.example.dto.LeaseRequest;
import org.example.model.Lease;
import org.example.service.LeaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leases")
public class LeaseController {

    @Autowired
    private LeaseService leaseService;

    // ✅ Lease a car
    @PostMapping("/lease")
    public ResponseEntity<Lease> leaseCar(@RequestBody LeaseRequest leaseRequest) {
        Lease lease = leaseService.leaseCar(
                leaseRequest.getCarId(),
                leaseRequest.getCustomerId(),
                leaseRequest.getStartDate(),
                leaseRequest.getEndDate()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(lease);
    }

    // ✅ Get all leased cars
    @GetMapping("/all")
    public ResponseEntity<List<LeasedCarDTO>> getAllLeasedCars() {
        List<LeasedCarDTO> leasedCars = leaseService.getAllLeasedCars();
        return ResponseEntity.ok(leasedCars);
    }

    // ✅ Get leased cars by customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<LeasedCarDTO>> getLeasedCarsByCustomer(@PathVariable Long customerId) {
        List<LeasedCarDTO> leasedCars = leaseService.getLeasedCarsByCustomer(customerId);
        return ResponseEntity.ok(leasedCars);
    }

    // ✅ Get leased car details by lease ID
    @GetMapping("/{leaseId}")
    public ResponseEntity<LeasedCarDTO> getLeasedCarById(@PathVariable Long leaseId) {
        LeasedCarDTO leasedCar = leaseService.getLeasedCarById(leaseId);
        return ResponseEntity.ok(leasedCar);
    }

    @PutMapping("/{leaseId}/end")
    public ResponseEntity<Lease> endLease(@PathVariable Long leaseId) {
        return ResponseEntity.ok(leaseService.endLease(leaseId));
    }
}
