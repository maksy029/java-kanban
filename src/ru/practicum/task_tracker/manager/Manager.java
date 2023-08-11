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
    }

    public void updateSubtask(Subtask subtask) {
        Subtask saveSubtask = subtasks.get(subtask.getId());
        if (saveSubtask == null) {
            return;
        }
        saveSubtask.setName(subtask.getName());
        saveSubtask.setDesc(subtask.getDesc());
        saveSubtask.setStatus(subtask.getStatus());
        updateEpicStatus(saveSubtask.getEpicId());
    }

    public void updateEpic(Epic epic) {
        Epic saveEpic = epics.get(epic.getId());
        if (saveEpic == null) {
            return;
        }
        saveEpic.setName(epic.getName());
        saveEpic.setDesc(epic.getDesc());
        saveEpic.setStatus(epic.getStatus());
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
            if (subtask==null) {
                return;
            }

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

    public void deleteTask(Long taskId) { // удаление таски по id
        tasks.remove(taskId);
    }

    public void deleteSubtaskById(Long subtaskId) { // удаление сабтаски по id
        Subtask savedSubtask = subtasks.get(subtaskId);
        if (savedSubtask == null) {
            return;
        }
        Epic savedEpic = epics.get(savedSubtask.getEpicId());
        if (savedEpic == null) {
            return;
        }
        subtasks.remove(subtaskId);
        updateEpicStatus(savedEpic.getId());
        savedEpic.getSubtaskIds().remove(savedSubtask.getId());
    }

    public void deleteEpic(Long epicId) { // удаление эпика по id
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                subtasks.remove(subtask.getEpicId());
            }
        }
        epics.remove(epicId);
    }

    public Task getTaskById(Long taskId) {
        return tasks.get(taskId);
    }

    public Epic getEpicById(Long epicId) {
        return epics.get(epicId);
    }

    public Subtask getSubtaskById(Long subtaskId) {
        return subtasks.get(subtaskId);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
}
