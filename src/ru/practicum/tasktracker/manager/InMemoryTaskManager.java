package ru.practicum.tasktracker.manager;

import ru.practicum.tasktracker.exception.ManagerSaveException;
import ru.practicum.tasktracker.models.Status;
import ru.practicum.tasktracker.tasks.Epic;
import ru.practicum.tasktracker.tasks.Subtask;
import ru.practicum.tasktracker.tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static ru.practicum.tasktracker.manager.Managers.getDefaultHistory;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Long, Task> tasks = new HashMap<>();
    protected final Map<Long, Epic> epics = new HashMap<>();
    protected final Map<Long, Subtask> subtasks = new HashMap<>();
    private long generatorId = 1;
    private final HistoryManager inMemoryHistoryManager = getDefaultHistory();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime
            , Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));

    @Override
    public Long addNewTask(Task task) {
        if (isCheckTimeIntersection(task)) {
            task.setId(generateId());
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else {
            throw new ManagerSaveException("Задача пересекается по времени");
        }
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
        if (isCheckTimeIntersection(subtask)) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic == null) {
                return null;
            }
            subtask.setId(generateId());
            subtasks.put(subtask.getId(), subtask);
            prioritizedTasks.add(subtask);
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(subtask.getEpicId());
            updateEpicDuration(subtask.getEpicId());
        } else {
            throw new ManagerSaveException("Задача пересекается по времени");
        }

        return subtask.getId();
    }

    @Override
    public long generateId() {
        return generatorId++;
    }

    @Override
    public long getGeneratorId() {
        return generatorId;
    }

    @Override
    public void setGeneratorId(long generatorId) {
        this.generatorId = generatorId;
    }

    @Override
    public void updateTask(Task task) {
        Task saveTask = tasks.get(task.getId());
        if (saveTask == null) {
            return;
        }
        prioritizedTasks.remove(saveTask);
        if (isCheckTimeIntersection(saveTask)) {
            saveTask.setName(task.getName());
            saveTask.setDesc(task.getDesc());
            saveTask.setStatus(task.getStatus());
            tasks.put(saveTask.getId(), saveTask);
            prioritizedTasks.add(saveTask);
        } else {
            throw new ManagerSaveException("Задача пересекается по времени");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask saveSubtask = subtasks.get(subtask.getId());
        if (saveSubtask == null) {
            return;
        }
        prioritizedTasks.remove(saveSubtask);
        if (isCheckTimeIntersection(saveSubtask)) {
            saveSubtask.setName(subtask.getName());
            saveSubtask.setDesc(subtask.getDesc());
            saveSubtask.setStatus(subtask.getStatus());
            subtasks.put(saveSubtask.getEpicId(), saveSubtask);
            updateEpicStatus(saveSubtask.getEpicId());
            updateEpicDuration(saveSubtask.getEpicId());
            prioritizedTasks.add(saveSubtask);
        } else {
            throw new ManagerSaveException("Задача пересекается по времени");
        }
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
        epics.put(saveEpic.getId(), saveEpic);
    }

    @Override
    public void updateEpicStatus(long epicId) {
        Epic epic = epics.get(epicId);
        List<Long> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        for (Long subtaskId : subtaskIds) {
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
        prioritizedTasks.remove(getTaskById(taskId));
        tasks.remove(taskId);
        inMemoryHistoryManager.remove(taskId);
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
        prioritizedTasks.remove(getSubtaskById(subtaskId));
        subtasks.remove(subtaskId);
        inMemoryHistoryManager.remove(subtaskId);
        savedEpic.getSubtaskIds().remove(savedSubtask.getId());
        updateEpicStatus(savedEpic.getId());
        updateEpicDuration(savedEpic.getId());
    }

    @Override
    public void deleteEpic(Long epicId) { // удаление эпика по id
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                subtasks.remove(subtask.getEpicId());
                inMemoryHistoryManager.remove(subtask.getEpicId());
            }
        }
        epics.remove(epicId);
        inMemoryHistoryManager.remove(epicId);
    }

    @Override
    public void deleteAllTask() { // удаление всех тасок в мапе и истории просмотра
        for (Long id : tasks.keySet()) {
            inMemoryHistoryManager.remove(id);
            prioritizedTasks.remove(getTaskById(id));
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubtask() { // удаление всех сабтасок и истории просмотра
        for (Long id : subtasks.keySet()) {
            inMemoryHistoryManager.remove(id);
            prioritizedTasks.remove(getSubtaskById(id));
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpic() { // удаление всех эпиков и истории просмотра
        for (Long id : epics.keySet()) {
            inMemoryHistoryManager.remove(id);
        }
        deleteAllSubtask();
        epics.clear();
    }

    @Override
    public void deleteAll() { // полная очистка тасок/сабтасок/эпиков и их просмотров
        deleteAllTask();
        deleteAllSubtask();
        deleteAllEpic();
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

    @Override
    public HistoryManager getInMemoryHistoryManager() {
        return this.inMemoryHistoryManager;
    }

    @Override
    public void updateEpicDuration(Long epicId) {
        Epic epic = epics.get(epicId);
        List<Long> subtaskIds = epic.getSubtaskIds();
        List<LocalDateTime> subtaskStartTimes = new ArrayList<>();
        List<LocalDateTime> subtaskEndTimes = new ArrayList<>();
        long sum = 0;
        for (Long subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask == null) {
                return;
            }
            subtaskStartTimes.add(subtask.getStartTime());
            sum += subtask.getDuration();
            subtaskEndTimes.add(subtask.getEndTime());
        }

        epic.setStartTime(Collections.min(subtaskStartTimes));
        epic.setDuration(sum);
        epic.setEndTime(Collections.max(subtaskEndTimes));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public boolean isCheckTimeIntersection(Task newTask) {
        if (getPrioritizedTasks().isEmpty()) {
            return true;
        }
        for (Task prioritizedTask : getPrioritizedTasks()) {
            if (prioritizedTask.getStartTime() == null) {
                return true;
            }
            if (!(newTask.getEndTime().isBefore(prioritizedTask.getStartTime())
                    || newTask.getStartTime().isAfter(prioritizedTask.getEndTime()))) {
                return false;
            }
        }
        return true;
    }
}
