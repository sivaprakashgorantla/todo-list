package com.gorantla.todo.domain.todolist.repository;

import com.gorantla.todo.domain.todolist.TaskPriority;
import com.gorantla.todo.domain.todolist.TodoList;
import com.gorantla.todo.domain.todolist.TodoTask;
import com.gorantla.todo.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for TodoTask entity
 * Provides standard CRUD operations and custom finder methods
 */
@Repository
public interface TodoTaskRepository extends JpaRepository<TodoTask, Long> {
    
    /**
     * Find all tasks belonging to a specific todo list
     * 
     * @param todoList the todo list containing the tasks
     * @return a list of tasks in the todo list
     */
    List<TodoTask> findByTodoList(TodoList todoList);
    
    /**
     * Find all tasks belonging to a specific todo list, with pagination
     * 
     * @param todoList the todo list containing the tasks
     * @param pageable pagination information
     * @return a page of tasks in the todo list
     */
    Page<TodoTask> findByTodoList(TodoList todoList, Pageable pageable);
    
    /**
     * Find tasks by their completion status within a todo list
     * 
     * @param todoList the todo list containing the tasks
     * @param completed the completion status to filter by
     * @return a list of tasks with the specified completion status
     */
    List<TodoTask> findByTodoListAndCompleted(TodoList todoList, boolean completed);
    
    /**
     * Find tasks by priority within a todo list
     * 
     * @param todoList the todo list containing the tasks
     * @param priority the priority to filter by
     * @return a list of tasks with the specified priority
     */
    List<TodoTask> findByTodoListAndPriority(TodoList todoList, TaskPriority priority);
    
    /**
     * Find tasks due before a specific date within a todo list
     * 
     * @param todoList the todo list containing the tasks
     * @param dueDate the due date to compare against
     * @return a list of tasks due before the specified date
     */
    List<TodoTask> findByTodoListAndDueDateBefore(TodoList todoList, LocalDateTime dueDate);
    
    /**
     * Find overdue tasks (due date in the past and not completed) within a todo list
     * 
     * @param todoList the todo list containing the tasks
     * @param now the current date/time to compare against
     * @return a list of overdue tasks
     */
    List<TodoTask> findByTodoListAndCompletedFalseAndDueDateBefore(TodoList todoList, LocalDateTime now);
    
    /**
     * Find tasks in todo lists owned by a specific user
     * 
     * @param owner the user who owns the todo lists
     * @return a list of tasks in todo lists owned by the user
     */
    @Query("SELECT t FROM TodoTask t WHERE t.todoList.owner = :owner")
    List<TodoTask> findByOwner(@Param("owner") User owner);
    
    /**
     * Find a task by its ID and todo list
     * 
     * @param id the ID of the task
     * @param todoList the todo list containing the task
     * @return an Optional containing the task if found, empty otherwise
     */
    Optional<TodoTask> findByIdAndTodoList(Long id, TodoList todoList);
    
    /**
     * Find tasks by title containing the search term (case-insensitive) within a todo list
     * 
     * @param titlePart the search term to look for in the title
     * @param todoList the todo list containing the tasks
     * @return a list of matching tasks
     */
    List<TodoTask> findByTitleContainingIgnoreCaseAndTodoList(String titlePart, TodoList todoList);
    
    /**
     * Count the number of tasks in a todo list
     * 
     * @param todoList the todo list containing the tasks
     * @return the count of tasks
     */
    long countByTodoList(TodoList todoList);
    
    /**
     * Count the number of completed tasks in a todo list
     * 
     * @param todoList the todo list containing the tasks
     * @return the count of completed tasks
     */
    long countByTodoListAndCompleted(TodoList todoList, boolean completed);
    
    /**
     * Delete all tasks in a todo list
     * 
     * @param todoList the todo list containing the tasks
     */
    void deleteByTodoList(TodoList todoList);
}
