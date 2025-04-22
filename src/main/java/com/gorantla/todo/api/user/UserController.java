package com.gorantla.todo.api.user;

import com.gorantla.todo.api.user.dto.ChangePasswordRequest;
import com.gorantla.todo.api.user.dto.CreateUserRequest;
import com.gorantla.todo.api.user.dto.UpdateUserRequest;
import com.gorantla.todo.api.user.dto.UserResponse;
import com.gorantla.todo.domain.user.User;
import com.gorantla.todo.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST controller for managing User resources
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Operations pertaining to users in the Todo application")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /api/users : Get all users
     *
     * @return the ResponseEntity with status 200 (OK) and the list of users
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.findAll().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/users/active : Get all active users
     *
     * @return the ResponseEntity with status 200 (OK) and the list of active users
     */
    @GetMapping("/active")
    @Operation(summary = "Get all active users", description = "Returns a list of all active users")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all active users")
    public ResponseEntity<List<UserResponse>> getAllActiveUsers() {
        List<UserResponse> users = userService.findAllActive().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/users/{id} : Get user by id
     *
     * @param id the id of the user to retrieve
     * @return the ResponseEntity with status 200 (OK) and the user, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID", description = "Returns a user based on ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        return userService.findById(id)
                .map(this::convertToUserResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    /**
     * GET /api/users/username/{username} : Get user by username
     *
     * @param username the username of the user to retrieve
     * @return the ResponseEntity with status 200 (OK) and the user, or with status 404 (Not Found)
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "Get a user by username", description = "Returns a user based on username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<UserResponse> getUserByUsername(
            @Parameter(description = "Username", required = true)
            @PathVariable String username) {
        return userService.findByUsername(username)
                .map(this::convertToUserResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    /**
     * GET /api/users/email/{email} : Get user by email
     *
     * @param email the email of the user to retrieve
     * @return the ResponseEntity with status 200 (OK) and the user, or with status 404 (Not Found)
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Get a user by email", description = "Returns a user based on email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<UserResponse> getUserByEmail(
            @Parameter(description = "Email", required = true)
            @PathVariable String email) {
        return userService.findByEmail(email)
                .map(this::convertToUserResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    /**
     * POST /api/users : Create a new user
     *
     * @param createUserRequest the user to create
     * @return the ResponseEntity with status 201 (Created) and the new user
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user", description = "Creates a new user and returns the created user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input or constraints violation", 
                         content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "User details", required = true)
            @Valid @RequestBody CreateUserRequest createUserRequest) {
        User user = convertToUserEntity(createUserRequest);
        User savedUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToUserResponse(savedUser));
    }

    /**
     * PUT /api/users/{id} : Update an existing user
     *
     * @param id the id of the user to update
     * @param updateUserRequest the user details to update
     * @return the ResponseEntity with status 200 (OK) and the updated user
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Updates a user and returns the updated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input or constraints violation"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user details", required = true)
            @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        
        // Verify user exists
        User existingUser = userService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        
        // Update fields
        existingUser.setUsername(updateUserRequest.getUsername());
        existingUser.setFirstName(updateUserRequest.getFirstName());
        existingUser.setLastName(updateUserRequest.getLastName());
        existingUser.setEmail(updateUserRequest.getEmail());
        
        User updatedUser = userService.updateUser(existingUser);
        return ResponseEntity.ok(convertToUserResponse(updatedUser));
    }

    /**
     * PATCH /api/users/{id}/active : Enable or disable a user
     *
     * @param id the id of the user to update
     * @param active the active status
     * @return the ResponseEntity with status 200 (OK)
     */
    @PatchMapping("/{id}/active")
    @Operation(summary = "Enable or disable a user", description = "Updates a user's active status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User status successfully updated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> updateUserActiveStatus(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Active status", required = true)
            @RequestParam boolean active) {
        
        userService.setUserActiveStatus(id, active);
        return ResponseEntity.ok().build();
    }

    /**
     * PATCH /api/users/{id}/password : Change user password
     *
     * @param id the id of the user
     * @param changePasswordRequest the change password request containing the new password
     * @return the ResponseEntity with status 200 (OK)
     */
    @PatchMapping("/{id}/password")
    @Operation(summary = "Change a user's password", description = "Updates a user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "New password", required = true)
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        
        userService.changePassword(id, changePasswordRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/users/{id} : Delete a user
     *
     * @param id the id of the user to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a user", description = "Deletes a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convert User entity to UserResponse DTO
     *
     * @param user the user entity
     * @return the user response DTO
     */
    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setActive(user.isActive());
        response.setCreatedDate(user.getCreatedDate());
        response.setLastModifiedDate(user.getLastModifiedDate());
        return response;
    }

    /**
     * Convert CreateUserRequest DTO to User entity
     *
     * @param createUserRequest the create user request DTO
     * @return the user entity
     */
    private User convertToUserEntity(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setPassword(createUserRequest.getPassword());
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setEmail(createUserRequest.getEmail());
        user.setActive(true);
        return user;
    }
}
