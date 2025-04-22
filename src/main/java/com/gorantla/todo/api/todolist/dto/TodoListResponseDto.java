package com.gorantla.todo.api.todolist.dto;

import com.gorantla.todo.domain.todolist.TodoList;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for TodoList responses
 */
public class TodoListResponseDto {

    private Long id;
    private String title;
    private String description;
    private Long ownerId;
    private String ownerUsername;
    private int taskCount;
    private int completedTaskCount;
    private int progressPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public TodoListResponseDto() {
    }

    // Factory method to convert from entity to DTO
    public static TodoListResponseDto fromEntity(TodoList todoList) {
        TodoListResponseDto dto = new TodoListResponseDto();
        dto.setId(todoList.getId());
        dto.setTitle(todoList.getTitle());
        dto.setDescription(todoList.getDescription());
        
        if (todoList.getOwner() != null) {
            dto.setOwnerId(todoList.getOwner().getId());
            dto.setOwnerUsername(todoList.getOwner().getUsername());
        }
        
        dto.setTaskCount(todoList.getTasks().size());
        dto.setCompletedTaskCount((int) todoList.getCompletedTasksCount());
        dto.setProgressPercentage((int) todoList.getProgressPercentage());
        dto.setCreatedAt(todoList.getCreatedAt());
        dto.setUpdatedAt(todoList.getUpdatedAt());
        
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public int getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(int completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
