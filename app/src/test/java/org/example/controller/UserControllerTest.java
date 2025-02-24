package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Register a new user successfully")
    void testRegisterUser() {
        User user = new User(1L, "John Doe", "john@example.com", User.Role.CAR_OWNER, null, null);
        when(userService.registerUser(any(User.class))).thenReturn(user);

        User response = userController.registerUser(user);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals(User.Role.CAR_OWNER, response.getRole());
        verify(userService, times(1)).registerUser(user);
    }


    @Test
    @DisplayName("Get all users successfully")
    void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User(1L, "John Doe", "john@example.com", User.Role.CAR_OWNER, null, null),
                new User(2L, "Jane Doe", "jane@example.com", User.Role.END_CUSTOMER, null, null)
        );

        when(userService.getAllUsers()).thenReturn(users);

        List<User> response = userController.getAllUsers();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("John Doe", response.get(0).getName());
        assertEquals(User.Role.CAR_OWNER, response.get(0).getRole());
        verify(userService, times(1)).getAllUsers();
    }

}