import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.Collections;
import java.util.List;

public class CentralServer{

	public static void main(String args[]){
		try{
		//InetAddress.getLocalHost().getHostAddress() was giving me 127.0.0.1 on linux machines
		//so if we get the loopback address, we try to get any non-loopback IPv4 address
			InetAddress local = InetAddress.getLocalHost();
			String centralServerIP = local.getHostAddress();
			if (local.isLoopbackAddress()) {
				centralServerIP = Collections.list(NetworkInterface.getNetworkInterfaces()).stream()
					.flatMap(network -> Collections.list(network.getInetAddresses()).stream())
					.filter(ip -> ip instanceof Inet4Address && !ip.isLoopbackAddress()
						&& !ip.isLinkLocalAddress() && !ip.isAnyLocalAddress())
					.map(InetAddress::getHostAddress)
					.findFirst()
					.orElseThrow();
			}
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

