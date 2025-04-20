package com.gorantla.todo.domain.todolist;

import com.gorantla.todo.domain.common.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * TodoTask entity representing a task in a todo list
 * This entity belongs to the TodoList aggregate
 */
@Entity
@Table(name = "todo_tasks")
public class TodoTask extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(min = 1, max = 200)
    @Column(name = "title", nullable = false)
    private String title;
    
    @Size(max = 1000)
    @Column(name = "description")
    private String description;
    
    @Column(name = "completed", nullable = false)
    private boolean completed = false;
    
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority = TaskPriority.MEDIUM;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "todo_list_id", nullable = false)
    private TodoList todoList;

    // Constructors
    public TodoTask() {
    }

    public TodoTask(String title, TodoList todoList) {
        this.title = title;
        this.todoList = todoList;
    }

    public TodoTask(String title, String description, TodoList todoList) {
        this.title = title;
        this.description = description;
        this.todoList = todoList;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }

    // Domain methods
    public void markAsCompleted() {
        this.completed = true;
    }
    
    public void markAsPending() {
        this.completed = false;
    }

    public boolean isOverdue() {
        return !completed && dueDate != null && dueDate.isBefore(LocalDateTime.now());
    }
    
    public void toggleCompletion() {
        this.completed = !this.completed;
    }
    
    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        TodoTask todoTask = (TodoTask) o;
        return id != null && id.equals(todoTask.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
