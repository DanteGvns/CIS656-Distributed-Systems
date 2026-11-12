import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

public class WebServer {
    private static final int portNum = 8088;

    public static void main(String[] args){
        Path folderRoot = Paths.get(".").toAbsolutePath().normalize();

        try {
            HttpServer listener = HttpServer.create(new InetSocketAddress(portNum), 0);
            listener.createContext("/", request ->handleClient(request, folderRoot));
            listener.setExecutor(Executors.newCachedThreadPool());
            listener.start();
            System.out.println("Web server listening at http://localhost:" + portNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(HttpExchange request, Path folderRoot) {
        int status = 404;
        String contentType = "text/plain; charset=UTF-8";
        byte[] body = "404 Not Found".getBytes(StandardCharsets.UTF_8);

        try {
            if (!request.getRequestMethod().equals("GET")) {
                status = 405;
                body = "405 Method Not Allowed".getBytes(StandardCharsets.UTF_8);
                request.getResponseHeaders().set("Allow", "GET");
            } else {
                String requestedFile = request.getRequestURI().getPath();
                Path file = folderRoot.resolve("hello.html");
                if ("/hello.html".equals(requestedFile) && Files.isRegularFile(file)) {
                    try {
                        body = Files.readAllBytes(file);
                        status = 200;
                        contentType = "text/html; charset=UTF-8";
                    } catch (IOException e) {
                        status = 500;
                        body = "500 Internal Server Error".getBytes(StandardCharsets.UTF_8);
                        e.printStackTrace();
                    }
                }
            }

            sendResponse(request, status, contentType, body);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            request.close();
        }
    }

    private static void sendResponse(HttpExchange request, int status, String contentType,
            byte[] body) throws IOException {
        request.getResponseHeaders().set("Content-Type", contentType);
        request.getResponseHeaders().set("Connection", "close");
        if (request.getRequestMethod().equals("HEAD")) {
            request.sendResponseHeaders(status, -1);
        } else {
            request.sendResponseHeaders(status, body.length);
            try (OutputStream output = request.getResponseBody()) {
                output.write(body);
            }
        }
    }
}