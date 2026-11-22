import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.net.*;

public class Peer extends UnicastRemoteObject implements Callback{

	private String peerName;

	Peer(String peerName)throws RemoteException{
		super();
		this.peerName = peerName;
	}

	public void showMessage(String message)throws RemoteException{
		System.out.println("Using Callback " + peerName + ": " + message);
	}

	public static void main(String args[]){
		Peer mySelf = null;
		try{
			System.setProperty("java.rmi.server.hostname", "localhost");
			Method stub=(Method)Naming.lookup("rmi://localhost:5000/lab6");  
			String serverName = stub.getServerName();
			String myIP = InetAddress.getLocalHost().getHostName()

			mySelf = new Peer(myIP);
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

			while(true){
				if(myIP == null || myIP.isEmpty()){
					break;
				}

				//String response = stub.action(userInput, mySelf);
				String centralServerResponse = stub.action(userInput, mySelf);
				System.out.println("Using RMI from " + serverName + ": " + response);
			}
		}catch(Exception e){System.out.println(e);}
		finally{
			if(mySelf != null){
				try{
					UnicastRemoteObject.unexportObject(mySelf, true);
				}catch(Exception e){System.out.println(e);}
			}
		}
	}
}
