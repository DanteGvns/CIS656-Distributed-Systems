import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class ClientApp {
    public static void main(String[] args){
        String dateTime = new Date().toString();
        System.out.println(dateTime);
        String serverAddress = "1.0.1";
        int portNum = 53245;
        try {
            Socket socket2 = new Socket(serverAddress, portNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}