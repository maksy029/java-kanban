package ru.practicum.tasktracker;

import ru.practicum.tasktracker.manager.FileBackedTasksManager;
import ru.practicum.tasktracker.models.Status;
import ru.practicum.tasktracker.tasks.Epic;
import ru.practicum.tasktracker.tasks.Subtask;
import ru.practicum.tasktracker.tasks.Task;

import java.io.File;
import java.time.LocalDateTime;

public class FileBackedTasksManagerTester {

    public static void main(String[] args) {
        File file = new File("src/ru/practicum/tasktracker/resources/data.csv");

        FileBackedTasksManager tasksManager1 = new FileBackedTasksManager(file);

        Task task1 = new Task("Таска1", "Описание Таски1", Status.NEW
                , 60, LocalDateTime.of(2023, 9, 20, 12, 0));
        Long task1Id = tasksManager1.addNewTask(task1);

        Task task2 = new Task("Таска2", "Описание Таски2", Status.DONE
                , 60, LocalDateTime.of(2023, 10, 21, 12, 0));
        Long task2Id = tasksManager1.addNewTask(task2);

        Epic epic1 = new Epic("Эпик1", "Описание Эпика1");
        Long epic1Id = tasksManager1.addNewEpic(epic1);

        Epic epic2 = new Epic("Эпик2", "Описание Эпика2");
        Long epic2Id = tasksManager1.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.IN_PROGRESS, epic1Id
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtask1Id = tasksManager1.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаска2", "Описание Сабтаски2", Status.DONE, epic2Id
                , 60, LocalDateTime.of(2023, 12, 23, 12, 0));
        Long subtask2Id = tasksManager1.addNewSubtask(subtask2);

        Subtask subtask3 = new Subtask("Сабтаска3", "Описание Сабтаски3", Status.DONE, epic2Id
                , 60, LocalDateTime.of(2024, 1, 24, 12, 0));
        Long subtask3Id = tasksManager1.addNewSubtask(subtask3);

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

        System.out.println();
        System.out.println("Печать task, subtask в порядке приоритета -startTime");
        for (Task prioritizedTask : tasksManager1.getPrioritizedTasks()) {
            System.out.println(prioritizedTask);
        }

        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(file);

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

        System.out.println();
        System.out.println("Печать восстановленных task, subtask в порядке приоритета -startTime");
        for (Task prioritizedTask : tasksManager2.getPrioritizedTasks()) {
            System.out.println(prioritizedTask);
        }
    }
}
