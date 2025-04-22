package com.gorantla.todo.api.todolist;

import com.gorantla.todo.api.todolist.dto.TodoListRequestDto;
import com.gorantla.todo.api.todolist.dto.TodoListResponseDto;
import com.gorantla.todo.api.todolist.mapper.TodoListMapper;
import com.gorantla.todo.api.util.ApiResponse;
import com.gorantla.todo.api.util.PaginatedResponse;
import com.gorantla.todo.domain.todolist.TodoList;
import com.gorantla.todo.service.todolist.TodoListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST controller for managing TodoList resources
 */
@RestController
@RequestMapping("/api/v1/users/{userId}/todolists")
public class TodoListController {

    private final TodoListService todoListService;
    private final TodoListMapper todoListMapper;

    @Autowired
    public TodoListController(TodoListService todoListService, TodoListMapper todoListMapper) {
        this.todoListService = todoListService;
        this.todoListMapper = todoListMapper;
    }

    /**
     * GET /api/v1/users/{userId}/todolists : Get all todo lists for a user
     *
     * @param userId the ID of the user
     * @param page page number (optional, default 0)
     * @param size page size (optional, default 10)
     * @param sort sort field (optional, default "createdAt")
     * @param direction sort direction (optional, default "DESC")
     * @return the list of todo lists
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<TodoListResponseDto>>> getAllTodoLists(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
        
        Page<TodoList> todoListPage = todoListService.findAllByUser(userId, pageable);
        Page<TodoListResponseDto> dtoPage = todoListPage.map(todoListMapper::toDto);
        
        PaginatedResponse<TodoListResponseDto> response = PaginatedResponse.from(dtoPage);
        return ResponseEntity.ok(ApiResponse.success("Todo lists retrieved successfully", response));
    }

    /**
     * GET /api/v1/users/{userId}/todolists/{id} : Get a specific todo list
     *
     * @param userId the ID of the user
     * @param id the ID of the todo list
     * @return the todo list
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoListResponseDto>> getTodoList(
            @PathVariable Long userId,
            @PathVariable Long id) {
        
        try {
            TodoList todoList = todoListService.findByIdAndUser(id, userId);
            TodoListResponseDto dto = todoListMapper.toDto(todoList);
            return ResponseEntity.ok(ApiResponse.success("Todo list retrieved successfully", dto));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * POST /api/v1/users/{userId}/todolists : Create a new todo list
     *
     * @param userId the ID of the user
     * @param requestDto the todo list to create
     * @return the created todo list
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TodoListResponseDto>> createTodoList(
            @PathVariable Long userId,
            @Valid @RequestBody TodoListRequestDto requestDto) {
        
        TodoList todoList = todoListMapper.toEntity(requestDto);
        TodoList savedTodoList = todoListService.createTodoList(todoList, userId);
        TodoListResponseDto responseDto = todoListMapper.toDto(savedTodoList);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Todo list created successfully", responseDto));
    }

    /**
     * PUT /api/v1/users/{userId}/todolists/{id} : Update a todo list
     *
     * @param userId the ID of the user
     * @param id the ID of the todo list
     * @param requestDto the updated todo list
     * @return the updated todo list
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoListResponseDto>> updateTodoList(
            @PathVariable Long userId,
            @PathVariable Long id,
            @Valid @RequestBody TodoListRequestDto requestDto) {
        
        try {
            TodoList existingTodoList = todoListService.findByIdAndUser(id, userId);
            todoListMapper.updateEntity(existingTodoList, requestDto);
            
            TodoList updatedTodoList = todoListService.updateTodoList(existingTodoList, userId);
            TodoListResponseDto responseDto = todoListMapper.toDto(updatedTodoList);
            
            return ResponseEntity.ok(ApiResponse.success("Todo list updated successfully", responseDto));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * DELETE /api/v1/users/{userId}/todolists/{id} : Delete a todo list
     *
     * @param userId the ID of the user
     * @param id the ID of the todo list
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodoList(
            @PathVariable Long userId,
            @PathVariable Long id) {
        
        try {
            todoListService.deleteTodoList(id, userId);
            return ResponseEntity.ok(ApiResponse.success("Todo list deleted successfully"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * DELETE /api/v1/users/{userId}/todolists : Delete all todo lists for a user
     *
     * @param userId the ID of the user
     * @return no content
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteAllTodoLists(@PathVariable Long userId) {
        todoListService.deleteAllByUser(userId);
        return ResponseEntity.ok(ApiResponse.success("All todo lists deleted successfully"));
    }

    /**
     * GET /api/v1/users/{userId}/todolists/search : Search todo lists by title
     *
     * @param userId the ID of the user
     * @param title the title search term
     * @return the list of matching todo lists
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TodoListResponseDto>>> searchTodoLists(
            @PathVariable Long userId,
            @RequestParam String title) {
        
        List<TodoList> todoLists = todoListService.findByTitleContaining(title, userId);
        List<TodoListResponseDto> dtoList = todoListMapper.toDtoList(todoLists);
        
        return ResponseEntity.ok(ApiResponse.success("Todo lists retrieved successfully", dtoList));
    }

    /**
     * GET /api/v1/users/{userId}/todolists/progress : Get todo lists ordered by completion percentage
     *
     * @param userId the ID of the user
     * @param page page number (optional, default 0)
     * @param size page size (optional, default 10)
     * @return the list of todo lists ordered by completion percentage
     */
    @GetMapping("/progress")
    public ResponseEntity<ApiResponse<PaginatedResponse<TodoListResponseDto>>> getTodoListsByProgress(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TodoList> todoListPage = todoListService.findByCompletionPercentage(userId, pageable);
        Page<TodoListResponseDto> dtoPage = todoListPage.map(todoListMapper::toDto);
        
        PaginatedResponse<TodoListResponseDto> response = PaginatedResponse.from(dtoPage);
        return ResponseEntity.ok(ApiResponse.success("Todo lists retrieved successfully", response));
    }

    /**
     * GET /api/v1/users/{userId}/todolists/count : Get the count of todo lists for a user
     *
     * @param userId the ID of the user
     * @return the count of todo lists
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countTodoLists(@PathVariable Long userId) {
        long count = todoListService.countByUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Todo list count retrieved successfully", count));
    }
}
