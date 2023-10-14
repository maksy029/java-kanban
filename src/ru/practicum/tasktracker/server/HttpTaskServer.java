package ru.practicum.tasktracker.server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import ru.practicum.tasktracker.manager.HttpTaskManager;
import ru.practicum.tasktracker.manager.Managers;
import ru.practicum.tasktracker.models.Endpoint;
import ru.practicum.tasktracker.tasks.Epic;
import ru.practicum.tasktracker.tasks.Subtask;
import ru.practicum.tasktracker.tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final HttpServer server;
    private final HttpTaskManager manager;
    private final Gson gson;

    public HttpTaskServer(HttpTaskManager httpTaskManager) throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
        manager = httpTaskManager;
        gson = Managers.getGson();
    }

    private void handler(HttpExchange h) {
        try {
            Endpoint endpoint = getEndpoint(h);
            String resp;
            Long idQuery;

            switch (endpoint) {
                case GET_TASKS:
                    if (!manager.getTasks().isEmpty()) {
                        resp = gson.toJson(manager.getTasks());
                        writeResponse(h, resp, 200);
                        System.out.println("Список tasks успешно отправлен");
                    } else {
                        writeResponse(h, "Список tasks - пустой", 404);
                        System.out.println("Список tasks - пустой");
                    }
                    break;

                case GET_TASK_BY_ID:
                    idQuery = extractQueryId(h);
                    if (manager.getTaskById(idQuery) != null) {
                        resp = gson.toJson(manager.getTaskById(idQuery));
                        writeResponse(h, resp, 200);
                        System.out.println("Task c id=" + idQuery + " успешно отправлена");
                    } else {
                        writeResponse(h, "Task c id=" + idQuery + " не найдена", 404);
                        System.out.println("Task c id=" + idQuery + " не найдена");
                    }
                    break;

                case POST_TASK:
                    postTask(h);
                    break;

                case DELETE_TASK_BY_ID:
                    idQuery = extractQueryId(h);

                    for (Task managerTask : manager.getTasks()) {
                        if (managerTask.getId().equals(idQuery)) {
                            manager.deleteTask(idQuery);
                            writeResponse(h, "Task c id=" + idQuery + " успешно удалена", 200);
                            System.out.println("Task c id=" + idQuery + " успешно удалена");
                        }
                    }
                    writeResponse(h, "Task c id=" + idQuery + " нет в базе tasks", 404);
                    System.out.println("Task c id=" + idQuery + " нет в базе tasks");

                    break;

                case DELETE_ALL_TASKS:
                    manager.deleteAllTask();
                    writeResponse(h, "Все tasks удалены", 200);
                    System.out.println("Все tasks удалены");
                    break;

                case GET_EPICS:
                    if (!manager.getEpics().isEmpty()) {
                        resp = gson.toJson(manager.getEpics());
                        writeResponse(h, resp, 200);
                        System.out.println("Список epics успешно отправлен");
                    } else {
                        writeResponse(h, "Список epics - пустой", 404);
                        System.out.println("Список epics - пустой");
                    }
                    break;

                case GET_EPIC_BY_ID:
                    idQuery = extractQueryId(h);
                    if (manager.getEpicById(idQuery) != null) {
                        resp = gson.toJson(manager.getEpicById(idQuery));
                        writeResponse(h, resp, 200);
                        System.out.println("Epic c id=" + idQuery + " успешно отправлен");
                    } else {
                        writeResponse(h, "Epic c id=" + idQuery + " не найден", 404);
                        System.out.println("Epic c id=" + idQuery + " не найден");
                    }
                    break;

                case POST_EPIC:
                    postEpic(h);
                    break;

                case DELETE_EPIC_BY_ID:
                    idQuery = extractQueryId(h);

                    for (Epic managerEpic : manager.getEpics()) {
                        if (managerEpic.getId().equals(idQuery)) {
                            manager.deleteEpic(idQuery);
                            writeResponse(h, "Epic c id=" + idQuery + " успешно удален", 200);
                            System.out.println("Epic c id=" + idQuery + " успешно удален");
                        }
                    }
                    writeResponse(h, "Epic c id=" + idQuery + " нет в базе epics", 404);
                    System.out.println("Epic c id=" + idQuery + " нет в базе epics");
                    break;

                case DELETE_ALL_EPICS:
                    manager.deleteAllEpic();
                    writeResponse(h, "Все epics удалены", 200);
                    System.out.println("Все epics удалены");
                    break;

                case GET_SUBTASKS:
                    if (!manager.getSubtasks().isEmpty()) {
                        resp = gson.toJson(manager.getSubtasks());
                        writeResponse(h, resp, 200);
                        System.out.println("Список subtasks успешно отправлен");
                    } else {
                        writeResponse(h, "Список subtasks - пустой", 404);
                        System.out.println("Список subtasks - пустой");
                    }
                    break;

                case GET_SUBTASK_BY_ID:
                    idQuery = extractQueryId(h);
                    if (manager.getSubtaskById(idQuery) != null) {
                        resp = gson.toJson(manager.getSubtaskById(idQuery));
                        writeResponse(h, resp, 200);
                        System.out.println("Subtask с id=" + idQuery + " успешно отправлен");
                    } else {
                        writeResponse(h, "Subtask c id=" + idQuery + " не найден", 404);
                        System.out.println("Subtask c id=" + idQuery + " не найден");
                    }
                    break;

                case POST_SUBTASK:
                    postSubtask(h);
                    break;

                case DELETE_SUBTASK_BY_ID:
                    idQuery = extractQueryId(h);

                    for (Subtask managerSubtask : manager.getSubtasks()) {
                        if (managerSubtask.getId().equals(idQuery)) {
                            manager.deleteSubtaskById(idQuery);
                            writeResponse(h, "Subtask c id=" + idQuery + " успешно удален", 200);
                            System.out.println("Subtask c id=" + idQuery + " успешно удален");
                        }
                    }

                    writeResponse(h, "Subtask c id=" + idQuery + " нет в базе subtasks", 404);
                    System.out.println("Subtask c id=" + idQuery + " нет в базе subtasks");
                    break;

                case DELETE_ALL_SUBTASKS:
                    manager.deleteAllSubtask();
                    writeResponse(h, "Все subtasks удалены", 200);
                    System.out.println("Все subtasks удалены");
                    break;

                case GET_PRIORITIZED_TASKS:
                    if (!manager.getPrioritizedTasks().isEmpty()) {
                        resp = gson.toJson(manager.getPrioritizedTasks());
                        writeResponse(h, resp, 200);
                        System.out.println("Cписок задач в порядке приориета отправлен");
                    } else {
                        writeResponse(h, "Cписок задач в порядке приоритета пустой", 404);
                        System.out.println("Cписок задач в порядке приоритета пустой");
                    }
                    break;

                case GET_EPIC_SUBTASKS_BY_ID:
                    idQuery = extractQueryId(h);
                    if (!manager.getEpicSubtasks(idQuery).isEmpty()) {
                        resp = gson.toJson(manager.getEpicSubtasks(idQuery));
                        writeResponse(h, resp, 200);
                        System.out.println("Список сабтасок эпика с id=" + idQuery + " отправлен");
                    } else {
                        writeResponse(h, "Список сабтасок эпика с id=" + idQuery + " пустой", 404);
                    }
                    break;

                case GET_HISTORY:
                    if (!manager.getInMemoryHistoryManager().getHistory().isEmpty()) {
                        resp = gson.toJson(manager.getInMemoryHistoryManager().getHistory());
                        writeResponse(h, resp, 200);
                        System.out.println("История просмотров отправлена");
                    } else {
                        writeResponse(h, "История просмотров - отсутствует", 404);
                        System.out.println("История просмотров - отсутствует");
                    }
                    break;

                default:
                    writeResponse(h, "Некорректный HTTP запрос, получили - "
                            + h.getRequestMethod() + h.getRequestURI().getPath(), 400);
                    System.out.println("Некорректный HTTP запрос, получили - "
                            + h.getRequestMethod() + h.getRequestURI().getPath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при подготовке ответа на HTTP запрос");
        } finally {
            h.close();
        }
    }

    private Endpoint getEndpoint(HttpExchange h) {
        String requestMethod = h.getRequestMethod();
        String[] pathParts = extractPath(h);
        String query = extractQuery(h);

        if (pathParts.length == 2 && requestMethod.equals("GET") && query == null) {
            return Endpoint.GET_PRIORITIZED_TASKS;
        }

        if (pathParts.length == 3 && query == null) {
            if ((pathParts[2].equals("task")) && requestMethod.equals("GET")) {
                return Endpoint.GET_TASKS;
            }
            if ((pathParts[2].equals("epic")) && requestMethod.equals("GET")) {
                return Endpoint.GET_EPICS;
            }
            if ((pathParts[2].equals("subtask")) && requestMethod.equals("GET")) {
                return Endpoint.GET_SUBTASKS;
            }
            if ((pathParts[2].equals("task")) && requestMethod.equals("POST")) {
                return Endpoint.POST_TASK;
            }
            if ((pathParts[2].equals("epic")) && requestMethod.equals("POST")) {
                return Endpoint.POST_EPIC;
            }
            if ((pathParts[2].equals("subtask")) && requestMethod.equals("POST")) {
                return Endpoint.POST_SUBTASK;
            }
            if (pathParts[2].equals("history") && requestMethod.equals("GET")) {
                return Endpoint.GET_HISTORY;
            }
            if ((pathParts[2].equals("task")) && requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_ALL_TASKS;
            }
            if ((pathParts[2].equals("epic")) && requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_ALL_EPICS;
            }
            if ((pathParts[2].equals("subtask")) && requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_ALL_SUBTASKS;
            }
        }

        if (pathParts.length == 3 && query != null) {
            if ((pathParts[2].equals("task")) && requestMethod.equals("GET")) {
                return Endpoint.GET_TASK_BY_ID;
            }
            if ((pathParts[2].equals("epic")) && requestMethod.equals("GET")) {
                return Endpoint.GET_EPIC_BY_ID;
            }
            if ((pathParts[2].equals("subtask")) && requestMethod.equals("GET")) {
                return Endpoint.GET_SUBTASK_BY_ID;
            }
            if ((pathParts[2].equals("task")) && requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_TASK_BY_ID;
            }
            if ((pathParts[2].equals("epic")) && requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_EPIC_BY_ID;
            }
            if ((pathParts[2].equals("subtask")) && requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_SUBTASK_BY_ID;
            }
        }
        if (pathParts.length == 4 && requestMethod.equals("GET") && (pathParts[2].equals("subtask")
                && query != null)
                && (pathParts[3].equals("epic"))) {
            return Endpoint.GET_EPIC_SUBTASKS_BY_ID;
        }

        return Endpoint.UNKNOWN;
    }

    private String[] extractPath(HttpExchange h) {
        return h.getRequestURI().getPath().split("/");
    }

    private String extractQuery(HttpExchange h) {
        return h.getRequestURI().getQuery();
    }

    private long extractQueryId(HttpExchange h) {
        return Integer.parseInt(h.getRequestURI().getQuery().substring(3));
    }

    private String extractBodyRequest(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }

    private JsonObject extractJsonObject(HttpExchange h, String body) throws IOException {
        JsonElement jsonElement = JsonParser.parseString(body);

        if (!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
            writeResponse(h, "Передано тело не в формате JSON", 400);
            System.out.println("Передано тело не в формате JSON");
        }
        return jsonElement.getAsJsonObject();
    }

    private void postTask(HttpExchange h) throws IOException {
        String body = extractBodyRequest(h);
        Task task = gson.fromJson(body, Task.class);
        JsonObject jsonObject = extractJsonObject(h, body);

        long id = jsonObject.get("id").getAsLong();
        if (id == 0) {
            manager.addNewTask(task);
            writeResponse(h, "Task успешно добавлена", 200);
            System.out.println("Task успешно добавлена");
        } else {
            for (Task managerTask : manager.getTasks()) {
                if (!managerTask.getId().equals(id)) {
                    writeResponse(h, "Task c id=" + id + " в базе не найдена. Если требуется добавить " +
                            "новую задачу укажите в теле запроса задачу в формате JSON c id=0", 400);
                    System.out.println("Task c id=" + id + " в базе не найдена. Если требуется добавить новую задачу "
                            + "укажите в теле запроса задачу в формате JSON c id=0");
                }
            }
            manager.updateTask(task);
            writeResponse(h, "Task c id=" + id + " успешно обновлена", 200);
            System.out.println("Task c id=" + id + " успешно обновлена");
        }
    }

    private void postEpic(HttpExchange h) throws IOException {
        String body = extractBodyRequest(h);
        Epic epic = gson.fromJson(body, Epic.class);
        JsonObject jsonObject = extractJsonObject(h, body);

        long id = jsonObject.get("id").getAsLong();
        if (id == 0) {
            manager.addNewEpic(epic);
            writeResponse(h, "Epic успешно добавлен", 200);
            System.out.println("Epic успешно добавлен");
        } else {
            for (Epic managerEpic : manager.getEpics()) {
                if (!managerEpic.getId().equals(id)) {
                    writeResponse(h, "Epic c id=" + id + " в базе не найден. Если требуется добавить " +
                            "новый эпик укажите в теле запроса эпик в формате JSON c id=0", 400);
                    System.out.println("Epic c id=" + id + " в базе не найден. Если требуется добавить новый эпик "
                            + "укажите в теле запроса эпик в формате JSON c id=0");
                }
            }
            manager.updateEpic(epic);
            writeResponse(h, "Epic c id=" + id + " успешно обновлен", 200);
            System.out.println("Epic c id=" + id + " успешно обновлен");
        }
    }

    private void postSubtask(HttpExchange h) throws IOException {
        String body = extractBodyRequest(h);
        Subtask subtask = gson.fromJson(body, Subtask.class);
        JsonObject jsonObject = extractJsonObject(h, body);

        long id = jsonObject.get("id").getAsLong();
        long epicId = jsonObject.get("epicId").getAsLong();
        if (id == 0) {
            if (epicId == 0) {
                writeResponse(h, "Указан epicId=" + epicId + ", необходим корректный epicId," +
                        "под которым обновляется сабтаска", 400);
                System.out.println("Указан epicId=" + epicId + ", необходим корректный epicId," +
                        "под которым добавляется сабтаска");
            }
            for (Epic epic : manager.getEpics()) {
                if (!epic.getId().equals(epicId)) {
                    writeResponse(h, "Указан epicId=" + epicId + ", которого нет в базе Эпиов, необходим "
                            + "корректный epicId, под которым добавляется новая сабтаска", 400);
                    System.out.println("Указан epicId=" + epicId + ", которого нет в базе Эпиов, необходим "
                            + "корректный epicId, под которым добавляется новая сабтаска");
                }
            }
            manager.addNewSubtask(subtask);
            writeResponse(h, "Новая сабтаска успешно добавлена под эпиком с epicID=" + epicId
                    , 200);
            System.out.println("Новая сабтаска успешно добавлена под эпиком с epicID=" + epicId);
        }

        for (Subtask managerSubtask : manager.getSubtasks()) {
            if (!managerSubtask.getId().equals(id)) {
                writeResponse(h, "Subtask c id=" + id + " нет в базе Сабтасок, " +
                        "Если необходимо добавить новую сабтаск, то необходимо указать сабтаск " +
                        "с id=0", 400);
                System.out.println("Subtask c id=" + id + " нет в базе Сабтасок, " +
                        "Если необходимо добавить новую сабтаск, то необходимо указать сабтаск " +
                        "с id=0");
            }
        }

        for (Epic epic : manager.getEpics()) {
            if (!epic.getId().equals(epicId)) {
                writeResponse(h, "Указан epicId=" + epicId + ", которого нет в базе Эпиов, необходим "
                        + "корректный epicId, под которым обновляется сабтаска", 400);
                System.out.println("Указан epicId=" + epicId + ", которого нет в базе Эпиов, необходим "
                        + "корректный epicId, под которым обновляется сабтаска");
            }
        }
        manager.updateSubtask(subtask);
        writeResponse(h, "Успешно обновлена сабтаска с id=" + id + ", под эпиком с epicId=" + epicId
                , 200);
        System.out.println("Успешно обновлена сабтаска с id=" + id + ", под эпиком с epicId=");
    }


    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    public void start() {
        System.out.println("Запускаем HttpTaskServer на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop(int delay) {
        System.out.println("Сервер c портом: " + PORT + " завершит работу через delay=" + delay);
        server.stop(delay);
    }

}
