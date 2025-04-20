package com.gorantla.todo.domain.todolist;

import com.gorantla.todo.domain.common.AggregateRoot;
import com.gorantla.todo.domain.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * TodoList entity representing a list of todo tasks
 * This is an aggregate root that contains TodoTask entities
 */
@Entity
@Table(name = "todo_lists")
public class TodoList extends AggregateRoot {

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 500)
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @OneToMany(
        mappedBy = "todoList",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @OrderBy("priority ASC, dueDate ASC")
    private List<TodoTask> tasks = new ArrayList<>();

    // Constructors
    public TodoList() {
    }

    public TodoList(String title, User owner) {
        this.title = title;
        this.owner = owner;
    }

    // Getters and Setters
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<TodoTask> getTasks() {
        return tasks;
    }

    // Domain methods for managing tasks within the aggregate
    public void addTask(TodoTask task) {
        tasks.add(task);
        task.setTodoList(this);
    }

    public void removeTask(TodoTask task) {
        tasks.remove(task);
        task.setTodoList(null);
    }

    public TodoTask findTaskById(Long taskId) {
        return tasks.stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElse(null);
    }

    public long getCompletedTasksCount() {
        return tasks.stream()
                .filter(TodoTask::isCompleted)
                .count();
    }

    public List<TodoTask> getCompletedTasks() {
        return tasks.stream()
                .filter(TodoTask::isCompleted)
                .toList();
    }

    public List<TodoTask> getPendingTasks() {
        return tasks.stream()
                .filter(task -> !task.isCompleted())
                .toList();
    }

    public long getProgressPercentage() {
        if (tasks.isEmpty()) {
            return 0;
        }
        return (getCompletedTasksCount() * 100) / tasks.size();
    }
}
