package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.module.Status;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    Long addNewTask(Task task);

    Long addNewEpic(Epic epic);

    Long addNewSubtask(Subtask subtask);

    long generateId();

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void updateEpicStatus(long epicId);

    void deleteTask(Long taskId);

    void deleteSubtaskById(Long subtaskId);

    void deleteEpic(Long epicId);

    Task getTaskById(Long taskId);

    Epic getEpicById(Long epicId);

    Subtask getSubtaskById(Long subtaskId);

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    void printHistory();
}
