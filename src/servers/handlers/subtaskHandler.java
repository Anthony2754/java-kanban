package servers.handlers;

import adapters.adapterForLocalDataTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.exceptions.ManagerCreateException;
import managers.exceptions.ManagerDeleteException;
import managers.exceptions.ManagerSaveException;
import managers.Managers;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class subtaskHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new adapterForLocalDataTime())
            .create();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Headers requestHeaders = httpExchange.getRequestHeaders();
        List<String> contentTypeValues = requestHeaders.get("Content-type");
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());
        String key = path.split("=")[1];
        if (key.contains("&")) {
            key = key.replaceFirst("&.*$", "");
        }
        System.out.println(httpExchange.getRequestURI());
        System.out.println("Началась обработка запроса " + path + " от клиента.");
        String response;
        String body;
        int statusCode = 404;

        TaskManager manager = Managers.getDefaultManager(key);

        if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
            switch (method) {
                case "GET":
                    if (path.endsWith("/tasks/subtask?key=" + key)) {

                        try (OutputStream os = httpExchange.getResponseBody()) {

                            response = gson.toJson(manager.getTreeMapSubtask());

                            httpExchange.sendResponseHeaders(200, 0);
                            os.write(response.getBytes(DEFAULT_CHARSET));
                            return;
                        }
                    } else if (path.startsWith("/tasks/subtask?key=" + key + "&id=")) {

                        int id = Integer.parseInt(path.split("=")[2]);
                        try (OutputStream os = httpExchange.getResponseBody()) {

                            try {
                                response = gson.toJson(manager.getSubtaskById(id));
                                statusCode = manager.toJson(key);
                            } catch (InterruptedException e) {
                                httpExchange.sendResponseHeaders(404, 0);
                                response = e.getMessage();
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }

                            httpExchange.sendResponseHeaders(statusCode, 0);
                            os.write(response.getBytes(DEFAULT_CHARSET));
                            return;
                        }
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                        httpExchange.close();
                    }
                    break;
                case "POST":
                    if (path.endsWith("/tasks/subtask?key=" + key)) {

                        try (InputStream is = httpExchange.getRequestBody()) {
                            body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                        }

                        Subtask subTask = gson.fromJson(body, Subtask.class);

                        try {
                            manager.createTask(subTask);
                            statusCode = manager.toJson(key);
                        } catch (ManagerCreateException | ManagerSaveException | InterruptedException e) {

                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(404, 0);
                                response = e.getMessage();
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        }

                        httpExchange.sendResponseHeaders(statusCode, 0);
                        httpExchange.close();
                        return;
                    } else if (path.startsWith("/tasks/subtask?key=" + key + "&id=")) {

                        int id = Integer.parseInt(path.split("=")[2]);
                        try (InputStream is = httpExchange.getRequestBody()) {
                            body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                        }

                        Subtask subtaskData = gson.fromJson(body, Subtask.class);
                        Subtask subTask = new Subtask(
                                id
                                , subtaskData.getEpicId()
                                , subtaskData.getName()
                                , subtaskData.getDescription()
                                , subtaskData.getStatus()
                                , subtaskData.getStartTime()
                                , subtaskData.getDuration());
                        try {
                            manager.subtaskUpdate(subTask);
                            statusCode = manager.toJson(key);
                        } catch (ManagerCreateException | InterruptedException e) {

                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(404, 0);
                                response = e.getMessage();
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        }

                        httpExchange.sendResponseHeaders(statusCode, 0);
                        httpExchange.close();
                        return;
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                        httpExchange.close();
                    }
                    break;
                case "DELETE":
                    if (path.endsWith("/tasks/subtask?key=" + key)) {

                        try {
                            manager.deletingAllSubtasks();
                            statusCode = manager.toJson(key);
                        } catch (ManagerDeleteException | InterruptedException e) {

                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(404, 0);
                                response = e.getMessage();
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        }

                        httpExchange.sendResponseHeaders(statusCode, 0);
                        httpExchange.close();
                        return;
                    } else if (path.startsWith("/tasks/subtask?key=" + key + "&id=")) {

                        int id = Integer.parseInt(path.split("=")[2]);
                        try {
                            manager.deleteSubtaskById(id);
                            statusCode = manager.toJson(key);
                        } catch (ManagerDeleteException | InterruptedException e) {

                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(404, 0);
                                response = e.getMessage();
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        }

                        httpExchange.sendResponseHeaders(statusCode, 0);
                        httpExchange.close();
                        return;
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                        httpExchange.close();
                    }
                    break;
                default:
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        httpExchange.sendResponseHeaders(404, 0);
                        response = "Используйте следующие методы: GET | PUT | DELETE";
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
            }
        }
    }
}