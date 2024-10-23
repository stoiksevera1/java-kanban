package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.sun.net.httpserver.HttpServer;
import manager.*;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskServer {
    private static final int PORT = 8080;

    public static TaskManager mg = new InMemoryTaskManager();

    public HttpTaskServer(TaskManager mg) {
        HttpTaskServer.mg = mg;
    }

    public static HttpServer httpServer;

    public static void main(String[] args) throws IOException {
        new HttpTaskServer(mg);
        start();
    }


    public static void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/subtasks", new SubtasksHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.start();
    }

    public static void stop() {
        httpServer.stop(0);
    }


    public static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                //.excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTimeAdapter())
                .create();
    }


}