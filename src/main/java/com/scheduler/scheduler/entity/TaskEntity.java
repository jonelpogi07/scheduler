package com.scheduler.scheduler.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import com.scheduler.scheduler.dto.TaskDto;

@Entity
@Table(name = "task")
@SqlResultSetMapping(
        name = "TaskDtoMapping",
        classes = @ConstructorResult(
                targetClass = TaskDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "creation_date", type = LocalDateTime.class),
                        @ColumnResult(name = "start_date", type = LocalDateTime.class),
                        @ColumnResult(name = "end_date", type = LocalDateTime.class),
                        @ColumnResult(name = "dependencies", type = String.class),
                        @ColumnResult(name = "duration", type = Integer.class)
                }
        )
)

public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "creation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    @Column(name = "start_date")
    private LocalDateTime startDate;  // Changed from Date to LocalDateTime

    @Column(name = "end_date")
    private LocalDateTime endDate;    // Changed from Date to LocalDateTime

    @Column(name = "status")
    private int status;

    @Column(name = "dependencies")
    private String dependencies;

    @Column(name = "duration")
    private int duration;

    // Default constructor
    public TaskEntity() {
        // Set creationDate to current time when the entity is created
        this.creationDate = LocalDateTime.now();
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

    // Optionally, you can set creationDate if needed, but it defaults to now()
    public void setCreationDate(LocalDateTime creationDate) {
        if (creationDate != null) {
            this.creationDate = creationDate;
        } else {
            this.creationDate = LocalDateTime.now();  // Defaults to current time if not provided
        }
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