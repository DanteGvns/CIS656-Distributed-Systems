import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;

public class ServerApp {
    //make instance of the implementation
    private static final ProccessString proccessString = new ProccessStringImpl();


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

        try {
            //Send out which Client they are
            PrintWriter outputSocket = new PrintWriter(appSocket.getOutputStream(), true);
            outputSocket.println(clientConnected + clientNumber);

            //wait for messsage from client
            ObjectInputStream objectInputStream = new ObjectInputStream(appSocket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(appSocket.getOutputStream());
            objectOutputStream.flush();

            while (true) {
                Object object = objectInputStream.readObject();

                if (object instanceof RpcRequestHandler) {
                    RpcRequestHandler rpcRequestHandler = (RpcRequestHandler) object;
                    if ("timeOrUpper".equals(rpcRequestHandler.getMethod())) {
                        String message = proccessString.timeOrUpper(rpcRequestHandler.getMessage());
                        objectOutputStream.writeObject(message);
                        objectOutputStream.flush();
                    } else {
                        System.out.println("Error: Could not proccess string");
                        throw new UnsupportedOperationException();
                    }
                } else {
                    throw new UnsupportedOperationException("Unknown RPC request.");
                }
            }
        //catch for if client disconnets, mainly when they Ctrl + C
        } catch (EOFException | java.net.SocketException e) {
            System.out.println("Client #" + clientNumber + " has disconnected.");

        //regular catch server side error, prints which client instance caused it
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Connection error for client #" + clientNumber
                    + ": " + e.getMessage());
        } finally {
            try {
                appSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close client #" + clientNumber + " socket.");
            }
        }
    }

}
