package server;

import server.Handler;
import server.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    private final ExecutorService executorService = Executors.newFixedThreadPool(64);
    private static final int LIMIT_CONNECTIONS = 64;
    private final Map<String, Map<String, Handler>> handlers = new HashMap<>();

    public Server() {

    }


    public void listen(int port) {
        try (final ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                final var socket = serverSocket.accept();
                executorService.submit(() -> connect(socket));
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // если ключа не существует, то в мап добавляем новую пару ключ&мапа
    //если ключ(метод) уже есть, то достаем ключ и кладем в его карзину новую мапу ссылка&handler
    public void addHandler(String method, String path, Handler handler) {
        if (!handlers.containsKey(method)) handlers.put(method, new HashMap<>());
        handlers.get(method).put(path, handler);

    }

    public void connect(Socket socket) {
        try (
                socket;
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream())
        ) {
            Request request = new Request();
            // read only request line for simplicity
            // must be in form GET /path HTTP/1.1
            final String[] parts = getResponsePartsFromRequest(in);
            final var path = parts[1];

            if (parts.length != 3) {

            } else if (!validPaths.contains(path)) {
                errorResponse(out);
            } else {
                request.setPath(path);
                request.setMethod(parts[0]);
                getTrueResponse(request, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getResponsePartsFromRequest(BufferedReader in) throws IOException {
        final var requestLine = in.readLine();
        final var parts = requestLine.split(" ");
        return parts;
    }

    private void errorResponse(BufferedOutputStream out) throws IOException {
        String response = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Length: 0\r\n" +
                "Connection: close\r\n" +
                "\r\n";
        out.write(response.getBytes());
        out.flush();
    }

    public String okResponse(String mimeType, long length) {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + mimeType + "\r\n" +
                "Content-Length: " + length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";

    }

    public void getTrueResponse(Request request, Socket socket) throws IOException {
        var path = request.getPath();
        var method = request.getMethod();
        if (handlers.containsKey(method)) {
            var handlerMap = handlers.get(path);
            if (handlerMap.containsKey(path)) {
                var handler = handlerMap.get(path);
                try (BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {
                    handler.handle(request, out);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    public byte[] template(Path filePath) throws IOException {
        var template = Files.readString(filePath);
        return template.replace(
                "{time}",
                LocalDateTime.now().toString()
        ).getBytes();
    }
}



