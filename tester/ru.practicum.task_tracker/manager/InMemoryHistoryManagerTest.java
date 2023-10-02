package ru.practicum.task_tracker.tester;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.manager.HistoryManager;
import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.manager.TaskManager;
import ru.practicum.task_tracker.models.Status;
import ru.practicum.task_tracker.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    TaskManager taskManager;

    @BeforeEach
    void setTaskmanager() {
        historyManager = Managers.getDefaultHistory();
        taskManager = Managers.getDefault();
    }

    @DisplayName("Тест добавления в историю.")
    @Test
    protected void testAdd() {
        Task task = new Task("Таск1", "Описание Таски1", Status.NEW);
        final Long taskId = taskManager.addNewTask(task);

        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История пустая.");
    }

    @DisplayName("Тест удаления из истории.")
    @Test
    protected void testRemove() {
        Task task = new Task("Таск1", "Описание Таски1", Status.NEW);
        final Long taskId = taskManager.addNewTask(task);

        historyManager.remove(taskId);

        final List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая.");
    }
}
