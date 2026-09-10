import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class ClientApp {
    public static void main(String[] args){
        String dateTime = new Date().toString();
        System.out.println(dateTime);

        //set address of sever and port number to connect to
        String serverAddress = "";
        int portNum = 53245;

        try {
            Socket severSocket = new Socket(serverAddress, portNum);
            String dateMessage = new Date().toString();
            String notify = "The date has been sent to the sever!";

            //Write the string to the output port
            PrintWriter outputSocket = new PrintWriter(severSocket.getOutputStream(), true);
            outputSocket.println(dateMessage);
            System.out.println(notify);

            //close the socket
            severSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}