package org.example.controller;

import org.example.enums.CarStatus;
import org.example.model.Car;
import org.example.model.Lease;
import org.example.model.User;
import org.example.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @DisplayName("Should return all users successfully")
    void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(
                new User(1L, "John", "john@example.com", User.Role.END_CUSTOMER, new ArrayList<>(), new ArrayList<>()),
                new User(2L, "Jane", "jane@example.com", User.Role.CAR_OWNER, new ArrayList<>(), new ArrayList<>())
        );

        when(adminService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].name").value("Jane"));

        verify(adminService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Should return user by ID successfully")
    void testGetUserById() throws Exception {
        User user = new User(1L, "John", "john@example.com", User.Role.END_CUSTOMER, new ArrayList<>(), new ArrayList<>());

        when(adminService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/admin/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));

        verify(adminService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("Should delete user successfully")
    void testDeleteUser() throws Exception {
        doNothing().when(adminService).deleteUser(1L);

        mockMvc.perform(delete("/admin/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        verify(adminService, times(1)).deleteUser(1L);
    }

    @Test
    @DisplayName("Should return all cars successfully")
    void testGetAllCars() throws Exception {
        List<Car> cars = Arrays.asList(
                new Car(1L, "Corolla", "Toyota", true, "ABC123", CarStatus.IDLE, null, new ArrayList<>()),
                new Car(2L, "Civic", "Honda", true, "XYZ789", CarStatus.IDLE, null, new ArrayList<>())
        );

        when(adminService.getAllCars()).thenReturn(cars);

        mockMvc.perform(get("/admin/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].model").value("Corolla"))
                .andExpect(jsonPath("$[0].brand").value("Toyota"));

        verify(adminService, times(1)).getAllCars();
    }

    @Test
    @DisplayName("Should delete car successfully")
    void testDeleteCar() throws Exception {
        doNothing().when(adminService).deleteCar(1L);

        mockMvc.perform(delete("/admin/cars/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Car deleted successfully"));

        verify(adminService, times(1)).deleteCar(1L);
    }

    @Test
    @DisplayName("Should return all leases successfully")
    void testGetAllLeases() throws Exception {
        List<Lease> leases = Arrays.asList(
                new Lease(
                        1L, new User(1L, "John", "john@example.com", User.Role.END_CUSTOMER, new ArrayList<>(), new ArrayList<>()),
                        new Car(1L, "Corolla", "Toyota", true, "ABC123", CarStatus.IDLE, new User(), new ArrayList<>()),
                        LocalDateTime.now(), LocalDateTime.now().plusDays(7), Lease.LeaseStatus.ACTIVE
                ),
                new Lease(
                        2L, new User(2L, "Jane", "jane@example.com", User.Role.CAR_OWNER, new ArrayList<>(), new ArrayList<>()),
                        new Car(2L, "Civic", "Honda", true, "XYZ789", CarStatus.IDLE, new User(), new ArrayList<>()),
                        LocalDateTime.now(), LocalDateTime.now().plusDays(5), Lease.LeaseStatus.COMPLETED
                )
        );

        when(adminService.getAllLeases()).thenReturn(leases);

        mockMvc.perform(get("/admin/leases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[1].status").value("COMPLETED"));

        verify(adminService, times(1)).getAllLeases();
    }

    @Test
    @DisplayName("Should end lease successfully")
    void testEndLease() throws Exception {
        doNothing().when(adminService).endLease(1L);

        mockMvc.perform(put("/admin/leases/1/end"))
                .andExpect(status().isOk())
                .andExpect(content().string("Lease ended successfully"));

        verify(adminService, times(1)).endLease(1L);
    }
}
