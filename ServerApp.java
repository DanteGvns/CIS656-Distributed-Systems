import java.io.IOException;
import java.net.ServerSocket;

import javax.imageio.IIOException;

public class ServerApp {
    public static void main(String[] args){
        String startUpMessage = "This Sever is Running";
        System.out.println(startUpMessage);

        try { 
            ServerSocket listener = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
