package com.gorantla.todo.service.todolist;

import com.gorantla.todo.domain.todolist.TaskPriority;
import com.gorantla.todo.domain.todolist.TodoList;
import com.gorantla.todo.domain.todolist.TodoTask;
import com.gorantla.todo.domain.todolist.repository.TodoListRepository;
import com.gorantla.todo.domain.todolist.repository.TodoTaskRepository;
import com.gorantla.todo.domain.user.User;
import com.gorantla.todo.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service class for managing TodoTask entities
 * Implements business logic and transaction boundaries for todo task operations
 */
@Service
@Transactional(readOnly = true)
public class TodoTaskService {

    private final TodoTaskRepository todoTaskRepository;
    private final TodoListRepository todoListRepository;
    private final UserRepository userRepository;

    @Autowired
    public TodoTaskService(TodoTaskRepository todoTaskRepository, TodoListRepository todoListRepository, UserRepository userRepository) {
        this.todoTaskRepository = todoTaskRepository;
        this.todoListRepository = todoListRepository;
        this.userRepository = userRepository;
    }

    /**
     * Find a task by ID
     *
     * @param id the task ID
     * @return an Optional containing the task if found
     */
    public Optional<TodoTask> findById(Long id) {
        return todoTaskRepository.findById(id);
    }

    /**
     * Find a task by ID and validate ownership
     *
     * @param taskId the task ID
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @return the task if found and accessible by the user
     * @throws NoSuchElementException if the task is not found or not accessible by the user
     */
    public TodoTask findByIdAndTodoListAndUser(Long taskId, Long todoListId, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        return todoTaskRepository.findByIdAndTodoList(taskId, todoList)
                .orElseThrow(() -> new NoSuchElementException("Task not found with ID: " + taskId + " in todo list ID: " + todoListId));
    }

    /**
     * Find all tasks in a todo list
     *
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @return a list of tasks in the todo list
     */
    public List<TodoTask> findAllByTodoList(Long todoListId, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        return todoTaskRepository.findByTodoList(todoList);
    }

    /**
     * Find all tasks in a todo list with pagination
     *
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @param pageable pagination information
     * @return a page of tasks in the todo list
     */
    public Page<TodoTask> findAllByTodoList(Long todoListId, Long userId, Pageable pageable) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        return todoTaskRepository.findByTodoList(todoList, pageable);
    }

    /**
     * Find tasks by completion status in a todo list
     *
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @param completed the completion status
     * @return a list of tasks with the specified completion status
     */
    public List<TodoTask> findByCompletionStatus(Long todoListId, Long userId, boolean completed) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        return todoTaskRepository.findByTodoListAndCompleted(todoList, completed);
    }

    /**
     * Find tasks by priority in a todo list
     *
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @param priority the task priority
     * @return a list of tasks with the specified priority
     */
    public List<TodoTask> findByPriority(Long todoListId, Long userId, TaskPriority priority) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        return todoTaskRepository.findByTodoListAndPriority(todoList, priority);
    }

    /**
     * Find overdue tasks in a todo list
     *
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @return a list of overdue tasks
     */
    public List<TodoTask> findOverdueTasks(Long todoListId, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        return todoTaskRepository.findByTodoListAndCompletedFalseAndDueDateBefore(todoList, LocalDateTime.now());
    }

    /**
     * Find tasks by title search term in a todo list
     *
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @param titlePart the search term for the title
     * @return a list of matching tasks
     */
    public List<TodoTask> findByTitleContaining(Long todoListId, Long userId, String titlePart) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        return todoTaskRepository.findByTitleContainingIgnoreCaseAndTodoList(titlePart, todoList);
    }

    /**
     * Find all tasks belonging to the user across all their todo lists
     *
     * @param userId the user ID
     * @return a list of all tasks owned by the user
     */
    public List<TodoTask> findAllTasksByUser(Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        return todoTaskRepository.findByOwner(owner);
    }

    /**
     * Count the number of tasks in a todo list
     *
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @return the count of tasks
     */
    public long countTasksInTodoList(Long todoListId, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        return todoTaskRepository.countByTodoList(todoList);
    }

    /**
     * Count the number of completed tasks in a todo list
     *
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @return the count of completed tasks
     */
    public long countCompletedTasksInTodoList(Long todoListId, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        return todoTaskRepository.countByTodoListAndCompleted(todoList, true);
    }

    /**
     * Create a new task in a todo list
     *
     * @param task the task to create
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @return the saved task entity
     */
    @Transactional
    public TodoTask createTask(TodoTask task, Long todoListId, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        task.setTodoList(todoList);
        return todoTaskRepository.save(task);
    }

    /**
     * Update an existing task
     *
     * @param task the task data to update
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @return the updated task entity
     * @throws NoSuchElementException if the task is not found or not accessible by the user
     */
    @Transactional
    public TodoTask updateTask(TodoTask task, Long todoListId, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        TodoTask existingTask = todoTaskRepository.findByIdAndTodoList(task.getId(), todoList)
                .orElseThrow(() -> new NoSuchElementException("Task not found with ID: " + task.getId() + " in todo list ID: " + todoListId));
        
        // Update fields
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setCompleted(task.isCompleted());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setPriority(task.getPriority());
        
        return todoTaskRepository.save(existingTask);
    }

    /**
     * Toggle the completion status of a task
     *
     * @param taskId the task ID
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @return the updated task entity
     */
    @Transactional
    public TodoTask toggleTaskCompletion(Long taskId, Long todoListId, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        TodoTask task = todoTaskRepository.findByIdAndTodoList(taskId, todoList)
                .orElseThrow(() -> new NoSuchElementException("Task not found with ID: " + taskId + " in todo list ID: " + todoListId));
        
        task.toggleCompletion();
        return todoTaskRepository.save(task);
    }

    /**
     * Delete a task
     *
     * @param taskId the task ID
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @throws NoSuchElementException if the task is not found or not accessible by the user
     */
    @Transactional
    public void deleteTask(Long taskId, Long todoListId, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        TodoTask task = todoTaskRepository.findByIdAndTodoList(taskId, todoList)
                .orElseThrow(() -> new NoSuchElementException("Task not found with ID: " + taskId + " in todo list ID: " + todoListId));
        
        todoTaskRepository.delete(task);
    }

    /**
     * Delete all tasks in a todo list
     *
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     */
    @Transactional
    public void deleteAllTasksInTodoList(Long todoListId, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        todoTaskRepository.deleteByTodoList(todoList);
    }

    /**
     * Update task priority
     *
     * @param taskId the task ID
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @param priority the new priority
     * @return the updated task entity
     */
    @Transactional
    public TodoTask updateTaskPriority(Long taskId, Long todoListId, Long userId, TaskPriority priority) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        TodoTask task = todoTaskRepository.findByIdAndTodoList(taskId, todoList)
                .orElseThrow(() -> new NoSuchElementException("Task not found with ID: " + taskId + " in todo list ID: " + todoListId));
        
        task.setPriority(priority);
        return todoTaskRepository.save(task);
    }

    /**
     * Update task due date
     *
     * @param taskId the task ID
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @param dueDate the new due date
     * @return the updated task entity
     */
    @Transactional
    public TodoTask updateTaskDueDate(Long taskId, Long todoListId, Long userId, LocalDateTime dueDate) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        TodoTask task = todoTaskRepository.findByIdAndTodoList(taskId, todoList)
                .orElseThrow(() -> new NoSuchElementException("Task not found with ID: " + taskId + " in todo list ID: " + todoListId));
        
        task.setDueDate(dueDate);
        return todoTaskRepository.save(task);
    }
}
