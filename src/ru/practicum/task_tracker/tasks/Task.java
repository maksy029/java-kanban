package ru.practicum.task_tracker.tasks;

import ru.practicum.task_tracker.models.Status;
import ru.practicum.task_tracker.models.TaskType;

import java.util.Objects;

public class Task {
    protected Long id;
    protected String name;
    protected String desc;
    protected Status status;
    private final TaskType type = TaskType.TASK;

    public Task(String name, String desc, Status status) {
        this.name = name;
        this.desc = desc;
        this.status = status;
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
        return String.format("%d,%s,%s,%s,%s,", id, this.type, name, status, desc);
    }
}

