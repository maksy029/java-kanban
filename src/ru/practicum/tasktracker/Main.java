package ru.practicum.tasktracker;

import ru.practicum.tasktracker.manager.HttpTaskManager;
import ru.practicum.tasktracker.manager.Managers;
import ru.practicum.tasktracker.models.Status;
import ru.practicum.tasktracker.server.HttpTaskServer;
import ru.practicum.tasktracker.server.KVServer;
import ru.practicum.tasktracker.tasks.Epic;
import ru.practicum.tasktracker.tasks.Subtask;
import ru.practicum.tasktracker.tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

/*
коммит 2 по ТЗ8:
- исключили  wild import;
- произвели рефакторинг методов: handler(HttpExchange exchange) и getEndpoint(HttpExchange exchange)
класса HttpTaskServer с той целью, что бы разбить их на менее объемные методы;
 */

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskManager inMemoryTaskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(inMemoryTaskManager);
        httpTaskServer.start();

        //Создание задач и присвоение ID
        Task task1 = new Task("Таска1", "Описание Таски1", Status.NEW
                , 60, LocalDateTime.of(2023, 9, 20, 12, 0));
        Long task1Id = inMemoryTaskManager.addNewTask(task1);

        Task task2 = new Task("Таска2", "Описание Таски2", Status.DONE
                , 60, LocalDateTime.of(2023, 10, 21, 12, 0));
        Long task2Id = inMemoryTaskManager.addNewTask(task2);

        Epic epic1 = new Epic("Эпик1", "Описание Эпика1");
        Long epic1Id = inMemoryTaskManager.addNewEpic(epic1);

        Epic epic2 = new Epic("Эпик2", "Описание Эпика2");
        Long epic2Id = inMemoryTaskManager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.IN_PROGRESS, epic1Id
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtask1Id = inMemoryTaskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаска2", "Описание Сабтаски2", Status.DONE, epic2Id
                , 60, LocalDateTime.of(2023, 12, 23, 12, 0));
        Long subtask2Id = inMemoryTaskManager.addNewSubtask(subtask2);

        Subtask subtask3 = new Subtask("Сабтаска3", "Описание Сабтаски3", Status.DONE, epic2Id
                , 60, LocalDateTime.of(2024, 1, 24, 12, 0));
        Long subtask3Id = inMemoryTaskManager.addNewSubtask(subtask3);

        //Проверка. Печать всех созданных task, epic, subtask
        System.out.println();
        System.out.println("Печать всех созданных task, epic, subtask: ");

        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println(subtask);
        }

        //Проверка. Возврашение выборочные имена Task Subtask Epic
        System.out.println();
        System.out.println("Проверка. Возврашение выборочные имена Task Subtask Epic");
        System.out.println(inMemoryTaskManager.getTaskById(task1Id).getName());
        System.out.println(inMemoryTaskManager.getSubtaskById(subtask1Id).getName());
        System.out.println(inMemoryTaskManager.getEpicById(epic1Id).getName());

        // Проверка. Вывод инфо задач:
        System.out.println();
        System.out.println("Проверка. Вывод инфо задач");
        System.out.println("У эпика с ID: " + epic1Id + " Название: "
                + inMemoryTaskManager.getEpicById(epic1Id).getName());
        System.out.println("У таски с ID: " + task1Id + " Название: "
                + inMemoryTaskManager.getTaskById(task1Id).getName());
        System.out.println("У сабтаски с ID: " + subtask1Id + " Название: "
                + inMemoryTaskManager.getSubtaskById(subtask1Id).getName());

        //Печать истории просмотров:
        System.out.println();
        System.out.println("Печать истории просмотров:");
        for (Task history : inMemoryTaskManager.getInMemoryHistoryManager().getHistory()) {
            System.out.println(history.getId());
        }
    }
}

