package org.example.controller;

import org.example.model.Car;
import org.example.model.Lease;
import org.example.model.User;
import org.example.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 🔹 View all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    // 🔹 View a specific user
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return adminService.getUserById(id);
    }

    // 🔹 Delete a user
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return "User deleted successfully";
    }

    // 🔹 View all cars
    @GetMapping("/cars")
    public List<Car> getAllCars() {
        return adminService.getAllCars();
    }

    // 🔹 Delete a car
    @DeleteMapping("/cars/{id}")
    public String deleteCar(@PathVariable Long id) {
        adminService.deleteCar(id);
        return "Car deleted successfully";
    }

    // 🔹 View all leases
    @GetMapping("/leases")
    public List<Lease> getAllLeases() {
        return adminService.getAllLeases();
    }

    // 🔹 End a lease manually
    @PutMapping("/leases/{leaseId}/end")
    public String endLease(@PathVariable Long leaseId) {
        adminService.endLease(leaseId);
        return "Lease ended successfully";
    }
}

