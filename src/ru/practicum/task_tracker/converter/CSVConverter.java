package ru.practicum.task_tracker.converter;

import ru.practicum.task_tracker.exception.ManagerSaveException;
import ru.practicum.task_tracker.manager.HistoryManager;
import ru.practicum.task_tracker.models.Status;
import ru.practicum.task_tracker.models.TaskType;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVConverter {

    private CSVConverter() {
    }

    public static String toString(Task task) {
        return task.toString();
    }

    // taskStr формат: id,type,name,status,description,duration,startTime,epic
    public static Task fromString(String taskStr) {
        String[] tokens = taskStr.split(",");
        Long id = (long) Integer.parseInt(tokens[0]);
        TaskType type = TaskType.valueOf(tokens[1]);
        String name = tokens[2];
        Status status = Status.valueOf(tokens[3]);
        String desc = tokens[4];
        long duration = Integer.parseInt(tokens[5]);
        LocalDateTime startTime = null;
        if (!tokens[6].equals("null")) {
            startTime = LocalDateTime.parse(tokens[6]);
        }
        Long epicID = null;
        if (tokens.length > 7) {
            epicID = (long) Integer.parseInt(tokens[7]);
        }

        switch (type) {
            case TASK:
                Task task = new Task(name, desc, status);
                task.setId(id);
                task.setDuration(duration);
                task.setStartTime(startTime);
                return task;
            case EPIC:
                Epic epic = new Epic(name, desc);
                epic.setId(id);
                epic.setStatus(status);
                epic.setDuration(duration);
                epic.setStartTime(startTime);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(name, desc, status, epicID);
                subtask.setId(id);
                subtask.setDuration(duration);
                subtask.setStartTime(startTime);
                return subtask;
            default:
                throw new ManagerSaveException("911");
        }
    }

    public static String historyToString(HistoryManager manager) {
        if (manager.getHistory().isEmpty()) {
            return "нет истории";
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
