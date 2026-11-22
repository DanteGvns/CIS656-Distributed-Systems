import java.rmi.*;
import java.rmi.server.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class ServerMethodRemote extends UnicastRemoteObject implements ServerMethod{

	//store the connected clients
	private List<String> peerIPs = Collections.synchronizedList(new ArrayList<>());

	ServerMethodRemote()throws RemoteException{
		super();
	}

	public String getServerName()throws RemoteException{
		try{
			return InetAddress.getLocalHost().getHostName();
		}catch(UnknownHostException e){
			throw new RemoteException("Could not get the server name.", e);
		}
	}

	public String getRandomPeer(String newPeerIp)throws RemoteException{
		String response = "";

		if (peerIPs.isEmpty()) {
			response = "0";
			peerIPs.add(newPeerIp);
		} else {
			String randomPeerIP = peerIPs.get(
				ThreadLocalRandom.current().nextInt(peerIPs.size())
			);
			response = randomPeerIP;
		}
		  
		return response;
	}
	public List<String> getAllPeers() throws RemoteException{
		return peerIPs;
	};
	public void storePeer(String peerIp)throws RemoteException{
		peerIPs.add(peerIp);
	};

	public void removePeer(String peerIp)throws RemoteException{
		peerIPs.remove(peerIp);
	};
}
