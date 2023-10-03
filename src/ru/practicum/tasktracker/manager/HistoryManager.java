package ru.practicum.tasktracker.manager;

import ru.practicum.tasktracker.tasks.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory();

    void add(Task task);

    void remove(Long id);
}
