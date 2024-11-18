package com.scheduler.scheduler.controller;

import com.scheduler.scheduler.dto.TaskDto;
import com.scheduler.scheduler.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(value= "/addTask")
    public TaskDto addTask(@RequestBody TaskDto taskDto) {
        return taskService.addTask(taskDto);
    }

    @PostMapping(value= "/updateTaskStatus")
    public TaskDto updateTaskStatus(@RequestBody TaskDto taskDto) {
        return taskService.updateTaskStatus(taskDto);
    }

    @PostMapping(value= "/batchAddTask")
    public List<TaskDto> batchAddTask(@RequestBody List<TaskDto> taskDto) {
        return taskService.batchAddTask(taskDto);
    }

    @GetMapping("/showTasks")
    public List<TaskDto> getTaskById() {
        return taskService.getTaskById();
    }
}
