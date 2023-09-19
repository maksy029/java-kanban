package ru.practicum.task_tracker.tester;

import ru.practicum.task_tracker.manager.FileBackedTasksManager;
import ru.practicum.task_tracker.models.Status;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.io.File;

public class FileBackedTasksManagerTest {

    public static void main(String[] args) {
        File file = new File("src/ru/practicum/task_tracker/resources/data.csv");

        FileBackedTasksManager tasksManager1 = new FileBackedTasksManager(file);

        Task task1 = new Task("Таска1", "Описание Таски1", Status.NEW);
        Long task1Id = tasksManager1.addNewTask(task1);

        Task task2 = new Task("Таска2", "Описание Таски2", Status.NEW);
        Long task2Id = tasksManager1.addNewTask(task2);

        Epic epic1 = new Epic("Эпик1", "Описание Эпика1");
        Long epic1Id = tasksManager1.addNewEpic(epic1);

        Epic epic2 = new Epic("Эпик2", "Описание Эпика2");
        Long epic2Id = tasksManager1.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.NEW, epic1Id);
        Long subtask1Id = tasksManager1.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаска2", "Описание Сабтаски2", Status.NEW, epic1Id);
        Long subtask2Id = tasksManager1.addNewSubtask(subtask2);

        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(file);

        //Печать всех созданных task, epic, subtask c сохранением в СSV файл
        System.out.println();
        System.out.println("Печать всех созданных task, epic, subtask c сохранением в СSV файл: ");

        for (Task task : tasksManager1.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic : tasksManager1.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : tasksManager1.getSubtasks()) {
            System.out.println(subtask);
        }

        //Создаем историю просмотра
        tasksManager1.getTaskById(task1Id);
        tasksManager1.getSubtaskById(subtask1Id);
        tasksManager1.getEpicById(epic1Id);

        //Печать истории просмотров с сохранеием id в CSV
        System.out.println();
        System.out.println("Печать истории просмотров с сохранеием id в CSV: ");
        for (Task history : tasksManager1.getInMemoryHistoryManager().getHistory()) {
            System.out.println(history);
        }

        //Печать восстановленных из СSV файла задач:
        System.out.println();
        System.out.println("Печать восстановленных из СSV файла задач");
        for (Task task : tasksManager2.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic : tasksManager2.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : tasksManager2.getSubtasks()) {
            System.out.println(subtask);
        }

        //Печать восстановленной из CSV истории просмотров
        System.out.println();
        System.out.println("Печать восстановленной из CSV истории просмотров: ");
        for (Task history : tasksManager2.getInMemoryHistoryManager().getHistory()) {
            System.out.println(history);
        }
    }
}
