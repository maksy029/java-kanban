package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.models.Status;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.task_tracker.manager.Managers.getDefaultHistory;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<Long, Epic> epics = new HashMap<>();
    private final Map<Long, Subtask> subtasks = new HashMap<>();
    private long generatorId = 0;
    private final HistoryManager inMemoryHistoryManager = getDefaultHistory();

    @Override
    public Long addNewTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public Long addNewEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
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

    @Override
    public long generateId() {
        return generatorId++;
    }

    @Override
    public void updateTask(Task task) {
        Task saveTask = tasks.get(task.getId());
        if (saveTask == null) {
            return;
        }
        saveTask.setName(task.getName());
        saveTask.setDesc(task.getDesc());
        saveTask.setStatus(task.getStatus());
    }

    @Override
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

    @Override
    public void updateEpic(Epic epic) {
        Epic saveEpic = epics.get(epic.getId());
        if (saveEpic == null) {
            return;
        }
        saveEpic.setName(epic.getName());
        saveEpic.setDesc(epic.getDesc());
        saveEpic.setStatus(epic.getStatus());
    }

    @Override
    public void updateEpicStatus(long epicId) {
        Epic epic = epics.get(epicId);
        List<Long> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        for (long subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask == null) {
                return;
            }
            if (subtaskIds.size() == 1) {
                epic.setStatus(subtask.getStatus());
            } else if (subtask.getStatus() == Status.IN_PROGRESS && subtask.getStatus() != Status.DONE
                    && subtask.getStatus() != Status.NEW) {
                epic.setStatus(Status.IN_PROGRESS);
            } else if (subtask.getStatus() != Status.IN_PROGRESS && subtask.getStatus() != Status.DONE
                    && subtask.getStatus() == Status.NEW) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.DONE);
            }
        }
    }

    @Override
    public void deleteTask(Long taskId) { // удаление таски по id
        tasks.remove(taskId);
    }

    @Override
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
        savedEpic.getSubtaskIds().remove(savedSubtask.getId());
        updateEpicStatus(savedEpic.getId());
    }

    @Override
    public void deleteEpic(Long epicId) { // удаление эпика по id
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                subtasks.remove(subtask.getEpicId());
            }
        }
        epics.remove(epicId);
    }

    @Override
    public Task getTaskById(Long taskId) {
        inMemoryHistoryManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicById(Long epicId) {
        inMemoryHistoryManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public Subtask getSubtaskById(Long subtaskId) {
        inMemoryHistoryManager.add(subtasks.get(subtaskId));
        return subtasks.get(subtaskId);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
}
