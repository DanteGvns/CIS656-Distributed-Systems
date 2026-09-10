import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServerApp {
    public static void main(String[] args){
        String startUpMessage = "This Sever is Running";
        System.out.println(startUpMessage);
        int portNum = 53245;
        try { 
            ServerSocket listener = new ServerSocket(portNum);
            while (true) {
                Socket socket1 = listener.accept();
                System.out.println("Client connected!");

                BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket1.getInputStream())
                );

                //read buffer
                String message;
                while ((message = in.readLine()) != null ) {
                    System.out.println("Client says: " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
