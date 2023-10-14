package ru.practicum.tasktracker.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.tasktracker.models.Status;
import ru.practicum.tasktracker.server.KVServer;
import ru.practicum.tasktracker.tasks.Epic;
import ru.practicum.tasktracker.tasks.Subtask;
import ru.practicum.tasktracker.tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer kvServer;

    @BeforeEach
    public void setUP() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @AfterEach
    public void shutDown() {
        kvServer.stop();
    }

    @DisplayName("Тест восстановления менеджера из KVTaskServer")
    @Test
    public void testLoadFromHttpServer() {
        HttpTaskManager manager = new HttpTaskManager();

        //создаем задачи
        Task task1 = new Task("Таска1", "Описание Таски1", Status.NEW
                , 60, LocalDateTime.of(2023, 9, 20, 12, 0));
        Long task1Id = manager.addNewTask(task1);

        Epic epic1 = new Epic("Эпик1", "Описание Эпика1");
        Long epic1Id = manager.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.IN_PROGRESS, epic1Id
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtask1Id = manager.addNewSubtask(subtask1);

        //создаем историю
        manager.getTaskById(task1Id);
        manager.getEpicById(epic1Id);
        manager.getSubtaskById(subtask1Id);

        //создаем менеджер с восстановленными объектами
        HttpTaskManager restoreManager = new HttpTaskManager();
        Task restoreTask1 = restoreManager.getTaskById(task1Id);
        Epic restoreEpic1 = restoreManager.getEpicById(epic1Id);
        Subtask restoreSubtask1 = restoreManager.getSubtaskById(subtask1Id);

        //проверям восстановление задач
        assertEquals(task1, restoreTask1);
        assertEquals(epic1, restoreEpic1);
        assertEquals(subtask1, restoreSubtask1);

        //проверяем восстановление истории
        assertEquals(manager.getInMemoryHistoryManager().getHistory()
                , restoreManager.getInMemoryHistoryManager().getHistory());

        //проверяемм восстановление листов задач
        assertEquals(manager.getTasks(), restoreManager.getTasks());
        assertEquals(manager.getEpics(), restoreManager.getEpics());
        assertEquals(manager.getSubtasks(), restoreManager.getSubtasks());
    }
}