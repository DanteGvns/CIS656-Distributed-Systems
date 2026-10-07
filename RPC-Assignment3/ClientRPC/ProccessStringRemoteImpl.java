import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ProccessStringRemoteImpl implements ProccessString {
    private final Socket socket;
    private final ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public ProccessStringRemoteImpl(Socket socket) throws IOException {
        this.socket = socket;
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public String timeOrUpper(String message) {
        try {
            RpcRequestHandler rpcRequestHandler = generateRequest(message);
            objectOutputStream.writeObject(rpcRequestHandler);
            objectOutputStream.flush();

            if (objectInputStream == null) {
                objectInputStream = new ObjectInputStream(socket.getInputStream());
            }
            Object response = objectInputStream.readObject();

            if (response instanceof String) {
                return (String) response;
            }

            throw new InternalError("Unexpected response from server.");
        } catch (Exception e) {
            throw new InternalError(e);
        }
    }

    private RpcRequestHandler generateRequest(String message) {
        RpcRequestHandler rpcRequestHandler = new RpcRequestHandler();
        rpcRequestHandler.setMessage(message);
        rpcRequestHandler.setMethod("timeOrUpper");
        return rpcRequestHandler;
    }

}
