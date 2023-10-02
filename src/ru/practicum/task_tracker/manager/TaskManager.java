package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.List;

public interface TaskManager {

    Long addNewTask(Task task);

    Long addNewEpic(Epic epic);

    Long addNewSubtask(Subtask subtask);

    long generateId();

    long getGeneratorId();

    void setGeneratorId(long generatorId);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void updateEpicStatus(long epicId);

    void deleteTask(Long taskId);

    void deleteSubtaskById(Long subtaskId);

    void deleteEpic(Long epicId);

    void deleteAllTask();

    void deleteAllSubtask();

    void deleteAllEpic();

    void deleteAll();

    Task getTaskById(Long taskId);

    Epic getEpicById(Long epicId);

    Subtask getSubtaskById(Long subtaskId);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    HistoryManager getInMemoryHistoryManager();

    void updateEpicDuration(Long epicId);

    List<Task> getPrioritizedTasks();

    boolean isCheckTimeIntersection(Task task);
}
