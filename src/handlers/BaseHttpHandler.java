package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


public class BaseHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


    }

    protected void sendText(HttpExchange h, String text) throws IOException {

        if (h.getRequestMethod().equals("POST")) {
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            try (OutputStream os = h.getResponseBody()) {
                h.sendResponseHeaders(201, 0);
                os.write(text.getBytes(StandardCharsets.UTF_8));
            }
            h.close();

        } else if (h.getRequestMethod().equals("DELETE")) {
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(201, 0);
            h.close();
        } else {
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            try (OutputStream os = h.getResponseBody()) {
                h.sendResponseHeaders(200, 0);
                os.write(text.getBytes(StandardCharsets.UTF_8));
                h.close();
            }
        }


    }


    protected Optional<Integer> getId(String id) {
        try {
            return Optional.of(Integer.parseInt(id));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        try (OutputStream os = h.getResponseBody()) {
            h.sendResponseHeaders(404, 0);
            os.write(text.getBytes(StandardCharsets.UTF_8));
        }
        h.close();

    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        try (OutputStream os = h.getResponseBody()) {
            h.sendResponseHeaders(406, 0);
            os.write("Not Acceptable".getBytes(StandardCharsets.UTF_8));
        }
        h.close();

    }
}





