import java.rmi.*;
import java.rmi.server.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class MethodRemote extends UnicastRemoteObject implements Method{

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
}
