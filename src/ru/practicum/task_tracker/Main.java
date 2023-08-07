package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.TaskTracker;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskTracker taskTracker = new TaskTracker();

        //Создание задач и присвоение ID
        Task task1 = new Task("Заправка", "заправить топливом авто", "NEW");
        Long task1Id = taskTracker.addNewTask(task1);

        Task task2 = new Task("Мойка", "помыть авто", "NEW");
        Long task2Id = taskTracker.addNewTask(task2);

        Epic epic1 = new Epic("ТО авто", "Произвести техническое обслуживание авто");
        long epic1Id = taskTracker.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Запчасти", "Купить масло и фильтра", "NEW", epic1Id);
        Long subtask1Id = taskTracker.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Автосервис", "найти автосервис и сдать авто", "NEW", epic1Id);
        Long subtask2Id = taskTracker.addNewSubtask(subtask2);

        Epic epic2 = new Epic("Колеса", "поменять колеса на авто");
        Long epic2Id = taskTracker.addNewEpic(epic2);

        Subtask subtask3 = new Subtask("Поиск", "Найти в автомагазине колеса необходимого формата", "NEW", epic2Id);
        Long subtask3Id = taskTracker.addNewSubtask(subtask3);

        //Проверка. Печать всех task, epic, subtask
        System.out.println("");
        System.out.println("Проверка. Печатаем task, epic, subtask: ");
        taskTracker.print();
        System.out.println("");

        // Проверка. Обновление статусов.
        System.out.println("Проверка. Обновления статусов:");
        taskTracker.updateTaskStatus(task1);
        taskTracker.updateTaskStatus(task1);
        taskTracker.updateTaskStatus(task2);
        taskTracker.updateTaskStatus(task2);
        taskTracker.updateSubtaskStatus(subtask1);
        taskTracker.updateSubtaskStatus(subtask2);
        taskTracker.updateSubtaskStatus(subtask2);
        taskTracker.updateSubtaskStatus(subtask3);
        taskTracker.print();
        System.out.println("");

/*
        // Проверка. Удаление.
        System.out.println("Проверка. Удаление task, subtask и epic:");
        taskTracker.deleteTask(task1Id);
        taskTracker.deleteEpic(epic2Id);
        taskTracker.deleteSubtask(subtask2Id);
        taskTracker.print();
        System.out.println("");

        // Проверка. Обноление Task.
        System.out.println("Проверка. Обноление Task:");
        Task task3 = new Task("Обклейка", "обклеить фары пленкой", "NEW");
        taskTracker.updateTaskInfo(task3, task2Id);
        taskTracker.print();
        System.out.println("");

        //Проверка. Обновление Epic.
        System.out.println("Проверка. Обновление Epic:");
        Epic epic3 = new Epic("Путешетвие", "продумать путешествие на авто");
        taskTracker.updateEpicInfo(epic3, epic2Id);
        Subtask subtask4 = new Subtask("Сабтаск4", "описание сабтакска4", "NEW", epic1Id);
        taskTracker.updateSubtaskInfo(subtask4, subtask3Id);
        taskTracker.print();
*/

    }
}