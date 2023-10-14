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
        String tasksTaskString = kvTaskClient.load(TASK);
        if (tasksTaskString != null) {
            List<Task> tasksTask = gson.fromJson(tasksTaskString, new TypeToken<ArrayList<Task>>() {
            });
            tasksTask.forEach(task -> tasks.put(task.getId(), task));
        }

        String tasksSubtaskString = kvTaskClient.load(SUBTASK);
        if (tasksSubtaskString != null) {
            List<Subtask> tasksSubtask = gson.fromJson(tasksSubtaskString, new TypeToken<ArrayList<Subtask>>() {
            });
            tasksSubtask.forEach(subtask -> subtasks.put(subtask.getId(), subtask));
        }

        String tasksEpicString = kvTaskClient.load(EPIC);
        if (tasksEpicString != null) {
            List<Epic> tasksEpic = gson.fromJson(tasksEpicString, new TypeToken<ArrayList<Epic>>() {
            });
            tasksEpic.forEach(epic -> epics.put(epic.getId(), epic));
        }

        String tasksHistoryString = kvTaskClient.load(HISTORY);
        if (tasksHistoryString != null) {
            List<Task> tasksHistory = gson.fromJson(tasksHistoryString, new TypeToken<ArrayList<Task>>() {
            });
            tasksHistory.forEach(task -> getInMemoryHistoryManager().add(task));
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
