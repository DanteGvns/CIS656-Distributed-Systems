import java.rmi.*;
import java.util.List;

public interface PeerMethods extends Remote{
    int getPeerCount() throws RemoteException;

    void setServerOff() throws RemoteException;

    List<String> getPeers() throws RemoteException;

    String connect(String ip, int port) throws RemoteException;

    void disconnect(String ip, int port) throws RemoteException;	
}