package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.TaskManager;
import ru.practicum.task_tracker.module.Status;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import static ru.practicum.task_tracker.manager.Managers.getDefault;

/*
коммит 4: мы провели рефакторинг методов update task/subtask/epic.
Изменили метод deleteSubtaskById, теперь он так же удаляет ID subtask из эпика.
Изменили работу методов get task/subtask/epic, теперь они возвращают листы.

 */

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = getDefault();

        //Создание задач и присвоение ID
        Task task1 = new Task("Заправка", "заправить топливом авто", Status.NEW);
        Long task1Id = inMemoryTaskManager.addNewTask(task1);

        Task task2 = new Task("Мойка", "помыть авто", Status.NEW);
        Long task2Id = inMemoryTaskManager.addNewTask(task2);

        Epic epic1 = new Epic("ТО авто", "Произвести техническое обслуживание авто");
        long epic1Id = inMemoryTaskManager.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Запчасти", "Купить масло и фильтра", Status.NEW, epic1Id);
        Long subtask1Id = inMemoryTaskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Автосервис", "найти автосервис и сдать авто", Status.NEW, epic1Id);
        Long subtask2Id = inMemoryTaskManager.addNewSubtask(subtask2);

        Epic epic2 = new Epic("Колеса", "поменять колеса на авто");
        Long epic2Id = inMemoryTaskManager.addNewEpic(epic2);

        Subtask subtask3 = new Subtask("Поиск", "Найти в автомагазине колеса необходимого формата",
                Status.DONE, epic2Id);
        Long subtask3Id = inMemoryTaskManager.addNewSubtask(subtask3);


        //Проверка. Печать всех task, epic, subtask
        System.out.println("");
        System.out.println("Проверка. Печатаем task, epic, subtask: ");
        print();
        System.out.println("");

        // Проверка. обновление Таск
        Task newTask = new Task("Тестовая", "Тестовая", Status.NEW);
        inMemoryTaskManager.updateTask(newTask);
        inMemoryTaskManager.updateTask(task2);

        //Проверка. Возврашение Task Subtask Epic
        System.out.println(inMemoryTaskManager.getTaskById(task1Id).getName());
        System.out.println(inMemoryTaskManager.getSubtaskById(subtask1Id).getName());
        System.out.println(inMemoryTaskManager.getEpicById(epic1Id).getName());

        // Проверка. Вывод инфо.
        System.out.println("У эпика с ID: " + epic1Id + " Название: "
                + inMemoryTaskManager.getEpicById(epic1Id).getName());
        System.out.println("У таски с ID: " + task1Id + " Название: "
                + inMemoryTaskManager.getTaskById(task1Id).getName());
        System.out.println("У сабтаски с ID: " + subtask1Id + "название: "
                + inMemoryTaskManager.getSubtaskById(subtask1Id).getName());

        System.out.println();
        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.println(task.getName());
        }

        inMemoryTaskManager.deleteSubtaskById(subtask1Id);
        for (Long subtaskId : epic1.getSubtaskIds()) {
            System.out.println("У эпика с ID: " + epic1Id + " Саьтаски с ID: " + subtaskId);
        }

        System.out.println();
        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.println("Эпик с названием: " + epic.getName());
        }
        System.out.println();
        System.out.println(epic1);

        System.out.println();
        inMemoryTaskManager.getTaskById(task1Id);
        inMemoryTaskManager.getSubtaskById(subtask2Id);
        inMemoryTaskManager.getEpicById(epic1Id);
        inMemoryTaskManager.printHistory();
    }

    public static void print() {
        TaskManager inMemoryTaskManager = getDefault();
        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.println("Task: " + task.getName() + ". Описание: " + task.getDesc()
                    + ". Статус: " + task.getStatus());
        }
        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.println("Epic: " + epic.getName() + ". Описание: " + epic.getDesc()
                    + ". Статус: " + epic.getStatus());
            for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
                for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
                    if (subtask.getId() == epic.getSubtaskIds().get(i)) {
                        System.out.println("Subtask: " + subtask.getName() + ". Описание: " + subtask.getDesc()
                                + ". Статус: " + subtask.getStatus());
                    }
                }
            }
        }
    }
}
