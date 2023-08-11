package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.Manager;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;

public class Main {
    static Manager manager = new Manager();

    public static void main(String[] args) {

        //Создание задач и присвоение ID
        Task task1 = new Task("Заправка", "заправить топливом авто", "NEW");
        Long task1Id = manager.addNewTask(task1);

        Task task2 = new Task("Мойка", "помыть авто", "NEW");
        Long task2Id = manager.addNewTask(task2);

        Epic epic1 = new Epic("ТО авто", "Произвести техническое обслуживание авто");
        long epic1Id = manager.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Запчасти", "Купить масло и фильтра", "NEW", epic1Id);
        Long subtask1Id = manager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Автосервис", "найти автосервис и сдать авто", "NEW", epic1Id);
        Long subtask2Id = manager.addNewSubtask(subtask2);

        Epic epic2 = new Epic("Колеса", "поменять колеса на авто");
        Long epic2Id = manager.addNewEpic(epic2);

        Subtask subtask3 = new Subtask("Поиск", "Найти в автомагазине колеса необходимого формата", "NEW", epic2Id);
        Long subtask3Id = manager.addNewSubtask(subtask3);


        //Проверка. Печать всех task, epic, subtask
        System.out.println("");
        System.out.println("Проверка. Печатаем task, epic, subtask: ");
        print();
        System.out.println("");
/*
        // Проверка. Обновление статусов.
        System.out.println("Проверка. Обновления статусов:");
        manager.updateTask(task1);
        manager.updateTask(task1);
        manager.updateTask(task2);
        manager.updateTask(task2);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        manager.updateSubtask(subtask2);
        manager.updateSubtask(subtask3);
        print();
        System.out.println("");

        // Проверка. Удаление.
        System.out.println("Проверка. Удаление task, subtask и epic:");
        manager.deleteTask(task1Id);
        manager.deleteEpic(epic2Id);
        manager.deleteSubtask(subtask2Id);
        print();
        System.out.println("");

        // Проверка. Обноление Task.
        System.out.println("Проверка. Обноление Task:");
        Task task3 = new Task("Обклейка", "обклеить фары пленкой", "NEW");
        manager.updateTask(task3);
        print();
        System.out.println("");

        //Проверка. Обновление Epic.
        System.out.println("Проверка. Обновление Epic:");
        Epic epic3 = new Epic("Путешетвие", "продумать путешествие на авто");
        manager.updateEpic(epic3);
        Subtask subtask4 = new Subtask("Сабтаск4", "описание сабтакска4", "NEW", epic1Id);
        manager.updateSubtask(subtask4);
        print();
*/
        // Проверка. обновление Таск
        Task newTask = new Task(task1Id, "Тестовая", "Тестовая", "NEW");
        manager.updateTask(newTask);
        manager.updateTask(task2);

        //Проверка. Возврашение Task Subtask Epic
        System.out.println(manager.getTaskById(task1Id).getName());
        System.out.println(manager.getSubtaskById(subtask1Id).getName());
        System.out.println(manager.getEpicById(epic1Id).getName());

        // Проверка. Вывод инфо.
        System.out.println("У эпика с ID: " + epic1Id + " Название: " + manager.getEpicById(epic1Id).getName());
        System.out.println("У таски с ID: " + task1Id + " Название: " + manager.getTaskById(task1Id).getName());
        System.out.println("У сабтаски с ID: " + subtask1Id + "название: " + manager.getSubtaskById(subtask1Id).getName());

        System.out.println();
        for (Task task : manager.getTasks()) {
            System.out.println(task.getName());
        }

        manager.deleteSubtaskById(subtask1Id);
        for (Long subtaskId : epic1.getSubtaskIds()) {
            System.out.println("У эпика с ID: " + epic1Id + " Саьтаски с ID: " + subtaskId);
        }

        System.out.println();
        for (Epic epic : manager.getEpics()) {
            System.out.println("Эпик с названием: " + epic.getName());
        }

    }

    public static void print() {
        for (Task task : manager.getTasks()) {
            System.out.println("Task: " + task.getName() + ". Описание: " + task.getDesc() + ". Статус: " + task.getStatus());
        }
        for (Epic epic : manager.getEpics()) {
            System.out.println("Epic: " + epic.getName() + ". Описание: " + epic.getDesc() + ". Статус: " + epic.getStatus());
            for (Subtask subtask : manager.getSubtasks()) {
                for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
                    if (subtask.getId() == epic.getSubtaskIds().get(i)) {
                        System.out.println("Subtask: " + subtask.getName() + ". Описание: " + subtask.getDesc() + ". Статус: " + subtask.getStatus());
                    }
                }
            }
        }
    }
}
