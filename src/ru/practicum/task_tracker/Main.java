package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.TaskManager;
import ru.practicum.task_tracker.models.Status;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import static ru.practicum.task_tracker.manager.Managers.getDefault;

/*
коммит 2 по ТЗ6:
исправили в классе FileBackedTasksManager:
- переменная File file теперь private;
- в методе loadFromFile изменили тип переменной newTaskManager c TaskManager  на FileBackedTasksManager,
тем самым ушли от каста возвращаемого обекта;
 */

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = getDefault();

        //Создание задач и присвоение ID
        Task task1 = new Task("Таска1", "Описание Таски1", Status.NEW);
        Long task1Id = inMemoryTaskManager.addNewTask(task1);

        Task task2 = new Task("Таска2", "Описание Таски2", Status.NEW);
        Long task2Id = inMemoryTaskManager.addNewTask(task2);

        Epic epic1 = new Epic("Эпик1", "Описание Эпика1");
        long epic1Id = inMemoryTaskManager.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.NEW, epic1Id);
        Long subtask1Id = inMemoryTaskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаска2", "Описание Сабтаски2", Status.NEW, epic1Id);
        Long subtask2Id = inMemoryTaskManager.addNewSubtask(subtask2);

        Subtask subtask3 = new Subtask("Сабтаска3", "Описание Сабтаски3", Status.NEW, epic1Id);
        Long subtask3Id = inMemoryTaskManager.addNewSubtask(subtask3);

        Epic epic2 = new Epic("Эпик2", "Описание Эпика2");
        Long epic2Id = inMemoryTaskManager.addNewEpic(epic2);


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

//        // Проверка. Обновление Таск
//        System.out.println();
//        System.out.println("Проверка. Обновление Таск");
//        Task newTask = new Task("Тестовая", "Тестовая", Status.NEW);
//        inMemoryTaskManager.addNewTask(newTask);
//        inMemoryTaskManager.updateTask(newTask);
//        for (Task task : inMemoryTaskManager.getTasks()) {
//            System.out.println(task);
//        }

        //Печать истории просмотров:
        System.out.println();
        System.out.println("Печать истории просмотров:");
        for (Task history : inMemoryTaskManager.getInMemoryHistoryManager().getHistory()) {
            System.out.println(history.getId());
        }
    }
}

