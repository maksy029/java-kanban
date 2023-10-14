package ru.practicum.tasktracker.manager;

import com.google.gson.Gson;

import java.io.File;

public final class Managers {

    private Managers() {
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager() {
        };
    }

    public static HttpTaskManager getDefault() {
        return new HttpTaskManager() {
        };
    }

    public static Gson getGson() {
        return new Gson();
    }

    public static FileBackedTasksManager getDefaultFileBacked() {
        return new FileBackedTasksManager(new File("src/ru/practicum/tasktracker/resources/data.csv"));
    }
}
