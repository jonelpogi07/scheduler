package com.scheduler.scheduler.impl;
import com.scheduler.scheduler.dto.TaskDto;
import com.scheduler.scheduler.entity.TaskEntity;
import com.scheduler.scheduler.enums.Enums;
import com.scheduler.scheduler.exceptionEnums.ExceptionEnums;
import com.scheduler.scheduler.exceptions.TaskExceptions;
import com.scheduler.scheduler.repository.TaskRepository;
import com.scheduler.scheduler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TaskImpl implements TaskService {
    private final TaskRepository taskRepository;
    @Autowired
    public TaskImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskDto addTask(TaskDto task) {
        // Check if the task name already exists in the database
        if (taskRepository.existsByName(task.getName())) {
            throw new TaskExceptions(ExceptionEnums.ErrorCode.TASK_NAME_EXIST.getMessage());
        }

        // If task has dependencies, validate and set start date based on the latest dependency end date
        if (!task.getDependencies().isEmpty()) {
            String[] dependenciesIds = task.getDependencies().split(",");
            LocalDateTime latestEndDate = LocalDateTime.MIN;  // Initialize to the smallest possible date-time

            // Loop through each dependency ID and find the latest end date
            for (String dependencyId : dependenciesIds) {
                Long dependencyIdLong = Long.valueOf(dependencyId);
                Optional<TaskEntity> dependencyTask = taskRepository.findById(dependencyIdLong);
                if (!dependencyTask.isPresent()) {
                    throw new TaskExceptions("Dependency task with ID " + dependencyId + " not found");
                }

                TaskEntity dependency = dependencyTask.get();
                if (dependency.getEndDate() != null && dependency.getEndDate().isAfter(latestEndDate)) {
                    latestEndDate = dependency.getEndDate();  // Keep track of the latest endDate
                    // Set the start date to 1 minute after the latest dependency's end date
                    task.setStartDate(latestEndDate.plusMinutes(1));
                }
            }
        }

        // Convert TaskDto to TaskEntity and save the task
        TaskEntity taskTobeAdd = new TaskEntity();
        taskTobeAdd.setName(task.getName());
        taskTobeAdd.setCreationDate(task.getCreationDate() != null ? task.getCreationDate() : LocalDateTime.now()); // Use current time if null
        taskTobeAdd.setStartDate(task.getStartDate());  // Set the calculated or provided start date
        taskTobeAdd.setEndDate(task.getEndDate());  // Use the provided end date
        taskTobeAdd.setStatus(Enums.Code.NOT_STARTED.getStatus());  // Default status
        taskTobeAdd.setDependencies(task.getDependencies());
        taskTobeAdd.setDuration(task.getDuration());

        taskRepository.save(taskTobeAdd);

        // Set the ID of the task in the DTO and return it
        task.setId(taskTobeAdd.getId());
        return task;
    }

    @Override
    public TaskDto updateTaskStatus(TaskDto task) {
        // Fetch the existing task from the repository by ID
        TaskEntity taskToUpdate = taskRepository.findById(task.getId())
                .orElseThrow(() -> new TaskExceptions(ExceptionEnums.ErrorCode.TASK_CANNOT_FIND.getMessage()));

        // Check if there are dependencies and validate their status
        if (taskToUpdate.getDependencies() != null && !taskToUpdate.getDependencies().isEmpty()) {
            // Split the comma-separated dependencies ("1,2,9")
            String[] dependencyIds = taskToUpdate.getDependencies().split(",");

            // Loop through each dependency ID and check its status
            for (String depId : dependencyIds) {
                Long dependencyId = Long.parseLong(depId.trim());

                // Fetch the dependent task by ID
                TaskEntity dependencyTask = taskRepository.findById(dependencyId)
                        .orElseThrow(() -> new TaskExceptions("Dependency task with ID " + dependencyId + " not found"));

                // If any dependency has status other than 2, do not allow status update
                if (dependencyTask.getStatus() != Enums.Code.DONE.getStatus()) {
                    throw new TaskExceptions("Cannot update task status. Dependency task with ID " + dependencyId + " has status " + dependencyTask.getStatus());
                }
            }
        }

        // If all dependencies have status 2, update the status of the task
        taskToUpdate.setStatus(task.getStatus());
        if(task.getStatus()==Enums.Code.IN_PROGRESS.getStatus()){
            taskToUpdate.setStartDate(LocalDateTime.now());
        }
        if(task.getStatus()==Enums.Code.DONE.getStatus()){
            taskToUpdate.setEndDate(LocalDateTime.now());
        }
        taskRepository.save(taskToUpdate);

        // Return the updated TaskDto
        return new TaskDto(taskToUpdate);
    }

    @Override
    public List<TaskDto> batchAddTask(List<TaskDto> tasks) {
        // Validate and prepare task entities to be saved
        List<TaskEntity> taskEntities = new ArrayList<>();

        for (TaskDto task : tasks) {
            // Check if the task name already exists in the database
            if (taskRepository.existsByName(task.getName())) {
                throw new TaskExceptions("Task name " + task.getName() + " already exists");
            }

            // If there are dependencies, find the latest end date from the dependent tasks
            LocalDateTime taskStartDate;
            if (task.getDependencies() == null || task.getDependencies().isEmpty()) {
                // If no dependencies, start the task at the current time
                taskStartDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);  // Set to start of the day
            } else {
                // If there are dependencies, fetch the latest endDate from the dependent tasks
                String[] dependenciesIds = task.getDependencies().split(",");
                LocalDateTime latestDependencyEndDate = LocalDateTime.MIN;  // Initialize to the smallest possible time

                for (String dependencyId : dependenciesIds) {
                    Long dependencyIdLong = Long.valueOf(dependencyId);
                    Optional<TaskEntity> dependencyTask = taskRepository.findById(dependencyIdLong);
                    if (dependencyTask.isPresent()) {
                        TaskEntity dependency = dependencyTask.get();
                        if (dependency.getEndDate() != null && dependency.getEndDate().isAfter(latestDependencyEndDate)) {
                            latestDependencyEndDate = dependency.getEndDate();
                        }
                    } else {
                        throw new TaskExceptions("Dependency task with ID " + dependencyId + " not found");
                    }
                }

                // Set the start date to the day after the latest dependency end date
                taskStartDate = latestDependencyEndDate.plusMinutes(1);
            }

            // Set the endDate based on the duration (duration 1 = 1 day)
            LocalDateTime taskEndDate = taskStartDate.plusDays(task.getDuration());

            // Convert TaskDto to TaskEntity
            TaskEntity taskToAdd = new TaskEntity();
            taskToAdd.setName(task.getName());
            taskToAdd.setCreationDate(task.getCreationDate() != null ? task.getCreationDate() : LocalDateTime.now()); // Default to current time if null
            taskToAdd.setStartDate(taskStartDate);
            taskToAdd.setEndDate(taskEndDate);
            taskToAdd.setStatus(Enums.Code.NOT_STARTED.getStatus());  // Default status
            taskToAdd.setDependencies(task.getDependencies());  // Assuming this is handled as a comma-separated string
            taskToAdd.setDuration(task.getDuration());

            // Add TaskEntity to the list
            taskEntities.add(taskToAdd);
        }

        // Save all tasks in a batch
        List<TaskEntity> savedEntities = taskRepository.saveAll(taskEntities);

        // Convert saved entities back to TaskDto
        List<TaskDto> savedTasks = savedEntities.stream()
                .map(savedTask -> {
                    TaskDto savedTaskDto = new TaskDto();
                    savedTaskDto.setId(savedTask.getId());
                    savedTaskDto.setName(savedTask.getName());
                    savedTaskDto.setCreationDate(savedTask.getCreationDate());
                    savedTaskDto.setStartDate(savedTask.getStartDate());
                    savedTaskDto.setEndDate(savedTask.getEndDate());
                    savedTaskDto.setStatus(savedTask.getStatus());
                    savedTaskDto.setDependencies(savedTask.getDependencies());
                    savedTaskDto.setDuration(savedTask.getDuration());
                    return savedTaskDto;
                })
                .collect(Collectors.toList());

        // Return the list of TaskDto objects with assigned IDs
        return savedTasks;
    }


    @Override
    public List<TaskDto> getTaskById() {

        List<TaskEntity> taskEntities = taskRepository.findAll();  // all tasks.

        // Convert List<TaskEntity> to List<TaskDto>
        return taskEntities.stream()
                .map(task -> new TaskDto(task))// TaskDto has a constructor that takes TaskEntity
                .collect(Collectors.toList());
    }
}
