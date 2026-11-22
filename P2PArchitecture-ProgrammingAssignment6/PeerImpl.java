import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PeerImpl extends UnicastRemoteObject implements PeerMethods {
    private List<String> neighbors = new ArrayList<>();
    private final Random random = new Random();

    public PeerImpl() throws RemoteException {
        super();
    }

    @Override
    public int getPeerCount() {
        return neighbors.size();
    }

    @Override
    public List<String> getPeers() {
        return neighbors;
    }

    @Override
    public String connect(String ip, int port)
            throws RemoteException {

        if (neighbors.size() >= 3) {
            return neighbors.get(random.nextInt(neighbors.size()));
        }

        if (!neighbors.contains(ip)) {
            neighbors.add(ip);
            System.out.println("Peer: " + ip + " is now a neighbor.");
        }

        return "ACCEPTED";
    }

    @Override
    public void disconnect(String ip, int port) throws RemoteException{
        neighbors.remove(ip);
        System.out.println("Peer: " + ip + " has disconnected.");
    };
}
