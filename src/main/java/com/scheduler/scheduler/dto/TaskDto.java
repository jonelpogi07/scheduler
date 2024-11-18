package com.scheduler.scheduler.dto;

import com.scheduler.scheduler.entity.TaskEntity;

import java.time.LocalDateTime;

public class TaskDto {

    private Long id;
    private String name;
    private LocalDateTime creationDate = LocalDateTime.now(); // Default to current time
    private LocalDateTime startDate; // Changed to LocalDateTime
    private LocalDateTime endDate;   // Changed to LocalDateTime
    private int status;
    private String dependencies;

    private int duration;

    // Default constructor
    public TaskDto() {
    }

    // Constructor to map from TaskEntity to TaskDto
    public TaskDto(TaskEntity taskEntity) {
        this.id = taskEntity.getId();
        this.name = taskEntity.getName();
        this.creationDate = taskEntity.getCreationDate();
        this.startDate = taskEntity.getStartDate();  // Assuming TaskEntity has LocalDateTime for startDate
        this.endDate = taskEntity.getEndDate();      // Assuming TaskEntity has LocalDateTime for endDate
        this.status = taskEntity.getStatus();
        this.dependencies = taskEntity.getDependencies();
        this.duration = taskEntity.getDuration();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate != null ? creationDate : LocalDateTime.now();
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDependencies() {
        return dependencies;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
