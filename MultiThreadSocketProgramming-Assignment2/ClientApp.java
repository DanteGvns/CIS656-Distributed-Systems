import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args){
        //port number used to communicate
        int portNum = 53245;

        String serverPrompt = "Please enter the Severs IP address:";
        String sendPrompt = "Please enter a string to send to the server:";
        String returnMessage;
        String notify = "\nServer Response:";
        String message;

        //get IP address from user
        System.out.println(serverPrompt);
        Scanner scanner = new Scanner((System.in));
        String serverAddress;

        //make sure the app doesn't error if the user ctrl+c while typing Server Address
        if (scanner.hasNextLine()){
            serverAddress = scanner.nextLine();
        } else {
            scanner.close();
            return;
        }

        //connect to the sever and send message
        try {
            //connect to sever
            Socket severSocket = new Socket(serverAddress, portNum);

            //listen for which client # we are
            BufferedReader in = new BufferedReader(
                new InputStreamReader(severSocket.getInputStream())
            );
            message = in.readLine();
            System.out.println(message);

            //send a message to sever
            PrintWriter outputSocket = new PrintWriter(severSocket.getOutputStream(), true);
            while (scanner.hasNextLine()){
                //ask user for message
                System.out.println(sendPrompt);
                message = scanner.nextLine();

                //close socket is user enters ""
                if (message.isEmpty()){
                    severSocket.close();
                    System.out.println("Connection closed.");
                    break;

                } else {
                    outputSocket.println(message);

                    //listen for response from server
                    returnMessage = in.readLine();
                    System.out.println(notify);
                    System.out.println(returnMessage+"\n");

                }
            }
        //catch error for invalid host ip address
        } catch (UnknownHostException e) {
            System.out.println("Could not resolve host: " + serverAddress);
        //catch for if client disconnets, mainly when they Ctrl + C
        } catch (java.net.SocketException e) {
        System.out.println("Connection to sever unexpectedly ended.");

        //regular catch server side error, prints which client instance caused it
        } catch (IOException e) {
            e.printStackTrace();
        }

        //close the scanner b/c why not
        scanner.close();
    }
}