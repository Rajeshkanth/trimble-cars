package org.example.controller;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;

import org.example.dto.LeasedCarDTO;
import org.example.enums.CarStatus;
import org.example.model.Car;
import org.example.model.Lease;
import org.example.model.Lease.LeaseStatus;
import org.example.model.User;
import org.example.model.User.Role;
import org.example.service.LeaseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {LeaseController.class})
@ExtendWith(SpringExtension.class)
class LeaseControllerTest {
    @Autowired
    private LeaseController leaseController;

    @MockBean
    private LeaseService leaseService;

    @Test
    @DisplayName("Test getAllLeasedCars()")
    void testGetAllLeasedCars() throws Exception {
        when(leaseService.getAllLeasedCars()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/leases/all");

        MockMvcBuilders.standaloneSetup(leaseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    @DisplayName("Test getLeasedCarsByCustomer(Long)")
    void testGetLeasedCarsByCustomer() throws Exception {
        when(leaseService.getLeasedCarsByCustomer(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/leases/customer/{customerId}", 1L);

        MockMvcBuilders.standaloneSetup(leaseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    @DisplayName("Test getLeasedCarById(Long)")
    void testGetLeasedCarById() throws Exception {
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        when(leaseService.getLeasedCarById(Mockito.<Long>any()))
                .thenReturn(new LeasedCarDTO(1L, 1L, 1L, startDate, LocalDate.of(1970, 1, 1), LeaseStatus.ACTIVE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/leases/{leaseId}", 1L);

        MockMvcBuilders.standaloneSetup(leaseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"leaseId\":1,\"carId\":1,\"customerId\":1,\"startDate\":[1970,1,1],\"endDate\":[1970,1,1],\"status\":\"ACTIVE\"}"));
    }


    @Test
    @DisplayName("Test endLease(Long)")
    void testEndLease() throws Exception {
        User owner = new User();
        owner.setCars(new ArrayList<>());
        owner.setEmail("jane.doe@example.org");
        owner.setId(1L);
        owner.setLeases(new ArrayList<>());
        owner.setName("Name");
        owner.setRole(Role.CAR_OWNER);

        Car car = new Car();
        car.setAvailableForLease(true);
        car.setBrand("Brand");
        car.setId(1L);
        car.setLeaseHistory(new ArrayList<>());
        car.setModel("Model");
        car.setOwner(owner);
        car.setRegistrationNumber("42");
        car.setStatus(CarStatus.IDLE);

        User customer = new User();
        customer.setCars(new ArrayList<>());
        customer.setEmail("jane.doe@example.org");
        customer.setId(1L);
        customer.setLeases(new ArrayList<>());
        customer.setName("Name");
        customer.setRole(Role.CAR_OWNER);

        Lease lease = new Lease();
        lease.setCar(car);
        lease.setCustomer(customer);
        lease.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lease.setId(1L);
        lease.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lease.setStatus(LeaseStatus.ACTIVE);
        when(leaseService.endLease(Mockito.<Long>any())).thenReturn(lease);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/leases/{leaseId}/end", 1L);

        MockMvcBuilders.standaloneSetup(leaseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"customer\":{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"role\":\"CAR_OWNER\"},\"car\":{"
                                        + "\"id\":1,\"model\":\"Model\",\"brand\":\"Brand\",\"availableForLease\":true,\"registrationNumber\":\"42\",\"status\":"
                                        + "\"IDLE\",\"owner\":{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"role\":\"CAR_OWNER\"}},\"startDate\""
                                        + ":[1970,1,1,0,0],\"endDate\":[1970,1,1,0,0],\"status\":\"ACTIVE\"}"));
    }


    @Test
    @DisplayName("Test endLease(Long); given ArrayList() add Lease()")
    void testEndLease_givenArrayListAddLease() throws Exception {
        User owner = new User();
        owner.setCars(new ArrayList<>());
        owner.setEmail("jane.doe@example.org");
        owner.setId(1L);
        owner.setLeases(new ArrayList<>());
        owner.setName("Name");
        owner.setRole(Role.CAR_OWNER);

        Car car = new Car();
        car.setAvailableForLease(true);
        car.setBrand("Brand");
        car.setId(1L);
        car.setLeaseHistory(new ArrayList<>());
        car.setModel("Model");
        car.setOwner(owner);
        car.setRegistrationNumber("42");
        car.setStatus(CarStatus.IDLE);

        User customer = new User();
        customer.setCars(new ArrayList<>());
        customer.setEmail("jane.doe@example.org");
        customer.setId(1L);
        customer.setLeases(new ArrayList<>());
        customer.setName("Name");
        customer.setRole(Role.CAR_OWNER);

        Lease lease = new Lease();
        lease.setCar(car);
        lease.setCustomer(customer);
        lease.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lease.setId(1L);
        lease.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lease.setStatus(LeaseStatus.ACTIVE);

        ArrayList<Lease> leaseHistory = new ArrayList<>();
        leaseHistory.add(lease);

        User owner2 = new User();
        owner2.setCars(new ArrayList<>());
        owner2.setEmail("jane.doe@example.org");
        owner2.setId(1L);
        owner2.setLeases(new ArrayList<>());
        owner2.setName("Name");
        owner2.setRole(Role.CAR_OWNER);

        Car car2 = new Car();
        car2.setAvailableForLease(true);
        car2.setBrand("Brand");
        car2.setId(1L);
        car2.setLeaseHistory(leaseHistory);
        car2.setModel("Model");
        car2.setOwner(owner2);
        car2.setRegistrationNumber("42");
        car2.setStatus(CarStatus.IDLE);

        User customer2 = new User();
        customer2.setCars(new ArrayList<>());
        customer2.setEmail("jane.doe@example.org");
        customer2.setId(1L);
        customer2.setLeases(new ArrayList<>());
        customer2.setName("Name");
        customer2.setRole(Role.CAR_OWNER);

        Lease lease2 = new Lease();
        lease2.setCar(car2);
        lease2.setCustomer(customer2);
        lease2.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lease2.setId(1L);
        lease2.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lease2.setStatus(LeaseStatus.ACTIVE);
        when(leaseService.endLease(Mockito.<Long>any())).thenReturn(lease2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/leases/{leaseId}/end", 1L);

        MockMvcBuilders.standaloneSetup(leaseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"customer\":{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"role\":\"CAR_OWNER\"},\"car\":{"
                                        + "\"id\":1,\"model\":\"Model\",\"brand\":\"Brand\",\"availableForLease\":false,\"registrationNumber\":\"42\",\"status\":"
                                        + "\"IDLE\",\"owner\":{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"role\":\"CAR_OWNER\"}},\"startDate\""
                                        + ":[1970,1,1,0,0],\"endDate\":[1970,1,1,0,0],\"status\":\"ACTIVE\"}"));
    }

    @Test
    @DisplayName("Test endLease(Long); given User() Email is 'john.smith@example.org'")
    void testEndLease_givenUserEmailIsJohnSmithExampleOrg() throws Exception {
        User owner = new User();
        owner.setCars(new ArrayList<>());
        owner.setEmail("jane.doe@example.org");
        owner.setId(1L);
        owner.setLeases(new ArrayList<>());
        owner.setName("Name");
        owner.setRole(Role.CAR_OWNER);

        Car car = new Car();
        car.setAvailableForLease(true);
        car.setBrand("Brand");
        car.setId(1L);
        car.setLeaseHistory(new ArrayList<>());
        car.setModel("Model");
        car.setOwner(owner);
        car.setRegistrationNumber("42");
        car.setStatus(CarStatus.IDLE);

        User customer = new User();
        customer.setCars(new ArrayList<>());
        customer.setEmail("jane.doe@example.org");
        customer.setId(1L);
        customer.setLeases(new ArrayList<>());
        customer.setName("Name");
        customer.setRole(Role.CAR_OWNER);

        Lease lease = new Lease();
        lease.setCar(car);
        lease.setCustomer(customer);
        lease.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lease.setId(1L);
        lease.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lease.setStatus(LeaseStatus.ACTIVE);

        User owner2 = new User();
        owner2.setCars(new ArrayList<>());
        owner2.setEmail("john.smith@example.org");
        owner2.setId(2L);
        owner2.setLeases(new ArrayList<>());
        owner2.setName("org.example.model.User");
        owner2.setRole(Role.END_CUSTOMER);

        Car car2 = new Car();
        car2.setAvailableForLease(false);
        car2.setBrand("42");
        car2.setId(2L);
        car2.setLeaseHistory(new ArrayList<>());
        car2.setModel("42");
        car2.setOwner(owner2);
        car2.setRegistrationNumber("Registration Number");
        car2.setStatus(CarStatus.ON_LEASE);

        User customer2 = new User();
        customer2.setCars(new ArrayList<>());
        customer2.setEmail("john.smith@example.org");
        customer2.setId(2L);
        customer2.setLeases(new ArrayList<>());
        customer2.setName("org.example.model.User");
        customer2.setRole(Role.END_CUSTOMER);

        Lease lease2 = new Lease();
        lease2.setCar(car2);
        lease2.setCustomer(customer2);
        lease2.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lease2.setId(2L);
        lease2.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lease2.setStatus(LeaseStatus.COMPLETED);

        ArrayList<Lease> leaseHistory = new ArrayList<>();
        leaseHistory.add(lease2);
        leaseHistory.add(lease);

        User owner3 = new User();
        owner3.setCars(new ArrayList<>());
        owner3.setEmail("jane.doe@example.org");
        owner3.setId(1L);
        owner3.setLeases(new ArrayList<>());
        owner3.setName("Name");
        owner3.setRole(Role.CAR_OWNER);

        Car car3 = new Car();
        car3.setAvailableForLease(true);
        car3.setBrand("Brand");
        car3.setId(1L);
        car3.setLeaseHistory(leaseHistory);
        car3.setModel("Model");
        car3.setOwner(owner3);
        car3.setRegistrationNumber("42");
        car3.setStatus(CarStatus.IDLE);

        User customer3 = new User();
        customer3.setCars(new ArrayList<>());
        customer3.setEmail("jane.doe@example.org");
        customer3.setId(1L);
        customer3.setLeases(new ArrayList<>());
        customer3.setName("Name");
        customer3.setRole(Role.CAR_OWNER);

        Lease lease3 = new Lease();
        lease3.setCar(car3);
        lease3.setCustomer(customer3);
        lease3.setEndDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lease3.setId(1L);
        lease3.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        lease3.setStatus(LeaseStatus.ACTIVE);
        when(leaseService.endLease(Mockito.<Long>any())).thenReturn(lease3);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/leases/{leaseId}/end", 1L);

        MockMvcBuilders.standaloneSetup(leaseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"customer\":{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"role\":\"CAR_OWNER\"},\"car\":{"
                                        + "\"id\":1,\"model\":\"Model\",\"brand\":\"Brand\",\"availableForLease\":false,\"registrationNumber\":\"42\",\"status\":"
                                        + "\"IDLE\",\"owner\":{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"role\":\"CAR_OWNER\"}},\"startDate\""
                                        + ":[1970,1,1,0,0],\"endDate\":[1970,1,1,0,0],\"status\":\"ACTIVE\"}"));
    }
}