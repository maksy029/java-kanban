package ru.practicum.task_tracker.converter;

import ru.practicum.task_tracker.manager.HistoryManager;
import ru.practicum.task_tracker.models.Status;
import ru.practicum.task_tracker.models.TaskType;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class CSVConverter {

    private CSVConverter() {
    }

    public static String toString(Task task) {
        return task.toString();
    }

    public static Task fromString(String taskStr) {
        String[] tokens = taskStr.split(","); // taskStr формат: id,type,name,status,description,epic
        Long id = (long) Integer.parseInt(tokens[0]);
        TaskType type = TaskType.valueOf(tokens[1]);
        String name = tokens[2];
        Status status = Status.valueOf(tokens[3]);
        String desc = tokens[4];
        Long epicID = null;
        if (tokens.length > 5) {
            epicID = (long) Integer.parseInt(tokens[5]);
        }
        switch (type) {
            case TASK:
                return new Task(name, desc, status);
            case SUBTASK:
                return new Subtask(name, desc, status, epicID);
            case EPIC:
                return new Epic(name, desc);
            default:
                return null;
        }
    }

    public static String historyToString(HistoryManager manager) {
        if (manager.getHistory().isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (Task task : manager.getHistory()) {
            result.append(task.getId()).append(",");
        }

        return result.toString();
    }

    public static List<Long> historyFromString(String historyStr) {
        List<Long> listId = new ArrayList<>();
        String[] tokens = historyStr.split(",");

        for (String id : tokens) {
            listId.add((long) Integer.parseInt(id));
        }
        return listId;
    }
}
