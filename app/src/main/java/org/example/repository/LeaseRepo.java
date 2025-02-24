package org.example.repository;

import org.example.model.Car;
import org.example.model.Lease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaseRepo extends JpaRepository<Lease, Long> {
    List<Lease> findByCustomerId(Long customerId);

    long countByCustomerIdAndStatus(Long customerId, Lease.LeaseStatus leaseStatus);
}
