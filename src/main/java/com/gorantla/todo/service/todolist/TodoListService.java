package com.gorantla.todo.service.todolist;

import com.gorantla.todo.domain.todolist.TodoList;
import com.gorantla.todo.domain.todolist.repository.TodoListRepository;
import com.gorantla.todo.domain.user.User;
import com.gorantla.todo.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service class for managing TodoList entities
 * Implements business logic and transaction boundaries for todo list operations
 */
@Service
@Transactional(readOnly = true)
public class TodoListService {

    private final TodoListRepository todoListRepository;
    private final UserRepository userRepository;

    @Autowired
    public TodoListService(TodoListRepository todoListRepository, UserRepository userRepository) {
        this.todoListRepository = todoListRepository;
        this.userRepository = userRepository;
    }

    /**
     * Find a todo list by ID
     *
     * @param id the todo list ID
     * @return an Optional containing the todo list if found
     */
    public Optional<TodoList> findById(Long id) {
        return todoListRepository.findById(id);
    }

    /**
     * Find a todo list by ID and validate owner
     *
     * @param id the todo list ID
     * @param userId the owner user ID
     * @return the todo list if found and owned by the user
     * @throws NoSuchElementException if the todo list is not found or not owned by the user
     */
    public TodoList findByIdAndUser(Long id, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        return todoListRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + id + " for user ID: " + userId));
    }

    /**
     * Find all todo lists owned by a user
     *
     * @param userId the owner user ID
     * @return a list of todo lists owned by the user
     */
    public List<TodoList> findAllByUser(Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        return todoListRepository.findByOwner(owner);
    }

    /**
     * Find all todo lists owned by a user with pagination
     *
     * @param userId the owner user ID
     * @param pageable pagination information
     * @return a page of todo lists owned by the user
     */
    public Page<TodoList> findAllByUser(Long userId, Pageable pageable) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        return todoListRepository.findByOwner(owner, pageable);
    }

    /**
     * Find todo lists by title search term and owner
     *
     * @param titlePart the search term for the title
     * @param userId the owner user ID
     * @return a list of matching todo lists
     */
    public List<TodoList> findByTitleContaining(String titlePart, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        return todoListRepository.findByTitleContainingIgnoreCaseAndOwner(titlePart, owner);
    }

    /**
     * Find todo lists ordered by completion percentage
     *
     * @param userId the owner user ID
     * @param pageable pagination information
     * @return a page of todo lists sorted by completion percentage
     */
    public Page<TodoList> findByCompletionPercentage(Long userId, Pageable pageable) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        return todoListRepository.findByOwnerOrderByCompletionPercentageDesc(owner, pageable);
    }

    /**
     * Count the number of todo lists owned by a user
     *
     * @param userId the owner user ID
     * @return the count of todo lists
     */
    public long countByUser(Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        return todoListRepository.countByOwner(owner);
    }

    /**
     * Create a new todo list
     *
     * @param todoList the todo list to create
     * @param userId the owner user ID
     * @return the saved todo list entity
     */
    @Transactional
    public TodoList createTodoList(TodoList todoList, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        todoList.setOwner(owner);
        return todoListRepository.save(todoList);
    }

    /**
     * Update an existing todo list
     *
     * @param todoList the todo list data to update
     * @param userId the owner user ID
     * @return the updated todo list entity
     * @throws NoSuchElementException if the todo list is not found or not owned by the user
     */
    @Transactional
    public TodoList updateTodoList(TodoList todoList, Long userId) {
        // Verify ownership
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList existingList = todoListRepository.findByIdAndOwner(todoList.getId(), owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoList.getId() + " for user ID: " + userId));
        
        // Update fields
        existingList.setTitle(todoList.getTitle());
        existingList.setDescription(todoList.getDescription());
        
        return todoListRepository.save(existingList);
    }

    /**
     * Delete a todo list
     *
     * @param todoListId the todo list ID
     * @param userId the owner user ID
     * @throws NoSuchElementException if the todo list is not found or not owned by the user
     */
    @Transactional
    public void deleteTodoList(Long todoListId, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        TodoList todoList = todoListRepository.findByIdAndOwner(todoListId, owner)
                .orElseThrow(() -> new NoSuchElementException("Todo list not found with ID: " + todoListId + " for user ID: " + userId));
        
        todoListRepository.delete(todoList);
    }

    /**
     * Delete all todo lists owned by a user
     *
     * @param userId the owner user ID
     */
    @Transactional
    public void deleteAllByUser(Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        
        todoListRepository.deleteByOwner(owner);
    }
}
