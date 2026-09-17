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
        
        Boolean stillRunning = true;
        int countClients = 1;
        String message;

        System.out.println(startUpMessage);
    
        try { 
            ServerSocket listener = new ServerSocket(portNum);
            while (true) {
                //connect to client
                Socket appSocket = listener.accept();
                int clientNumber = countClients++;
                System.out.println("Client #" + clientNumber + " connected.");
                new Thread(() ->handleClient(appSocket, clientNumber)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //make things simpler by making a method for each connected Client
    private static void handleClient(Socket appSocket, int clientNumber) {
            String clientConnected = "Hello, you are client #";
            String message;
            String upperCase;
            String dateMessage;

        try {
            //Send out which Client they are
            PrintWriter outputSocket = new PrintWriter(appSocket.getOutputStream(), true);
            outputSocket.println(clientConnected + clientNumber);

            //wait for messsage from client
            BufferedReader in = new BufferedReader(
                new InputStreamReader(appSocket.getInputStream())
            );
            while ((message = in.readLine()) != null ) {

                if (message.trim().equals("time")) {
                    dateMessage = new Date().toString();
                    outputSocket.println(dateMessage);

                } else if (!message.isEmpty()) {
                    upperCase = message.toUpperCase();
                    outputSocket.println(upperCase);

                } else if (message.isEmpty()){
                    System.out.println("Client #" + clientNumber + " has disconnected.");

                } else{
                    System.out.println("Error: unexpected communication error with client.");

                }
            }
        //catch for if client disconnets, mainly when they Ctrl + C
        } catch (java.net.SocketException e) {
        System.out.println("Client #" + clientNumber + " has disconnected.");

        //regular catch server side error, prints which client instance caused it
        } catch (IOException e) {
            System.err.println("Connection error for client #" + clientNumber
                    + ": " + e.getMessage());
        }
    }

}
