package com.gorantla.todo.domain.repository;

import com.gorantla.todo.domain.entity.Task;
import com.gorantla.todo.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Page<Task> findByCreator(User creator, Pageable pageable);
    Page<Task> findByAssignedUser(User assignedUser, Pageable pageable);
    Page<Task> findByPriority(Task.Priority priority, Pageable pageable);
    Page<Task> findByDueDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    List<Task> findByAssignedUserAndPriority(User assignedUser, Task.Priority priority);
}
