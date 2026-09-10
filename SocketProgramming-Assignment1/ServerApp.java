import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServerApp {
    public static void main(String[] args){
        //set listening port for client to connect to
        int portNum = 53245;
        
        String startUpMessage = "This Sever is Running";
        String waiting = "Waiting for another message....";

        System.out.println(startUpMessage);

        try { 
            ServerSocket listener = new ServerSocket(portNum);
            while (true) {
                Socket socket1 = listener.accept();
                System.out.println("Client connected!");

                //put the socket input stream into a buffered reader
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket1.getInputStream())
                );

                //read and print buffer
                String message;
                while ((message = in.readLine()) != null ) {
                    System.out.println(message);
                    System.out.println(waiting);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
