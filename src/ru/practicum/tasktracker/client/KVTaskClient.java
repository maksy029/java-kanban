package ru.practicum.tasktracker.client;

import ru.practicum.tasktracker.exception.KVClientException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private final String apiToken;
    private final HttpClient httpClient;

    public KVTaskClient(String url) {
        this.url = url;
        httpClient = HttpClient.newHttpClient();
        apiToken = register();
    }

    public String load(String key) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // При создание  HttpTaskManager проиходит автозагрузка данных из сервера, данным ветвлением принудительно
            // восстанавливаем, даже если сервер пуст. 404 - ошибка КВСервера если не удалось совершить загрузку по ключу
            if (response.statusCode() == 404) {
                return null;
            }

            if (response.statusCode() != 200) {
                throw new KVClientException("Плохой ответ: не 200, а - " + response.statusCode());
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new KVClientException("Не получается сделать запрос");
        }
    }

    public void put(String key, String value) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

            if (response.statusCode() != 200) {
                throw new KVClientException("Плохой ответ: не 200, а - " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new KVClientException("Не получается сделать запрос");
        }
    }

    private String register() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new KVClientException("Плохой ответ: не 200, а - " + response.statusCode());
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new KVClientException("Не получается сделать запрос");
        }
    }
}
