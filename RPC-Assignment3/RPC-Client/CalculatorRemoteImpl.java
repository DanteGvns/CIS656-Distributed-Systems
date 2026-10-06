import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CalculatorRemoteImpl implements Calculator {
    public static final int PORT = 9090;
 
    public int add(int a, int b) {

        try {
            Socket socket = new Socket("127.0.0.1", PORT);

            // 
            CalculateRpcRequest calculateRpcRequest = generateRequest(a, b);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            // 
            objectOutputStream.writeObject(calculateRpcRequest);

            // 
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object response = objectInputStream.readObject();

            if (response instanceof Integer) {
            	socket.close();
                return (Integer) response;
                
            } else {
            	socket.close();
                throw new InternalError();
                
            }
            
        } catch (Exception e) {
         
            throw new InternalError();
        }       
    }

    private CalculateRpcRequest generateRequest(int a, int b) {
        CalculateRpcRequest calculateRpcRequest = new CalculateRpcRequest();
        calculateRpcRequest.setA(a);
        calculateRpcRequest.setB(b);
        calculateRpcRequest.setMethod("add");
        return calculateRpcRequest;
    }

}
