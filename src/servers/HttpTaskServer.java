package servers;

import com.sun.net.httpserver.HttpServer;
import servers.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new taskHandler());
        httpServer.createContext("/tasks/epic", new epicHandler());
        httpServer.createContext("/tasks/subtask", new subtaskHandler());
        httpServer.createContext("/tasks/subtask/epic", new subtaskInEpicHandler());
        httpServer.createContext("/tasks/history", new taskHistoryHandler());
        httpServer.createContext("/tasks", new taskSHandler());
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void start() {
        httpServer.start();
    }
}
