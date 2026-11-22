import java.rmi.*;
import java.rmi.server.*;

public interface ServerMethod extends Remote{
	public String getServerName()throws RemoteException;
	public String getRandomPeer(String newPeerIp)throws RemoteException;
	
}
