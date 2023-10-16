package ru.practicum.tasktracker.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    private void handler(HttpExchange exchange) {
        try {
            Endpoint endpoint = getEndpoint(exchange);

            switch (endpoint) {
                case GET_TASKS:
                    getTasks(exchange);
                    break;
                case GET_TASK_BY_ID:
                    getTaskById(exchange);
                    break;
                case POST_TASK:
                    postTask(exchange);
                    break;
                case DELETE_TASK_BY_ID:
                    deleteTaskById(exchange);
                    break;
                case DELETE_ALL_TASKS:
                    deleteAllTasks(exchange);
                    break;
                case GET_EPICS:
                    getEpics(exchange);
                    break;
                case GET_EPIC_BY_ID:
                    getEpicById(exchange);
                    break;
                case POST_EPIC:
                    postEpic(exchange);
                    break;
                case DELETE_EPIC_BY_ID:
                    deleteEpicById(exchange);
                    break;
                case DELETE_ALL_EPICS:
                    deleteAllEpics(exchange);
                    break;
                case GET_SUBTASKS:
                    getSubtasks(exchange);
                    break;
                case GET_SUBTASK_BY_ID:
                    getSubtaskById(exchange);
                    break;
                case POST_SUBTASK:
                    postSubtask(exchange);
                    break;
                case DELETE_SUBTASK_BY_ID:
                    deleteSubtaskById(exchange);
                    break;
                case DELETE_ALL_SUBTASKS:
                    deleteAllSubtask(exchange);
                    break;
                case GET_PRIORITIZED_TASKS:
                    getPrioritizedTasks(exchange);
                    break;
                case GET_EPIC_SUBTASKS_BY_ID:
                    getEpicSubtasksById(exchange);
                    break;
                case GET_HISTORY:
                    getHistory(exchange);
                    break;
                default:
                    defaultEndpoint(exchange);
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при подготовке ответа на HTTP запрос");
        } finally {
            exchange.close();
        }
    }

    private void getTasks(HttpExchange exchange) {
        try {
            if (!manager.getTasks().isEmpty()) {
                String resp = gson.toJson(manager.getTasks());
                writeResponse(exchange, resp, 200);
                System.out.println("Список tasks успешно отправлен");
            } else {
                writeResponse(exchange, "Список tasks - пустой", 404);
                System.out.println("Список tasks - пустой");
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе getTasks()");
        }
    }

    private void getTaskById(HttpExchange exchange) {
        try {
            Long idQuery = extractQueryId(exchange);
            if (manager.getTaskById(idQuery) != null) {
                String resp = gson.toJson(manager.getTaskById(idQuery));
                writeResponse(exchange, resp, 200);
                System.out.println("Task c id=" + idQuery + " успешно отправлена");
            } else {
                writeResponse(exchange, "Task c id=" + idQuery + " не найдена", 404);
                System.out.println("Task c id=" + idQuery + " не найдена");
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе getTaskById()");
        }
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

    private void deleteTaskById(HttpExchange exchange) {
        try {
            Long idQuery = extractQueryId(exchange);

            for (Task managerTask : manager.getTasks()) {
                if (managerTask.getId().equals(idQuery)) {
                    manager.deleteTask(idQuery);
                    writeResponse(exchange, "Task c id=" + idQuery + " успешно удалена", 200);
                    System.out.println("Task c id=" + idQuery + " успешно удалена");
                }
            }
            writeResponse(exchange, "Task c id=" + idQuery + " нет в базе tasks", 404);
            System.out.println("Task c id=" + idQuery + " нет в базе tasks");
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе deleteTaskById()");
        }
    }

    private void deleteAllTasks(HttpExchange exchange) {
        try {
            manager.deleteAllTask();
            writeResponse(exchange, "Все tasks удалены", 200);
            System.out.println("Все tasks удалены");
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе deleteAllTasks()");
        }
    }

    private void getEpics(HttpExchange exchange) {
        try {
            if (!manager.getEpics().isEmpty()) {
                String resp = gson.toJson(manager.getEpics());
                writeResponse(exchange, resp, 200);
                System.out.println("Список epics успешно отправлен");
            } else {
                writeResponse(exchange, "Список epics - пустой", 404);
                System.out.println("Список epics - пустой");
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе getEpics()");
        }
    }

    private void getEpicById(HttpExchange exchange) {
        try {
            Long idQuery = extractQueryId(exchange);
            if (manager.getEpicById(idQuery) != null) {
                String resp = gson.toJson(manager.getEpicById(idQuery));
                writeResponse(exchange, resp, 200);
                System.out.println("Epic c id=" + idQuery + " успешно отправлен");
            } else {
                writeResponse(exchange, "Epic c id=" + idQuery + " не найден", 404);
                System.out.println("Epic c id=" + idQuery + " не найден");
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе getEpicById()");
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

    private void deleteEpicById(HttpExchange exchange) {
        try {
            Long idQuery = extractQueryId(exchange);

            for (Epic managerEpic : manager.getEpics()) {
                if (managerEpic.getId().equals(idQuery)) {
                    manager.deleteEpic(idQuery);
                    writeResponse(exchange, "Epic c id=" + idQuery + " успешно удален", 200);
                    System.out.println("Epic c id=" + idQuery + " успешно удален");
                }
            }
            writeResponse(exchange, "Epic c id=" + idQuery + " нет в базе epics", 404);
            System.out.println("Epic c id=" + idQuery + " нет в базе epics");
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе deleteEpicById()");
        }
    }

    private void deleteAllEpics(HttpExchange exchange) {
        try {
            manager.deleteAllEpic();
            writeResponse(exchange, "Все epics удалены", 200);
            System.out.println("Все epics удалены");
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе deleteAllEpics()");
        }
    }

    private void getSubtasks(HttpExchange exchange) {
        try {
            if (!manager.getSubtasks().isEmpty()) {
                String resp = gson.toJson(manager.getSubtasks());
                writeResponse(exchange, resp, 200);
                System.out.println("Список subtasks успешно отправлен");
            } else {
                writeResponse(exchange, "Список subtasks - пустой", 404);
                System.out.println("Список subtasks - пустой");
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе getSubtasks()");
        }
    }

    private void getSubtaskById(HttpExchange exchange) {
        try {
            Long idQuery = extractQueryId(exchange);
            if (manager.getSubtaskById(idQuery) != null) {
                String resp = gson.toJson(manager.getSubtaskById(idQuery));
                writeResponse(exchange, resp, 200);
                System.out.println("Subtask с id=" + idQuery + " успешно отправлен");
            } else {
                writeResponse(exchange, "Subtask c id=" + idQuery + " не найден", 404);
                System.out.println("Subtask c id=" + idQuery + " не найден");
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе getSubtaskById()");
        }
    }

    private void deleteSubtaskById(HttpExchange exchange) {
        try {
            Long idQuery = extractQueryId(exchange);

            for (Subtask managerSubtask : manager.getSubtasks()) {
                if (managerSubtask.getId().equals(idQuery)) {
                    manager.deleteSubtaskById(idQuery);
                    writeResponse(exchange, "Subtask c id=" + idQuery + " успешно удален"
                            , 200);
                    System.out.println("Subtask c id=" + idQuery + " успешно удален");
                }
            }

            writeResponse(exchange, "Subtask c id=" + idQuery + " нет в базе subtasks", 404);
            System.out.println("Subtask c id=" + idQuery + " нет в базе subtasks");
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе deleteSubtaskById()");
        }
    }

    private void deleteAllSubtask(HttpExchange exchange) {
        try {
            manager.deleteAllSubtask();
            writeResponse(exchange, "Все subtasks удалены", 200);
            System.out.println("Все subtasks удалены");
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе deleteAllSubtask()");
        }
    }

    private void getPrioritizedTasks(HttpExchange exchange) {
        try {
            if (!manager.getPrioritizedTasks().isEmpty()) {
                String resp = gson.toJson(manager.getPrioritizedTasks());
                writeResponse(exchange, resp, 200);
                System.out.println("Cписок задач в порядке приориета отправлен");
            } else {
                writeResponse(exchange, "Cписок задач в порядке приоритета пустой", 404);
                System.out.println("Cписок задач в порядке приоритета пустой");
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе getPrioritizedTasks()");
        }
    }

    private void getEpicSubtasksById(HttpExchange exchange) {
        try {
            Long idQuery = extractQueryId(exchange);
            if (!manager.getEpicSubtasks(idQuery).isEmpty()) {
                String resp = gson.toJson(manager.getEpicSubtasks(idQuery));
                writeResponse(exchange, resp, 200);
                System.out.println("Список сабтасок эпика с id=" + idQuery + " отправлен");
            } else {
                writeResponse(exchange, "Список сабтасок эпика с id=" + idQuery + " пустой"
                        , 404);
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе getEpicSubtasksById()");
        }
    }

    private void getHistory(HttpExchange exchange) {
        try {
            if (!manager.getInMemoryHistoryManager().getHistory().isEmpty()) {
                String resp = gson.toJson(manager.getInMemoryHistoryManager().getHistory());
                writeResponse(exchange, resp, 200);
                System.out.println("История просмотров отправлена");
            } else {
                writeResponse(exchange, "История просмотров - отсутствует", 404);
                System.out.println("История просмотров - отсутствует");
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе getHistory()");
        }
    }

    private void defaultEndpoint(HttpExchange exchange) throws IOException {
        writeResponse(exchange, "Некорректный HTTP запрос, получили - "
                + exchange.getRequestMethod() + exchange.getRequestURI().getPath(), 400);
        System.out.println("Некорректный HTTP запрос, получили - "
                + exchange.getRequestMethod() + exchange.getRequestURI().getPath());
    }

    private Endpoint getEndpoint(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        String[] pathParts = extractPath(exchange);
        String query = extractQuery(exchange);

        if (requestMethod.equals("GET")) {
            return getGetEndpoints(pathParts, query);
        }

        if (requestMethod.equals("POST")) {
            return getPostEndpoints(pathParts, query);
        }

        if (requestMethod.equals("DELETE")) {
            return getDeleteEndpoints(pathParts, query);
        }

        return Endpoint.UNKNOWN;
    }

    private Endpoint getGetEndpoints(String[] pathParts, String query) {

        if (pathParts.length == 2 && query == null) {
            return Endpoint.GET_PRIORITIZED_TASKS;
        }

        if (pathParts.length == 3 && query == null) {
            if ((pathParts[2].equals("task"))) {
                return Endpoint.GET_TASKS;
            }
            if ((pathParts[2].equals("epic"))) {
                return Endpoint.GET_EPICS;
            }
            if ((pathParts[2].equals("subtask"))) {
                return Endpoint.GET_SUBTASKS;
            }
            if (pathParts[2].equals("history")) {
                return Endpoint.GET_HISTORY;
            }
        }

        if (pathParts.length == 3 && query != null) {
            if ((pathParts[2].equals("task"))) {
                return Endpoint.GET_TASK_BY_ID;
            }
            if ((pathParts[2].equals("epic"))) {
                return Endpoint.GET_EPIC_BY_ID;
            }
            if ((pathParts[2].equals("subtask"))) {
                return Endpoint.GET_SUBTASK_BY_ID;
            }
        }

        if (pathParts.length == 4 && pathParts[2].equals("subtask") && query != null && pathParts[3].equals("epic")) {
            return Endpoint.GET_EPIC_SUBTASKS_BY_ID;
        }
        return Endpoint.UNKNOWN;
    }

    private Endpoint getPostEndpoints(String[] pathParts, String query) {

        if (pathParts.length == 3 && query == null) {
            if ((pathParts[2].equals("task"))) {
                return Endpoint.POST_TASK;
            }
            if ((pathParts[2].equals("epic"))) {
                return Endpoint.POST_EPIC;
            }
            if ((pathParts[2].equals("subtask"))) {
                return Endpoint.POST_SUBTASK;
            }
        }
        return Endpoint.UNKNOWN;
    }

    private Endpoint getDeleteEndpoints(String[] pathParts, String query) {

        if (pathParts.length == 3 && query == null) {
            if ((pathParts[2].equals("task"))) {
                return Endpoint.DELETE_ALL_TASKS;
            }
            if ((pathParts[2].equals("epic"))) {
                return Endpoint.DELETE_ALL_EPICS;
            }
            if ((pathParts[2].equals("subtask"))) {
                return Endpoint.DELETE_ALL_SUBTASKS;
            }
        }

        if (pathParts.length == 3 && query != null) {
            if ((pathParts[2].equals("task"))) {
                return Endpoint.DELETE_TASK_BY_ID;
            }
            if ((pathParts[2].equals("epic"))) {
                return Endpoint.DELETE_EPIC_BY_ID;
            }
            if ((pathParts[2].equals("subtask"))) {
                return Endpoint.DELETE_SUBTASK_BY_ID;
            }
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
