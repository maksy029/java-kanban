package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.HistoryManager;
import ru.practicum.task_tracker.manager.TaskManager;
import ru.practicum.task_tracker.models.Status;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import static ru.practicum.task_tracker.manager.Managers.getDefault;
import static ru.practicum.task_tracker.manager.Managers.getDefaultHistory;

/*
коммит 2 по ТЗ5:
Доработали замечания по прошлым спринтам(ТЗ3):
В классе InMemoryTaskManager добавили методы удаления всех Task, Subtask, Epic и их историю просмотра
 */

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = getDefault();
        HistoryManager inMemoryHistoryManager = getDefaultHistory();

        //Создание задач и присвоение ID
        Task task1 = new Task("Таск1", "описание Таск1", Status.NEW);
        Long task1Id = inMemoryTaskManager.addNewTask(task1);

        Task task2 = new Task("Таск2", "описание Таск2", Status.NEW);
        Long task2Id = inMemoryTaskManager.addNewTask(task2);

        Epic epic1 = new Epic("Эпик1", "Описание Эпик1");
        long epic1Id = inMemoryTaskManager.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Сабтаск1", "Описание Сабтаск1", Status.NEW, epic1Id);
        Long subtask1Id = inMemoryTaskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаск2", "Описание Сабтаск2", Status.NEW, epic1Id);
        Long subtask2Id = inMemoryTaskManager.addNewSubtask(subtask2);

        Subtask subtask3 = new Subtask("Сабтаск3", "Описание Сабтаск3", Status.NEW, epic1Id);
        Long subtask3Id = inMemoryTaskManager.addNewSubtask(subtask3);

        Epic epic2 = new Epic("Эпик2", "Описание Эпик2");
        Long epic2Id = inMemoryTaskManager.addNewEpic(epic2);

        //Проверка. Печать всех task, epic, subtask
        System.out.println();
        System.out.println("Проверка. Печатаем task, epic, subtask: ");
        print();

        // Проверка. Обновление Таск
        System.out.println();
        System.out.println("Проверка. Обновление Таск");
        Task newTask = new Task("Тестовая", "Тестовая", Status.NEW);
        inMemoryTaskManager.updateTask(newTask);
        inMemoryTaskManager.updateTask(task2);

        //Проверка. Возврашение Task Subtask Epic
        System.out.println();
        System.out.println("Проверка. Возврашение Task Subtask Epic");
        System.out.println(inMemoryTaskManager.getTaskById(task1Id).getName());
        System.out.println(inMemoryTaskManager.getSubtaskById(subtask1Id).getName());
        System.out.println(inMemoryTaskManager.getEpicById(epic1Id).getName());

        // Проверка. Вывод инфо.
        System.out.println();
        System.out.println("Проверка. Вывод инфо.");
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
            System.out.println("У эпика с ID: " + epic1Id + " Сабтаски с ID: " + subtaskId);
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

        System.out.println();

        for (Task task : inMemoryHistoryManager.getHistory()) {
            if (task == null) {
                System.out.println("Task пустая");
                return;
            }
            System.out.println(task);
        }
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
