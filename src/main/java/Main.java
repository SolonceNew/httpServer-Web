import server.Handler;
import server.Request;
import server.Server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        final var server = new Server();


        // добавление handler'ов (обработчиков)
        server.addHandler("GET", "/classic", new Handler() {
            public void handle(Request request, BufferedOutputStream out) {
                try {
                    var filePath = Path.of(".", "public", request.getPath());
                    var mimeType = Files.probeContentType(filePath);
                    var content = server.template(filePath);
                    out.write(server.okResponse(mimeType, content.length).getBytes());
                    out.write(content);
                    out.flush();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
       
        server.addHandler("POST", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {
                // TODO: handlers code
            }
        });

        server.listen(9999);
    }
}





