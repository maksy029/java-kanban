package ru.practicum.task_tracker.tasks;

import ru.practicum.task_tracker.models.Status;
import ru.practicum.task_tracker.models.TaskType;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Long> subtaskIds;  // в этом списке лежат id сабстасков для эпика
    private final TaskType type = TaskType.EPIC;

    public Epic(String name, String desc) {
        super(name, desc, Status.NEW);
        subtaskIds = new ArrayList<>();
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
        return String.format("%d,%s,%s,%s,%s,", id, type, name, status, desc);
    }

}
