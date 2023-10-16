package ru.practicum.tasktracker.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.practicum.tasktracker.client.KVTaskClient;
import ru.practicum.tasktracker.tasks.Epic;
import ru.practicum.tasktracker.tasks.Subtask;
import ru.practicum.tasktracker.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private static final String TASK = "task";
    private static final String EPIC = "epic";
    private static final String SUBTASK = "subtask";
    private static final String HISTORY = "history";

    private final KVTaskClient kvTaskClient;
    private final Gson gson;

    public HttpTaskManager() {
        super();
        kvTaskClient = new KVTaskClient();
        gson = Managers.getGson();
        load();
    }

    private void load() {
        String tasksJson = kvTaskClient.load(TASK);
        if (tasksJson != null) {
            List<Task> restoreTasks = gson.fromJson(tasksJson, new TypeToken<ArrayList<Task>>() {
            });
            restoreTasks.forEach(task -> tasks.put(task.getId(), task));
        }

        String subtasksJson = kvTaskClient.load(SUBTASK);
        if (subtasksJson != null) {
            List<Subtask> restoreSubtasks = gson.fromJson(subtasksJson, new TypeToken<ArrayList<Subtask>>() {
            });
            restoreSubtasks.forEach(subtask -> subtasks.put(subtask.getId(), subtask));
        }

        String epicsJson = kvTaskClient.load(EPIC);
        if (epicsJson != null) {
            List<Epic> restoreEpics = gson.fromJson(epicsJson, new TypeToken<ArrayList<Epic>>() {
            });
            restoreEpics.forEach(epic -> epics.put(epic.getId(), epic));
        }

        String historyJoin = kvTaskClient.load(HISTORY);
        if (historyJoin != null) {
            List<Task> restoreHistory = gson.fromJson(historyJoin, new TypeToken<ArrayList<Task>>() {
            });
            restoreHistory.forEach(task -> getInMemoryHistoryManager().add(task));
        }
    }

    @Override
    void save() {
        kvTaskClient.put(TASK, gson.toJson(tasks.values()));
        kvTaskClient.put(SUBTASK, gson.toJson(subtasks.values()));
        kvTaskClient.put(EPIC, gson.toJson(epics.values()));
        kvTaskClient.put(HISTORY, gson.toJson(getInMemoryHistoryManager().getHistory()));
    }
}
