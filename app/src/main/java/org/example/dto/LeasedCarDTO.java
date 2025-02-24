package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Lease;

import java.time.LocalDate;


public class LeasedCarDTO {
    private Long leaseId;
    private Long carId;
    private Long customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Lease.LeaseStatus status;

    // ✅ Constructor
    public LeasedCarDTO(Long leaseId, Long carId, Long customerId, LocalDate startDate, LocalDate endDate, Lease.LeaseStatus status) {
        this.leaseId = leaseId;
        this.carId = carId;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // ✅ Getters and Setters
    public Long getLeaseId() { return leaseId; }
    public void setLeaseId(Long leaseId) { this.leaseId = leaseId; }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Lease.LeaseStatus getStatus() { return status; }
    public void setStatus(Lease.LeaseStatus status) { this.status = status; }
}

