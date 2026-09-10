import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;

public class ServerApp {
    public static void main(String[] args){
        //set listening port for client to connect to
        int portNum = 53245;
        
        String startUpMessage = "This Sever is Running";
        String waiting = "Waiting for another message....";
        String sentMessage = "The date has been sent to the Client!";
        String clientConnected = "Client connected!";
        String dateMessage;
        String message;

        System.out.println(startUpMessage);

        try { 
            ServerSocket listener = new ServerSocket(portNum);

            while (true) {
                //connect to client
                Socket appSocket = listener.accept();
                System.out.println(clientConnected);

                //Write the date to the socket output
                dateMessage = new Date().toString();
                PrintWriter outputSocket = new PrintWriter(appSocket.getOutputStream(), true);
                outputSocket.println(dateMessage);
                System.out.println(sentMessage);

                //wait for response from client
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(appSocket.getInputStream())
                );
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
