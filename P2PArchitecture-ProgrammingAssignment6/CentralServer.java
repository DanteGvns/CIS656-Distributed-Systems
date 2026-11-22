import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.List;

public class CentralServer{

	public static void main(String args[]){
		try{
			String centralServerIP = InetAddress.getLocalHost().getHostAddress();
			System.setProperty("java.rmi.server.hostname", centralServerIP);
			LocateRegistry.createRegistry(5000);
			ServerMethodRemote stub = new ServerMethodRemote();
			Naming.rebind("rmi://" + centralServerIP + ":5000/centralServer", stub);  
			System.out.println("The central server is running.");

			while (true){
				System.out.println("Type \"members\" to see a list of current peers or type \"quit\" to terminate the central server:");
				BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
				String userCommand = userInput.readLine();

				if (userCommand.equalsIgnoreCase("members")) {
					List<String> allPeers = stub.getAllPeers();
					if (allPeers.isEmpty()) {
						System.out.println("No connected peers.");
					} else {
						for (int i = 0; i < allPeers.size(); i++) {
							System.out.println("Peer " + (i + 1) + ": " + allPeers.get(i));
						}
					}
				} else if (userCommand.equalsIgnoreCase("quit")) {
					stub.shutDown();
					UnicastRemoteObject.unexportObject(stub, true);
					break;
				}

			}//end while loop for getting ip from user
		}catch(Exception e){System.out.println(e);}
	} 
}

