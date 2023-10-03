package ru.practicum.tasktracker.tasks;

import ru.practicum.tasktracker.models.Status;
import ru.practicum.tasktracker.models.TaskType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Task {
    protected Long id;
    protected String name;
    protected String desc;
    protected Status status;
    private final TaskType type = TaskType.TASK;
    protected long duration;
    protected LocalDateTime startTime;

    public Task(String name, String desc, Status status) {
        this.name = name;
        this.desc = desc;
        this.status = status;
    }

    public Task(String name, String desc, Status status, long duration, LocalDateTime startTime) {
        this.name = name;
        this.desc = desc;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public TaskType getType() {
        return type;
    }

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(desc, task.desc) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc, status);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,", id, this.type, name, status, desc, duration, startTime);
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration, ChronoUnit.MINUTES);
    }
}

