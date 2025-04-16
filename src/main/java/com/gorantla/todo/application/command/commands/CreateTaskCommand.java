package com.gorantla.todo.application.command.commands;

import com.gorantla.todo.domain.entity.Task;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CreateTaskCommand {
    
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @NotNull(message = "Priority is required")
    private Task.Priority priority;
    
    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;
    
    @NotNull(message = "Algorithm type is required")
    private Task.AlgorithmType algorithmType;

    // Default constructor
    public CreateTaskCommand() {
    }

    // Constructor with all fields
    public CreateTaskCommand(String title, String description, Task.Priority priority, 
                             LocalDateTime dueDate, Task.AlgorithmType algorithmType) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.algorithmType = algorithmType;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task.Priority getPriority() {
        return priority;
    }

    public void setPriority(Task.Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Task.AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(Task.AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
}
