package com.gorantla.todo.domain.todolist.repository;

import com.gorantla.todo.domain.todolist.TodoList;
import com.gorantla.todo.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for TodoList entity
 * Provides standard CRUD operations and custom finder methods
 */
@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    
    /**
     * Find all todo lists owned by a specific user
     * 
     * @param owner the user who owns the lists
     * @return a list of todo lists owned by the user
     */
    List<TodoList> findByOwner(User owner);
    
    /**
     * Find all todo lists owned by a specific user, with pagination
     * 
     * @param owner the user who owns the lists
     * @param pageable pagination information
     * @return a page of todo lists owned by the user
     */
    Page<TodoList> findByOwner(User owner, Pageable pageable);
    
    /**
     * Find a todo list by its ID and owner
     * 
     * @param id the ID of the todo list
     * @param owner the owner of the todo list
     * @return an Optional containing the todo list if found, empty otherwise
     */
    Optional<TodoList> findByIdAndOwner(Long id, User owner);
    
    /**
     * Find todo lists by title containing the search term (case-insensitive)
     * 
     * @param titlePart the search term to look for in the title
     * @param owner the owner of the lists
     * @return a list of matching todo lists
     */
    List<TodoList> findByTitleContainingIgnoreCaseAndOwner(String titlePart, User owner);
    
    /**
     * Count the number of todo lists owned by a specific user
     * 
     * @param owner the user who owns the lists
     * @return the count of todo lists
     */
    long countByOwner(User owner);
    
    /**
     * Find todo lists with the highest completion percentage
     * 
     * @param owner the user who owns the lists
     * @param pageable pagination information
     * @return a page of todo lists sorted by completion percentage
     */
    @Query("SELECT t FROM TodoList t JOIN t.tasks tasks " +
           "WHERE t.owner = :owner " +
           "GROUP BY t " +
           "ORDER BY SUM(CASE WHEN tasks.completed = true THEN 1 ELSE 0 END) * 100.0 / COUNT(tasks) DESC")
    Page<TodoList> findByOwnerOrderByCompletionPercentageDesc(@Param("owner") User owner, Pageable pageable);
    
    /**
     * Delete all todo lists owned by a specific user
     * 
     * @param owner the user who owns the lists
     */
    void deleteByOwner(User owner);
}
