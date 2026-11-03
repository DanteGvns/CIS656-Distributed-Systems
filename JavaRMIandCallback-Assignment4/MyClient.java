import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.net.*;

public class MyClient extends UnicastRemoteObject implements Callback{

	private String clientName;

	MyClient(String clientName)throws RemoteException{
		super();
		this.clientName = clientName;
	}

	public void showMessage(String message)throws RemoteException{
		System.out.println("Using Callback " + clientName + ": " + message);
	}

	public static void main(String args[]){
		MyClient client = null;
		try{
			System.setProperty("java.rmi.server.hostname", "localhost");
			Method stub=(Method)Naming.lookup("rmi://localhost:5000/lab6");  
			String serverName = stub.getServerName();
			client = new MyClient(InetAddress.getLocalHost().getHostName());
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

			while(true){
				System.out.println("Enter a string to send to the server (empty to quit):");
				String userInput = input.readLine();
				if(userInput == null || userInput.isEmpty()){
					break;
				}

				String response = stub.action(userInput, client);
				System.out.println("Using RMI from " + serverName + ": " + response);
			}
		}catch(Exception e){System.out.println(e);}
		finally{
			if(client != null){
				try{
					UnicastRemoteObject.unexportObject(client, true);
				}catch(Exception e){System.out.println(e);}
			}
		}
	}
}
