import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args){
        //port number used to communicate
        int portNum = 53245;

        String prompt = "Please enter the Severs IP address:";
        String returnMessage = "Received";
        String notify = "The reply has been sent to the server!";

        //get IP address from user
        System.out.println(prompt);
        Scanner scanner = new Scanner((System.in));
        String serverAddress = scanner.nextLine();

        //connect to the sever and send message
        try {
            //connect to sever
            Socket severSocket = new Socket(serverAddress, portNum);

            //Read and print message from server, this should be the date
            BufferedReader in = new BufferedReader(
                new InputStreamReader(severSocket.getInputStream())
            );
            String message;
            while ((message = in.readLine()) != null ) {
                System.out.println(message);
            }

            //send back received to sever
            PrintWriter outputSocket = new PrintWriter(severSocket.getOutputStream(), true);
            outputSocket.println(returnMessage);

            //let users now the message was sent
            System.out.println(notify);

            //close the socket
            severSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //close the scanner b/c why not
        scanner.close();
    }
}