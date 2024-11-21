package com.scheduler.scheduler.repository;

import com.scheduler.scheduler.dto.TaskDto;

public interface TaskRepositoryCustom {
    TaskDto findTaskByNameAndStatus(String name, int status);
}
