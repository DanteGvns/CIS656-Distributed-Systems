
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebServer {
    private static final int portNum = 8080;

    public static void main(String[] args){
        Path folderRoot = Paths.get(".").toAbsolutePath().normalize();

        try (ServerSocket listener = new ServerSocket(portNum)) {
            System.out.println("Web server listening at http://localhost:" + portNum);

            while (true) {
                Socket appSocket = listener.accept();
                new Thread(() ->handleClient(appSocket, folderRoot)).start();
            }
        } catch (IOException e) {
            return;
        }
    }

    private static void handleClient(Socket appSocket, Path folderRoot) {
        String message;
        String status = "404 Not Found";
        String contentType = "text/plain; charset=UTF-8";
        byte[] body = "404 Not Found".getBytes(StandardCharsets.UTF_8);

        try (Socket connection = appSocket) {
            connection.setSoTimeout(10000);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.US_ASCII)
            );

            message = in.readLine();
            if (message == null) {
                return;
            }
            String[] requestParts = message.split(" ", 3);

            while ((message = in.readLine()) != null && !message.isEmpty()) {
            }

            if (requestParts.length != 3
                    || (!requestParts[2].equals("HTTP/1.1") && !requestParts[2].equals("HTTP/1.0"))) {
                status = "400 Bad Request";
                body = status.getBytes(StandardCharsets.UTF_8);
            } else if (!requestParts[0].equals("GET")) {
                status = "405 Method Not Allowed";
                body = status.getBytes(StandardCharsets.UTF_8);
            } else {
                String requestedFile;
                try {
                    requestedFile = URI.create(requestParts[1]).getPath();
                } catch (IllegalArgumentException e) {
                    sendResponse(connection, "400 Bad Request", contentType,
                        "400 Bad Request".getBytes(StandardCharsets.UTF_8));
                    return;
                }

                Path file = folderRoot.resolve("hello.html");
                if ("/hello.html".equals(requestedFile) && Files.isRegularFile(file)) {
                    try {
                        body = Files.readAllBytes(file);
                        status = "200 OK";
                        contentType = "text/html; charset=UTF-8";
                    } catch (IOException e) {
                        status = "500 Internal Server Error";
                        body = status.getBytes(StandardCharsets.UTF_8);
                    }
                }
            }

            sendResponse(connection, status, contentType, body);
        } catch (IOException e) {
            return;
        }
    }

    private static void sendResponse(Socket appSocket, String status, String contentType,
            byte[] body) throws IOException {
        String headers = "HTTP/1.1 " + status + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + body.length + "\r\n";
        if (status.equals("405 Method Not Allowed")) {
            headers += "Allow: GET\r\n";
        }
        headers += "Connection: close\r\n\r\n";

        OutputStream outputSocket = appSocket.getOutputStream();
        outputSocket.write(headers.getBytes(StandardCharsets.US_ASCII));
        outputSocket.write(body);
        outputSocket.flush();
    }
}