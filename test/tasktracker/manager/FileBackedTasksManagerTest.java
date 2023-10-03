package tasktracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.tasktracker.manager.FileBackedTasksManager;
import ru.practicum.tasktracker.models.Status;
import ru.practicum.tasktracker.tasks.Epic;
import ru.practicum.tasktracker.tasks.Subtask;
import ru.practicum.tasktracker.tasks.Task;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private final File file = new File("test/ru/practicum/tasktracker/resources/data.csv");

    @Override
    @BeforeEach
    void setTaskManager() {
        taskManager = new FileBackedTasksManager(file);
    }

    @DisplayName("Тест восстановления менеджера из файла")
    @Test
    protected void testLoadFromFile() {
        Task task1 = new Task("Таска1", "Описание Таски1", Status.NEW
                , 60, LocalDateTime.of(2023, 9, 20, 12, 0));
        Long task1Id = taskManager.addNewTask(task1);

        Task task2 = new Task("Таска2", "Описание Таски2", Status.DONE
                , 60, LocalDateTime.of(2023, 10, 21, 12, 0));
        Long task2Id = taskManager.addNewTask(task2);

        Epic epic1 = new Epic("Эпик1", "Описание Эпика1");
        Long epic1Id = taskManager.addNewEpic(epic1);

        Epic epic2 = new Epic("Эпик2", "Описание Эпика2");
        Long epic2Id = taskManager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.IN_PROGRESS, epic1Id
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtask1Id = taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаска2", "Описание Сабтаски2", Status.DONE, epic2Id
                , 60, LocalDateTime.of(2023, 12, 23, 12, 0));
        Long subtask2Id = taskManager.addNewSubtask(subtask2);

        Subtask subtask3 = new Subtask("Сабтаска3", "Описание Сабтаски3", Status.DONE, epic2Id
                , 60, LocalDateTime.of(2024, 1, 24, 12, 0));
        Long subtask3Id = taskManager.addNewSubtask(subtask3);

        taskManager.getTaskById(task1Id);
        taskManager.getEpicById(epic1Id);
        taskManager.getSubtaskById(subtask1Id);

        FileBackedTasksManager taskManager2 = FileBackedTasksManager.loadFromFile(file);

        assertEquals(taskManager.getEpics().size(), taskManager2.getEpics().size(), "Эпики не восстановились");
        assertEquals(taskManager.getTasks().size(), taskManager2.getTasks().size(), "Таски не восстановились");
        assertEquals(taskManager.getSubtasks().size(), taskManager2.getSubtasks().size(), "Сабтаски не " +
                "восстановились");
    }
}


