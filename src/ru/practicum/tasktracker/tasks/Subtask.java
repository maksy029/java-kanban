package ru.practicum.tasktracker.tasks;

import ru.practicum.tasktracker.models.Status;
import ru.practicum.tasktracker.models.TaskType;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private final Long epicId;


    public Subtask(String name, String desc, Status status, Long epicId) {
        super(name, desc, status);
        this.epicId = epicId;
        type = TaskType.SUBTASK;
    }

    public Subtask(String name, String desc, Status status, Long epicId, long duration, LocalDateTime startTime) {
        super(name, desc, status, duration, startTime);
        this.epicId = epicId;
        type = TaskType.SUBTASK;
    }

    public Long getEpicId() {
        return epicId;
    }

    public TaskType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%d", id, type, name, status, desc, duration, startTime, epicId);
    }
}



