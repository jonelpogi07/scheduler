package com.scheduler.scheduler.service;

import com.scheduler.scheduler.dto.TaskDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    TaskDto addTask(TaskDto task);

    TaskDto updateTaskStatus(TaskDto task);

    List<TaskDto> batchAddTask(List<TaskDto> task);

    List<TaskDto> getTaskById();
}
