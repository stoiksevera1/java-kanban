package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.ManagerSaveException;
import task.Subtask;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static handlers.HttpTaskServer.mg;
import static java.lang.Integer.parseInt;

public class SubtasksHandler extends BaseHttpHandler {

    Gson gson = HttpTaskServer.getGson();

    @Override
    public void handle(HttpExchange h) throws IOException {
        try {
            try {
                InputStream requestBody = h.getRequestBody();
                String[] p = h.getRequestURI().getPath().split("/");

                switch (h.getRequestMethod()) {
                    case "GET":
                        if (p.length < 3) {
                            sendText(h, gson.toJson(mg.getListSubTasks()));
                        } else {
                            if (getId(p[2]).isEmpty()) {
                                sendNotFound(h, "Некоректный ввод id");

                            }
                            sendText(h, gson.toJson(mg.getSubTask(parseInt(p[2]))));

                        }
                        break;
                    case "POST":
                        if (p.length < 3) {
                            mg.addTask(gson.fromJson(new String(requestBody.readAllBytes(), StandardCharsets.UTF_8),
                                    Subtask.class));
                            sendText(h, h.getResponseBody().toString());
                            break;
                        } else {
                            if (getId(p[2]).isEmpty()) {
                                sendNotFound(h, "Некоректный ввод id");
                                break;
                            }
                            Subtask subTask = gson.fromJson(new String(requestBody.readAllBytes(), StandardCharsets.UTF_8),
                                    Subtask.class);
                            subTask.setId(parseInt(p[2]));
                            mg.update(subTask);
                            sendText(h, h.getResponseBody().toString());
                        }
                        break;
                    case "DELETE":
                        if (p.length < 3) {
                            sendNotFound(h, "id не введен");
                        } else {
                            if (getId(p[2]).isEmpty()) {
                                sendNotFound(h, "Некоректный ввод id");
                                break;
                            }
                            mg.delSubTaskById((parseInt(p[2])));
                            sendText(h, h.getResponseBody().toString());
                        }
                        break;
                    default:
                        sendNotFound(h, "Неизвестный запрос.");
                }
            } catch (NullPointerException e) {
                sendNotFound(h, "Not Found");
            }
        } catch (ManagerSaveException exception) {
            sendHasInteractions(h);
        }
    }

}

