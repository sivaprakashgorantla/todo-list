package com.gorantla.todo.service.user;

import com.gorantla.todo.domain.user.User;
import com.gorantla.todo.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing User entities
 * Implements business logic and transaction boundaries for user operations
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Find a user by ID
     *
     * @param id the user ID
     * @return an Optional containing the user if found
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Find a user by username
     *
     * @param username the username
     * @return an Optional containing the user if found
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Find a user by email
     *
     * @param email the email address
     * @return an Optional containing the user if found
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Get all users
     *
     * @return a list of all users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Get all active users
     *
     * @return a list of active users
     */
    public List<User> findAllActive() {
        return userRepository.findByActive(true);
    }

    /**
     * Check if a username is already taken
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check if an email is already registered
     *
     * @param email the email to check
     * @return true if the email exists, false otherwise
     */
    public boolean isEmailRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Register a new user
     *
     * @param user the user to register
     * @return the saved user entity
     * @throws IllegalArgumentException if username or email already exists
     */
    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Update an existing user
     *
     * @param user the user to update
     * @return the updated user entity
     */
    @Transactional
    public User updateUser(User user) {
        // Check that user exists
        userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + user.getId()));

        // Check if username is changed and is already taken by another user
        userRepository.findByUsername(user.getUsername())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(user.getId())) {
                        throw new IllegalArgumentException("Username is already taken");
                    }
                });

        // Check if email is changed and is already taken by another user
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(user.getId())) {
                        throw new IllegalArgumentException("Email is already registered");
                    }
                });

        return userRepository.save(user);
    }

    /**
     * Change user password
     *
     * @param userId current user ID
     * @param newPassword the new password
     */
    @Transactional
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Enable or disable a user
     *
     * @param userId the user ID
     * @param active the active status
     */
    @Transactional
    public void setUserActiveStatus(Long userId, boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setActive(active);
        userRepository.save(user);
    }

    /**
     * Delete a user by ID
     *
     * @param userId the user ID
     */
    @Transactional
    public void deleteUser(Long userId) {
        // Check if user exists before deleting
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
