package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepo userRepository;

    public UserService(UserRepo userRepo) {

        this.userRepository = userRepo;
    }

    public List<User> getAllUsers() {
        logger.info("Fetching all users from the database");
        List<User> users = userRepository.findAll();
        logger.info("Found {} users", users.size());
        return users;
    }

    public User registerUser(User user) {
        logger.info("Registering new user with username: {}", user.getName());
        User savedUser = userRepository.save(user);
        logger.info("User {} registered successfully with ID {}", savedUser.getName(), savedUser.getId());
        return savedUser;
    }
}
