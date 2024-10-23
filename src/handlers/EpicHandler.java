package handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import manager.ManagerSaveException;
import task.Epic;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static handlers.HttpTaskServer.mg;
import static java.lang.Integer.parseInt;

public class EpicHandler extends BaseHttpHandler {
    Gson gson = HttpTaskServer.getGson();


    @Override
    public void handle(HttpExchange h) throws IOException {
        try {
            try {
                InputStream requestBody = h.getRequestBody();
                String[] p = h.getRequestURI().getPath().split("/");

                switch (h.getRequestMethod()) {
                    case "GET":
                        if (p.length > 3) {
                            if (p[3].equals("subtasks")) {

                                sendText(h, gson.toJson(mg.getListEpic(parseInt(p[2]))));
                            } else {
                                sendNotFound(h, "Некоректный ввод");
                            }
                            break;
                        }
                        if (p.length < 3) {
                            sendText(h, gson.toJson(mg.getListEpics()));
                        } else {
                            if (getId(p[2]).isEmpty()) {
                                sendNotFound(h, "Некоректный ввод id");
                                break;
                            }

                            sendText(h, gson.toJson(mg.getEpic(parseInt(p[2]))));

                        }
                        break;
                    case "POST":
                        JsonElement jsonElement = JsonParser.parseString(new String(requestBody.readAllBytes(), StandardCharsets.UTF_8));
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        Epic epic = new Epic(jsonObject.get("name").getAsString(),
                                jsonObject.get("description").getAsString());
                        if (p.length < 3) {

                            mg.addTask(epic);

                            sendText(h, String.valueOf((mg.getNextId() - 1)));
                        } else {
                            if (getId(p[2]).isEmpty()) {
                                sendNotFound(h, "Некоректный ввод id");
                                break;
                            }

                            epic.setId(parseInt(p[2]));
                            mg.update(epic);
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
                            mg.delEpicById((parseInt(p[2])));
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

