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

        String peerAddress = ip;

        if (!neighbors.contains(peerAddress)) {
            neighbors.add(peerAddress);
        }

        return "ACCEPTED";
    }

}
