package org.example.repository;

import org.example.enums.CarStatus;
import org.example.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepo extends JpaRepository<Car, Long> {
    List<Car> findByStatus(CarStatus status);

    boolean existsByRegistrationNumber(String registrationNumber);
}
