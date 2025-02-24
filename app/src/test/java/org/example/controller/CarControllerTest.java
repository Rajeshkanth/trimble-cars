package org.example.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import org.example.enums.CarStatus;
import org.example.model.Car;
import org.example.model.User;
import org.example.model.User.Role;
import org.example.service.CarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {CarController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CarControllerTest {
    @Autowired
    private CarController carController;

    @MockBean
    private CarService carService;


    @Test
    @DisplayName("Test registerCar(Car)")
    void testRegisterCar() throws Exception {
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
        when(carService.registerCar(Mockito.<Car>any())).thenReturn(car);

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
        car2.setLeaseHistory(new ArrayList<>());
        car2.setModel("Model");
        car2.setOwner(owner2);
        car2.setRegistrationNumber("42");
        car2.setStatus(CarStatus.IDLE);
        String content = (new ObjectMapper()).writeValueAsString(car2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/cars/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(carController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"model\":\"Model\",\"brand\":\"Brand\",\"availableForLease\":true,\"registrationNumber\":\"42\",\"status\":"
                                        + "\"IDLE\",\"owner\":{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"role\":\"CAR_OWNER\"}}"));
    }


    @Test
    @DisplayName("Test getAvailableCars()")
    void testGetAvailableCars() throws Exception {
        when(carService.getAvailableCars()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/cars/available");

        MockMvcBuilders.standaloneSetup(carController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}