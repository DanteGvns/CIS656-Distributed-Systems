import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ProviderApp {
    private Calculator calculator = new CalculatorImpl();

    public static void main(String[] args) throws IOException {
        new ProviderApp().begin();
    }

    private void begin() throws IOException {
        ServerSocket listener = new ServerSocket(9090);
        System.out.println("The server is on!");
      
        while (true) {
            Socket socket = listener.accept();
            try {
                // 
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Object object = objectInputStream.readObject();

                // 
                int result = 0;
                if (object instanceof CalculateRpcRequest) {
                    CalculateRpcRequest calculateRpcRequest = (CalculateRpcRequest) object;
                    if ("add".equals(calculateRpcRequest.getMethod())) {
                        result = calculator.add(calculateRpcRequest.getA(), calculateRpcRequest.getB());
                    } else {
                        throw new UnsupportedOperationException();
                    }
                }

                // 
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(new Integer(result));
            } catch (Exception e) {
            	System.out.println("error!");
            } 
        }
    } 
}
