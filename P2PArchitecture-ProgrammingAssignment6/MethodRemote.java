import java.rmi.*;
import java.rmi.server.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class MethodRemote extends UnicastRemoteObject implements Method{

	//store the connected clients
	private List<String> peerIPs = Collections.synchronizedList(new ArrayList<>());

	MethodRemote()throws RemoteException{
		super();
	}

	public String action(String userInput, Callback client)throws RemoteException{
		String response = "";

		if (userInput.equals("time")) {
			response = new Date().toString();
		} else if (!userInput.isEmpty()) {
			response = userInput.toUpperCase();
		}

		client.showMessage(response);
		return response;
	}

	public String getServerName()throws RemoteException{
		try{
			return InetAddress.getLocalHost().getHostName();
		}catch(UnknownHostException e){
			throw new RemoteException("Could not get the server name.", e);
		}
	}

	public String getRandomPeer()throws RemoteException{
		String response = "";

		if (peerIPs.isEmpty()) {
			response = "0";
		} else {
			String randomPeerIP = peerIPs.get(
				ThreadLocalRandom.current().nextInt(peerIPs.size())
			);
			response = randomPeerIP;
		}
		  
		return response;
	}
}
