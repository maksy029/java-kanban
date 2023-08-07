package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskTracker {
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

    public String updateTaskStatus(Task task) {
        if (task.getStatus().equals("IN_PROGRESS")) {
            task.setStatus("DONE");
        } else {
            task.setStatus("IN_PROGRESS");
        }
        return task.getStatus();
    }

    public String updateSubtaskStatus(Subtask subtask) {
        if (subtask.getStatus().equals("IN_PROGRESS")) {
            subtask.setStatus("DONE");
        } else {
            subtask.setStatus("IN_PROGRESS");
        }
        updateEpicStatus(subtask.getEpicId());
        return subtask.getStatus();
    }

    private void updateEpicStatus(long epicId) {
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

    public void updateTaskInfo(Task task, Long id) {
        Task newTaskInfo = tasks.get(id);
        if (newTaskInfo == null) {
            return;
        }
        tasks.put(id, task);
    }

    public void updateSubtaskInfo(Subtask subtask, Long id) {
        Subtask oldSubtaskInfo = subtasks.get(id);
        subtask.setId(oldSubtaskInfo.getId());
        subtask.setEpicId(oldSubtaskInfo.getEpicId());
        if (oldSubtaskInfo == null) {
            return;
        }
        subtasks.put(id, subtask);
    }

    public void updateEpicInfo(Epic epic, Long id) {
        Epic oldEpicInfo = epics.get(id);
        epic.setId(id);
        epic.setSubtaskIds(oldEpicInfo.getSubtaskIds());
        if (oldEpicInfo == null) {
            return;
        }
        epics.put(id, epic);
        updateEpicStatus(id);
    }

    public void deleteTask(Long id) { // удаление таски по id
        tasks.remove(id);
    }

    public void deleteSubtask(Long id) { // удаление сабтаски по id
        subtasks.remove(id);
    }

    public void deleteEpic(Long id) { // удаление эпика по id
        epics.remove(id);
    }

    public void deleteAllSubtasks(Long epicId, Epic epic) { // удаление всех сабтасок определенного эпика
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                epic.clearSubtaskIds(subtask.getId());
            }
            subtasks.remove(subtask);
        }
    }

    public ArrayList<String> printEpicById(Epic epic) { // печать и возврат эпика
        ArrayList<String> allSubtasksForCurrentEpic = new ArrayList<>();
        System.out.println("Эпик: " + epic.getName() + "Статус: " + epic.getStatus());
        for (Subtask subtask : subtasks.values()) {
            for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
                if (subtask.getId() == epic.getSubtaskIds().get(i)) {
                    allSubtasksForCurrentEpic.add(subtask.getName());
                }
            }
        }
        for (int j = 0; j < allSubtasksForCurrentEpic.size(); j++) {
            System.out.println(allSubtasksForCurrentEpic.get(j));
        }
        return allSubtasksForCurrentEpic;
    }

    public void print() {
        for (Task task : tasks.values()) {
            System.out.println("Task: " + task.getName() + ". Описание: " + task.getDesc() + ". Статус: " + task.getStatus());
        }
        for (Epic epic : epics.values()) {
            System.out.println("Epic: " + epic.getName() + ". Описание: " + epic.getDesc() + ". Статус: " + epic.getStatus());
            for (Subtask subtask : subtasks.values()) {
                for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
                    if (subtask.getId() == epic.getSubtaskIds().get(i)) {
                        System.out.println("Subtask: " + subtask.getName() + ". Описание: " + subtask.getDesc() + ". Статус: " + subtask.getStatus());
                    }
                }
            }
        }
    }

}
