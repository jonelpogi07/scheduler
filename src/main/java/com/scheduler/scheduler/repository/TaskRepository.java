package com.scheduler.scheduler.repository;

import com.scheduler.scheduler.dto.TaskDto;
import com.scheduler.scheduler.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    boolean existsByName(String name);
}
