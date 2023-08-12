package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyOfViews = new ArrayList<>();
    private static final int LIMIT_OF_HISTORY_SIZE = 10;

    @Override
    public List<Task> getHistory() {
        return null;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        historyOfViews.add(task);
        if (historyOfViews.size() >= LIMIT_OF_HISTORY_SIZE) {
            historyOfViews.remove(0);
        }
    }
}
