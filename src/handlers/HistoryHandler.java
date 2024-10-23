package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.ManagerSaveException;


import java.io.IOException;


import static handlers.HttpTaskServer.mg;


public class HistoryHandler extends BaseHttpHandler {
    Gson gson = HttpTaskServer.getGson();

    @Override
    public void handle(HttpExchange h) throws IOException {
        try {
            try {
                String[] p = h.getRequestURI().getPath().split("/");
                if (h.getRequestMethod().equals("GET") && (p.length < 3)) {
                    sendText(h, gson.toJson(mg.getHistory()));
                } else {
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
