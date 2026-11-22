import java.rmi.*;
import java.rmi.server.*;
import java.util.List;

public interface ServerMethod extends Remote{
	public String getServerName()throws RemoteException;
	public String getRandomPeer(String newPeerIp)throws RemoteException;
	public List<String> getAllPeers() throws RemoteException;
	public void storePeer(String peerIp)throws RemoteException;
	public void removePeer(String peerIp)throws RemoteException;
}
