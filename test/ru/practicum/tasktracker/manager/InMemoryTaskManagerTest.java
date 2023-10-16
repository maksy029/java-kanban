package ru.practicum.tasktracker.manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.tasktracker.models.Status;
import ru.practicum.tasktracker.tasks.Epic;
import ru.practicum.tasktracker.tasks.Subtask;
import ru.practicum.tasktracker.tasks.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @DisplayName("Тест получени списка Тасок")
    @Test
    protected void testGetTasks() {
        taskManager.addNewTask(new Task("Таска1", "Описание таски1", Status.NEW));

        assertEquals(1, taskManager.getTasks().size(), "Cписок тасок не возвращается");
    }

    @DisplayName("Тест получения списка Эпиков")
    @Test
    protected void testGetEpics() {
        taskManager.addNewEpic(new Epic("Эпик1", "Описание эпика1"));

        assertEquals(1, taskManager.getEpics().size(), "Список эпиков не возвращается");
    }

    @DisplayName("Тест получения списка Сабтасок")
    @Test
    protected void testGetSubtask() {
        Epic epic = new Epic("Эпик1", "Описание эпика1");
        Long epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.IN_PROGRESS, epicId
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtaskId = taskManager.addNewSubtask(subtask);

        assertEquals(1, taskManager.getSubtasks().size(), "Список сабтасок не возвращается");
        assertEquals(subtask, taskManager.getSubtaskById(subtaskId), "Сабтаска по id не возвращается");
        assertEquals(epic, taskManager.getEpicById(epicId), "Эпик по id не возвращается");
    }
}

