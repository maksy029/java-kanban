package ru.practicum.task_tracker.manager;

public final class Managers {

    private Managers() {

    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager() {
        };
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager() {
        };
    }
}
