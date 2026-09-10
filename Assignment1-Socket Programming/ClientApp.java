import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args){
        //set address of sever and port number to connect to
        int portNum = 53245;
        Scanner scanner = new Scanner((System.in));

        //get IP address from user
        String prompt = "Please enter the Severs IP address:";
        System.out.println(prompt);
        String serverAddress = scanner.nextLine();

        //connect to the sever and send message
        try {
            Socket severSocket = new Socket(serverAddress, portNum);
            String dateMessage = new Date().toString();
            String notify = "The date has been sent to the sever!";

            //Write the string to the output port
            PrintWriter outputSocket = new PrintWriter(severSocket.getOutputStream(), true);
            outputSocket.println(dateMessage);

            //let users now the message was sent
            System.out.println(notify);

            //close the socket
            severSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}