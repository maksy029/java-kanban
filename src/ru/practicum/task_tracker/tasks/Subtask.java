package ru.practicum.task_tracker.tasks;

import ru.practicum.task_tracker.models.Status;
import ru.practicum.task_tracker.models.TaskType;

import java.util.Objects;

public class Subtask extends Task {
    private Long epicId;
    private final TaskType type = TaskType.SUBTASK;


    public Subtask(String name, String desc, Status status, Long epicId) {
        super(name, desc, status);
        this.epicId = epicId;
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
        return String.format("%d,%s,%s,%s,%s,%d", id, type, name, status, desc, epicId);
    }


}



