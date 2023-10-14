package ru.practicum.tasktracker.tasks;

import ru.practicum.tasktracker.models.Status;
import ru.practicum.tasktracker.models.TaskType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Long> subtaskIds;  // в этом списке лежат id сабстасков для эпика
    private LocalDateTime endTime;

    public Epic(String name, String desc) {
        super(name, desc, Status.NEW);
        subtaskIds = new ArrayList<>();
        type = TaskType.EPIC;
    }

    public TaskType getType() {
        return type;
    }

    public ArrayList<Long> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(long subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void clearSubtaskIds(Long id) {
        subtaskIds.remove(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,", id, type, name, status, desc, duration, startTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
