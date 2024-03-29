package servers.handlers;

import adapters.AdapterForLocalDataTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.Managers;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SubtaskInEpicHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new AdapterForLocalDataTime())
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
        int statusCode = 404;

        TaskManager manager = Managers.getDefaultManager(key);

        if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
            if ("GET".equals(method)) {
                if (path.startsWith("/tasks/subtask/epic?key=" + key + "&id=")) {

                    int id = Integer.parseInt(path.split("=")[2]);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        List<Subtask> arrayListSubtask = new ArrayList<>();

                        try {
                            for (Integer subtaskId : manager.getEpicById(id).getArrayListSubtaskId()) {
                                for (Subtask subTask : manager.getTreeMapSubtask()) {
                                    if (subtaskId == subTask.getId()) {
                                        arrayListSubtask.add(subTask);
                                    }
                                }
                            }
                            response = gson.toJson(arrayListSubtask);
                            statusCode = manager.toJson(key);
                        } catch (InterruptedException exc) {
                            httpExchange.sendResponseHeaders(404, 0);
                            response = exc.getMessage();
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }

                        httpExchange.sendResponseHeaders(statusCode, 0);
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                } else {
                    httpExchange.sendResponseHeaders(404, 0);
                    httpExchange.close();
                }
            } else {
                try (OutputStream outputStream = httpExchange.getResponseBody()) {
                    httpExchange.sendResponseHeaders(404, 0);
                    response = "Используйте следующие метод: GET";
                    outputStream.write(response.getBytes(DEFAULT_CHARSET));
                }
            }
        }
    }
}