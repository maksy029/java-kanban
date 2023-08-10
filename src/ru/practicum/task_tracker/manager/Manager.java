package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, Epic> epics = new HashMap<>();
    private final HashMap<Long, Subtask> subtasks = new HashMap<>();
    private long generatorId = 0;

    public Long addNewTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public Long addNewEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public Long addNewSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(subtask.getEpicId());

        return subtask.getId();
    }

    private long generateId() {
        return generatorId++;
    }

    public void updateTask(Task task) {
        Task saveTask = tasks.get(task.getId());
        if (saveTask == null) {
            return;
        }
        saveTask.setName(task.getName());
        saveTask.setDesc(task.getDesc());
        saveTask.setStatus(task.getStatus());
        saveTask.setId(saveTask.getId());

    }

    public void updateSubtask(Subtask subtask) {
        Subtask saveSubtask = subtasks.get(subtask.getId());
        if (saveSubtask == null) {
            return;
        }
        saveSubtask.setName(subtask.getName());
        saveSubtask.setDesc(subtask.getDesc());
        saveSubtask.setStatus(subtask.getStatus());
        saveSubtask.setId(subtask.getId());
        saveSubtask.setEpicId(subtask.getEpicId());
        updateEpicStatus(saveSubtask.getEpicId());
    }

    void updateEpicStatus(long epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Long> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus("NEW");
            return;
        }
        String status = null;
        for (long subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);

            if (status == null) {
                status = subtask.getStatus();
                if (subtaskIds.size() == 1) {
                    epic.setStatus(status);
                }
            } else if (status.equals(subtask.getStatus()) && !status.equals("IN_PROGRESS")) {
                epic.setStatus(status);
            } else {
                epic.setStatus("IN_PROGRESS");
            }
        }
    }

    public void updateEpicInfo(Epic epic) {
        Epic oldEpicInfo = epics.get(epic.getId());
        epic.setId(epic.getId());
        epic.setSubtaskIds(oldEpicInfo.getSubtaskIds());
        if (oldEpicInfo == null) {
            return;
        }
        updateEpicStatus(epic.getId());
    }

    public void deleteTask(Long taskId) { // удаление таски по id
        tasks.remove(taskId);
    }

    public void deleteSubtask(Long subtaskId) { // удаление сабтаски по id
        subtasks.remove(subtaskId);
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getId() == subtaskId) {
                updateEpicStatus(subtask.getEpicId());
            }
        }
    }

    public void deleteEpic(Long epicId) { // удаление эпика по id
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                subtasks.remove(subtask.getEpicId());
            }
        }
        epics.remove(epicId);
    }

    public void deleteAllSubtasks(Epic epic) { // удаление всех сабтасок определенного эпика
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epic.getId()) {
                epic.clearSubtaskIds(subtask.getId());
            }
            subtasks.remove(subtask);
        }
    }


    public Task getTaskById(Long taskId) {
        Task task = tasks.get(taskId);
        return task;
    }

    public Epic getEpicById(Long epicId) {
        Epic epic = epics.get(epicId);
        return epic;
    }

    public Subtask getSubtaskById(Long subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        return subtask;
    }

    public HashMap<Long, Task> getTasks() {
        return tasks;
    }

    public HashMap<Long, Epic> getEpics() {
        return epics;
    }

    public HashMap<Long, Subtask> getSubtasks() {
        return subtasks;
    }

}
