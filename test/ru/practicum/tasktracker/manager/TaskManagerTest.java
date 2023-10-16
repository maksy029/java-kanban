package ru.practicum.tasktracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.tasktracker.models.Status;
import ru.practicum.tasktracker.tasks.Epic;
import ru.practicum.tasktracker.tasks.Subtask;
import ru.practicum.tasktracker.tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected TaskManager taskManager;

    @BeforeEach
    void setTaskManager() {
        taskManager = new InMemoryTaskManager();
    }

    @DisplayName("Тест создания Task.")
    @Test
    protected void testAddNewTask() {
        Task task = new Task("Таска1", "Описание Таски1", Status.NEW
                , 60, LocalDateTime.of(2023, 9, 20, 12, 0));
        final Long taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @DisplayName("Тест обновления Task.")
    @Test
    protected void testUpdateTask() {
        Task task = new Task("Таска1", "Описание Таски1", Status.NEW
                , 60, LocalDateTime.of(2023, 9, 20, 12, 0));
        Long task1Id = taskManager.addNewTask(task);

        task.setName("ТаскаUp");
        task.setDesc("Описание ТаскиUp");
        task.setStatus(Status.DONE);

        taskManager.updateTask(task);
        List<Task> tasks = taskManager.getTasks();

        assertEquals("ТаскаUp", tasks.get(0).getName(), "Имя таски не обновилось");
        assertEquals("Описание ТаскиUp", tasks.get(0).getDesc(), "Описание таски не обновилось");
        assertEquals(Status.DONE, tasks.get(0).getStatus(), "Статус таски не обновился");
    }

    @DisplayName("Тест удаления Task.")
    @Test
    protected void testDeleteTask() {
        Task task1 = new Task("Таска1", "Описание Таски1", Status.NEW
                , 60, LocalDateTime.of(2023, 9, 20, 12, 0));
        Long task1Id = taskManager.addNewTask(task1);

        Task task2 = new Task("Таска2", "Описание Таски2", Status.DONE
                , 60, LocalDateTime.of(2023, 10, 21, 12, 0));
        Long task2Id = taskManager.addNewTask(task2);

        taskManager.deleteTask(task1Id);

        final List<Task> tasks = taskManager.getTasks();

        assertEquals(1, tasks.size(), "Список задач не пустой");
    }

    @DisplayName("Тест удаления всех Task.")
    @Test
    protected void testDeleteAllTask() {
        Task task1 = new Task("Таска1", "Описание Таски1", Status.NEW
                , 60, LocalDateTime.of(2023, 9, 20, 12, 0));
        Long task1Id = taskManager.addNewTask(task1);

        Task task2 = new Task("Таска2", "Описание Таски2", Status.DONE
                , 60, LocalDateTime.of(2023, 10, 21, 12, 0));
        Long task2Id = taskManager.addNewTask(task2);

        taskManager.deleteAllTask();

        final List<Task> tasks = taskManager.getTasks();

        assertEquals(0, tasks.size(), "Список задач не пустой");
    }

    @DisplayName("Тест создания Epic, без сабтасок")
    @Test
    protected void testAddEpicWithOutSubtask() {
        Epic epic = new Epic("Эпик1", "Описание Эпика1");
        Long epicId = taskManager.addNewEpic(epic);

        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
        assertNull(epic.getStartTime(), "СтартТайм эпика не нулевой");
    }

    @DisplayName("Тест обновления Epic.")
    @Test
    protected void testUpdateEpic() {
        Epic epic = new Epic("Эпик1", "Описание Эпика1");
        Long epicId = taskManager.addNewEpic(epic);

        epic.setName("ЭпикUp");
        epic.setDesc("Описание ЭпикаUp");

        taskManager.updateEpic(epic);

        assertEquals("ЭпикUp", taskManager.getEpics().get(0).getName(), "Имя Эпика не обновилось");
    }

    @DisplayName("Тест создания Epic, с сабтасками")
    @Test
    protected void testAddEpicWithSubtask() {
        Epic epic = new Epic("Эпик1", "Описание Эпика1");
        Long epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.DONE, epicId
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtask1Id = taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаска2", "Описание Сабтаски2", Status.DONE, epicId
                , 60, LocalDateTime.of(2023, 12, 23, 12, 0));
        Long subtask2Id = taskManager.addNewSubtask(subtask2);

        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
        assertEquals(Status.DONE, savedEpic.getStatus(), "Cтатус эпика некорректный");
        assertEquals(epic.getStartTime(), subtask1.getStartTime(), "СтартТайм эпика некорректный");
    }

    @DisplayName("Тест удаления Epic")
    @Test
    protected void testDeleteEpicWithSubtask() {
        Epic epic = new Epic("Эпик1", "Описание Эпика1");
        Long epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.DONE, epicId
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtask1Id = taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаска2", "Описание Сабтаски2", Status.DONE, epicId
                , 60, LocalDateTime.of(2023, 12, 23, 12, 0));
        Long subtask2Id = taskManager.addNewSubtask(subtask2);

        taskManager.deleteEpic(epicId);

        final List<Epic> epics = taskManager.getEpics();

        assertEquals(0, epics.size(), "Cписок эпиков не пустой");
    }

    @DisplayName("Тест удаления всех Epic")
    @Test
    protected void testDeleteALLEpic() {
        Epic epic1 = new Epic("Эпик1", "Описание Эпика1");
        Long epic1Id = taskManager.addNewEpic(epic1);

        Epic epic2 = new Epic("Эпик1", "Описание Эпика1");
        Long epic2Id = taskManager.addNewEpic(epic2);

        taskManager.deleteAllEpic();

        final List<Epic> epics = taskManager.getEpics();

        assertEquals(0, epics.size(), "Cписок эпиков не пустой");
    }

    @DisplayName("Тест расчета статуса эпика")
    @Test
    protected void testStatusEpicWithSubtask() {
        Epic epic = new Epic("Эпик1", "Описание Эпика1");
        Long epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.DONE, epicId
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtask1Id = taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаска2", "Описание Сабтаски2", Status.DONE, epicId
                , 60, LocalDateTime.of(2023, 12, 23, 12, 0));
        Long subtask2Id = taskManager.addNewSubtask(subtask2);

        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertEquals(Status.DONE, savedEpic.getStatus(), "Cтатус эпика некорректный");
    }

    @DisplayName("Тест расчета startTime, duration, endTime эпика")
    @Test
    protected void testDurationEpicWithSubtask() {
        Epic epic = new Epic("Эпик1", "Описание Эпика1");
        Long epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.DONE, epicId
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtask1Id = taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаска2", "Описание Сабтаски2", Status.DONE, epicId
                , 60, LocalDateTime.of(2023, 12, 23, 12, 0));
        Long subtask2Id = taskManager.addNewSubtask(subtask2);

        final Epic savedEpic = taskManager.getEpicById(epicId);
        final Subtask savedSubtask1 = taskManager.getSubtaskById(subtask1Id);
        final Subtask savedSubtask2 = taskManager.getSubtaskById(subtask2Id);

        assertEquals(savedEpic.getStartTime(), savedSubtask1.getStartTime(), "startTime эпика некорректный");
        assertEquals(savedEpic.getDuration(), savedSubtask1.getDuration() + savedSubtask2.getDuration()
                , "duration Эпика некорректный");
        assertEquals(savedEpic.getEndTime(), savedSubtask2.getEndTime(), "endTime эпика некорректный");
    }

    @DisplayName("Тест создания Subtask.")
    @Test
    protected void testAddSubtask() {
        Epic epic = new Epic("Эпик1", "Описание Эпика1");
        Long epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.IN_PROGRESS, epicId
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtaskId = taskManager.addNewSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Cабтаск не найден.");
        assertEquals(subtask, savedSubtask, "Сабтаски не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Сабтаски не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество сабтасков.");
        assertEquals(subtask, subtasks.get(0), "Сабтаски не совпадают.");
        assertEquals(epic.getId(), subtask.getEpicId(), "У сабтаски нет эпика");
    }

    @DisplayName("Тест обновления Subtask.")
    @Test
    protected void testUpdateSubtask() {
        Epic epic = new Epic("Эпик1", "Описание Эпика1");
        Long epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.IN_PROGRESS, epicId
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtaskId = taskManager.addNewSubtask(subtask);
        subtask.setName("CабтаскаUp");
        subtask.setDesc("Описание СбатаскиUp");
        subtask.setStatus(Status.DONE);

        taskManager.updateSubtask(subtask);

        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals("CабтаскаUp", subtasks.get(0).getName(), "Имя Сабтаски не обновилось");
    }

    @DisplayName("Тест частичного удаления сабтасок")
    @Test
    protected void testDeleteSubtask() {
        Epic epic = new Epic("Эпик1", "Описание Эпика1");
        Long epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.DONE, epicId
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtask1Id = taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаска2", "Описание Сабтаски2", Status.DONE, epicId
                , 60, LocalDateTime.of(2023, 12, 23, 12, 0));
        Long subtask2Id = taskManager.addNewSubtask(subtask2);

        taskManager.deleteSubtaskById(subtask1Id);

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertEquals(1, subtasks.size(), "Cписок сабтасок больше 1го");
    }

    @DisplayName("Тест удаления всех сабтасок")
    @Test
    protected void testDeleteAllSubtask() {
        Epic epic = new Epic("Эпик1", "Описание Эпика1");
        Long epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.DONE, epicId
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtask1Id = taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаска2", "Описание Сабтаски2", Status.DONE, epicId
                , 60, LocalDateTime.of(2023, 12, 23, 12, 0));
        Long subtask2Id = taskManager.addNewSubtask(subtask2);

        taskManager.deleteAllSubtask();

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertEquals(0, subtasks.size(), "Cписок сабтасок непустой");
    }

    @DisplayName("Тест удаления всех тасок, сабтасок и эпиков")
    @Test
    protected void testDeleteAll() {
        Task task1 = new Task("Таска1", "Описание Таски1", Status.NEW
                , 60, LocalDateTime.of(2023, 9, 20, 12, 0));
        Long task1Id = taskManager.addNewTask(task1);

        Epic epic1 = new Epic("Эпик1", "Описание Эпика1");
        Long epic1Id = taskManager.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Сабтаска1", "Описание Сабтаски1", Status.IN_PROGRESS, epic1Id
                , 60, LocalDateTime.of(2023, 11, 22, 12, 0));
        Long subtask1Id = taskManager.addNewSubtask(subtask1);

        taskManager.deleteAll();

        final List<Task> tasks = taskManager.getTasks();
        final List<Subtask> subtasks = taskManager.getSubtasks();
        final List<Epic> epics = taskManager.getEpics();

        assertEquals(0, tasks.size(), "Cписок тасок непустой");
        assertEquals(0, subtasks.size(), "Cписок сабтасок непустой");
        assertEquals(0, epics.size(), "Cписок эпиков непустой");
    }

    @DisplayName("Тест возврат отсрортированного по приоритету листа задач")
    @Test
    protected void testGetPrioritizedTasks() {
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
        List<Task> prioritizedList = taskManager.getPrioritizedTasks();

        assertNotNull(prioritizedList, "Лист приоритетов не пустой");
        assertEquals(5, prioritizedList.size(), "Лист приоритетов не возвращается");
    }
}
