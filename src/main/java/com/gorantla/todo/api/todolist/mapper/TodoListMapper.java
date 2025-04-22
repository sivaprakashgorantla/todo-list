package com.gorantla.todo.api.todolist.mapper;

import com.gorantla.todo.api.todolist.dto.TodoListRequestDto;
import com.gorantla.todo.api.todolist.dto.TodoListResponseDto;
import com.gorantla.todo.domain.todolist.TodoList;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between TodoList entities and DTOs
 */
@Component
public class TodoListMapper {

    /**
     * Convert a TodoList entity to a response DTO
     *
     * @param todoList the entity to convert
     * @return the corresponding DTO
     */
    public TodoListResponseDto toDto(TodoList todoList) {
        return TodoListResponseDto.fromEntity(todoList);
    }

    /**
     * Convert a list of TodoList entities to response DTOs
     *
     * @param todoLists the list of entities to convert
     * @return the list of corresponding DTOs
     */
    public List<TodoListResponseDto> toDtoList(List<TodoList> todoLists) {
        return todoLists.stream()
                .map(TodoListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Create a new TodoList entity from a request DTO
     *
     * @param dto the request DTO
     * @return a new TodoList entity
     */
    public TodoList toEntity(TodoListRequestDto dto) {
        TodoList todoList = new TodoList();
        todoList.setTitle(dto.getTitle());
        todoList.setDescription(dto.getDescription());
        return todoList;
    }

    /**
     * Update an existing TodoList entity from a request DTO
     *
     * @param todoList the entity to update
     * @param dto the request DTO containing updated values
     * @return the updated entity
     */
    public TodoList updateEntity(TodoList todoList, TodoListRequestDto dto) {
        todoList.setTitle(dto.getTitle());
        todoList.setDescription(dto.getDescription());
        return todoList;
    }
}
