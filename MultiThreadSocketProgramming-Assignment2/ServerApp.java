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
        String sentMessage = "The message has been sent to the Client!";
        String clientConnected = "Hello, you are client #";
        Boolean stillRunning = true;
        int countClients = 0;
        String dateMessage;
        String message;
        String upperCase;

        System.out.println(startUpMessage);

        try { 
            //alawys listen for a new client
            while (true) {
                ServerSocket listener = new ServerSocket(portNum);
                //connect to client
                while (stillRunning) {
                    Socket appSocket = listener.accept();
                    System.out.println(clientConnected);

                    //Send out which Client they are
                    PrintWriter outputSocket = new PrintWriter(appSocket.getOutputStream(), true);
                    outputSocket.println(clientConnected + countClients++);
                    System.out.println(sentMessage);

                    //wait for messsage from client
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(appSocket.getInputStream())
                    );
                    while ((message = in.readLine()) != null ) {
                        if (message.trim() == "time") {
                            System.out.println("message was time sending back time");
                            dateMessage = new Date().toString();
                            outputSocket.println(dateMessage);
                        } else if (message != "") {
                            upperCase = message.toUpperCase();
                            System.out.println("message was " + message + " sending back time" + upperCase);
                            outputSocket.println(upperCase);
                        } else {
                            appSocket.close();
                            listener.close();
                            stillRunning = false;
                        }
                    }
                }
                stillRunning = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
