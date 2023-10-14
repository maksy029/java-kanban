package ru.practicum.tasktracker.manager;

import ru.practicum.tasktracker.converter.CSVConverter;
import ru.practicum.tasktracker.exception.ManagerSaveException;
import ru.practicum.tasktracker.tasks.Epic;
import ru.practicum.tasktracker.tasks.Subtask;
import ru.practicum.tasktracker.tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    private static final String HEADER_CSV_FILE = "id,type,name,status,description,duration,startTime,epic\n";

    public FileBackedTasksManager() {
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }


    void save() {
        try (Writer fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            fw.write(HEADER_CSV_FILE);

            for (Task task : getTasks()) {
                fw.write(CSVConverter.toString(task));
                fw.write("\n");
            }
            for (Epic epic : getEpics()) {
                fw.write(CSVConverter.toString(epic));
                fw.write("\n");
            }
            for (Subtask subtask : getSubtasks()) {
                fw.write(CSVConverter.toString(subtask));
                fw.write("\n");
            }

            fw.write("\n");
            fw.write(CSVConverter.historyToString(getInMemoryHistoryManager()));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи СSV файла!");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager newTaskManager = Managers.getDefaultFileBacked();
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            br.readLine(); // пропускаем первую строчку с заголовком

            while (br.ready()) {
                String taskStr = br.readLine();
                if (taskStr.isBlank()) {
                    break;
                } else {
                    Task task = CSVConverter.fromString(taskStr);
                    if (task instanceof Epic) {
                        newTaskManager.epics.put(task.getId(), (Epic) task);
                    } else if (task instanceof Subtask) {
                        newTaskManager.subtasks.put(task.getId(), (Subtask) task);
                        newTaskManager.prioritizedTasks.add(task);
                    } else {
                        newTaskManager.tasks.put(task.getId(), task);
                        newTaskManager.prioritizedTasks.add(task);
                    }
                    if (task.getId() > newTaskManager.getGeneratorId()) {
                        newTaskManager.setGeneratorId(task.getId());
                    }
                }
            }

            while (br.ready()) {
                String historyStr = br.readLine();
                if (historyStr.isBlank()) {
                    break;
                }
                for (Long id : CSVConverter.historyFromString(historyStr)) {

                    for (Task task : newTaskManager.getTasks()) {
                        if (task.getId().equals(id)) {
                            newTaskManager.getInMemoryHistoryManager().add(task);
                        }
                    }
                    for (Epic epic : newTaskManager.getEpics()) {
                        if (epic.getId().equals(id)) {
                            newTaskManager.getInMemoryHistoryManager().add(epic);
                        }
                    }
                    for (Subtask subtask : newTaskManager.getSubtasks()) {
                        if (subtask.getId().equals(id)) {
                            newTaskManager.getInMemoryHistoryManager().add(subtask);
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения имеющегося CSV файла");
        }
        return newTaskManager;
    }

    @Override
    public Long addNewTask(Task task) {
        Long id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public Long addNewSubtask(Subtask subtask) {
        Long id = super.addNewSubtask(subtask);
        save();
        return id;
    }

    @Override
    public Long addNewEpic(Epic epic) {
        Long id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateEpicStatus(long epicId) {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public void updateEpicDuration(Long epicId) {
        super.updateEpicDuration(epicId);
        save();
    }

    @Override
    public void deleteTask(Long taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteSubtaskById(Long subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteEpic(Long epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        save();
    }

    @Override
    public Task getTaskById(Long taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Long epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Long subtaskId) {
        Subtask subtask = super.getSubtaskById(subtaskId);
        save();
        return subtask;
    }


    @Override
    public long generateId() {
        return super.generateId();
    }
}
