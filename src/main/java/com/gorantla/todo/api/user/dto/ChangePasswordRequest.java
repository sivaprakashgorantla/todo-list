package com.gorantla.todo.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for changing a user's password
 */
public class ChangePasswordRequest {

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 6, max = 120, message = "Password must be between 6 and 120 characters")
    @Schema(description = "New password", example = "newPassword123", required = true)
    private String newPassword;

    // Getters and Setters
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
