import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.net.*;

public class Peer {
	public static void main(String args[]) throws IOException, NotBoundException{
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		String centralServerIP;
		ServerMethod centralServerStub;
		while (true) {
			System.out.println("Please enter the IP address of the Central Server or type \"quit\" to exit the program:");
			centralServerIP = userInput.readLine();
			if (centralServerIP == null || "quit".equalsIgnoreCase(centralServerIP.strip())) {
				return;
			}
			centralServerIP = centralServerIP.strip();
			if (centralServerIP.isEmpty()) {
				continue;
			}
			try {
				centralServerStub = (ServerMethod) LocateRegistry.getRegistry(centralServerIP, 5000).lookup("centralServer");
				break;
			} catch (RemoteException e) {
				System.err.println("Could not connect to the Central Server at " + centralServerIP + ":5000. Check the address and make sure the server is running.");
			} catch (NotBoundException e) {
				System.err.println("Reached " + centralServerIP + ":5000, but the centralServer service is not registered.");
			}
		}

		//Rmi setup for the peer itself
		Integer myPort = 5001;
		//InetAddress.getLocalHost().getHostAddress() was giving me 127.0.0.1 on linux machines
		//so if we get the loopback address, we try to determine the actual IP by connecting to the central server.
		InetAddress local = InetAddress.getLocalHost();
		String myIP = local.getHostAddress();
		if (local.isLoopbackAddress()) {
			try (DatagramSocket socket = new DatagramSocket()) {
				socket.connect(new InetSocketAddress(centralServerIP, 5000));
				myIP = socket.getLocalAddress().getHostAddress();
			}
		}
		
		System.setProperty("java.rmi.server.hostname", myIP);
		LocateRegistry.createRegistry(myPort);
		PeerImpl mySelf = new PeerImpl();
		Naming.rebind("rmi://" + myIP + ":5001/peer", mySelf);  

		String centralServerResponse = "";
		try {
			centralServerResponse = centralServerStub.getRandomPeer(myIP);
		} catch (Exception e){System.out.println(e);}

		//check server response and reach out to peer
		if(centralServerResponse == null || centralServerResponse.isEmpty()){
			System.out.println("Error! Could not get response from Central Server at: " + centralServerIP);
		} else if (centralServerResponse.equals("0")) {
			//sever sends back "0" when it has no peers stored
			// listenForPeers()
			System.out.println("No peers found. \n Starting network.");
		} else {
			String initPeerIP = centralServerResponse;
			System.out.println("Attempting to Connect to Peer at: " + initPeerIP);
			String connectedPeer = initPeerIP;
			PeerMethods remotePeer = (PeerMethods)Naming.lookup("rmi://" + initPeerIP + ":5001/peer");
			String initPeerResponse = remotePeer.connect(myIP, myPort);

			if ("ACCEPTED".equalsIgnoreCase(initPeerResponse)) {
				mySelf.connect(connectedPeer, 5001);
				centralServerStub.storePeer(myIP);
			} else {
				while (!"Accepted".equalsIgnoreCase(initPeerResponse)){
					connectedPeer = initPeerResponse;
					System.out.println("Peer at: " + initPeerIP + " is full.");
					System.out.println("Attempting to Connect to Peer at: " + initPeerResponse);

					remotePeer = (PeerMethods)Naming.lookup("rmi://" + initPeerResponse + ":5001/peer");
					initPeerResponse = remotePeer.connect(myIP, myPort);
				}
				mySelf.connect(connectedPeer, 5001);
				centralServerStub.storePeer(myIP);
			}
		}
		List<String> myPeers = new ArrayList<>();
		String userCommand = "";
		
		while (true){
			System.out.println("Type \"neighbors\" to see a list of direct peers or type \"quit\" to disconnect:");
			userCommand = userInput.readLine();
			myPeers = mySelf.getPeers();
			if (userCommand.equalsIgnoreCase("neighbors")) {
				if (myPeers.isEmpty()) {
					System.out.println("No connected peers.");
				} else {
					for (int i = 0; i < myPeers.size(); i++) {
						System.out.println("Peer " + (i + 1) + ": " + myPeers.get(i));
					}
				}
			} else if (userCommand.equalsIgnoreCase("quit")) {
				for (int i = 0; i < myPeers.size(); i++) {
					//disconnect from every peer
					String peerIP = myPeers.get(i);

					try {
						PeerMethods remotePeer = (PeerMethods) Naming.lookup("rmi://" + peerIP + ":5001/peer");
						remotePeer.disconnect(myIP, myPort);
					} catch (Exception e) {
						System.out.println(
							"Could not disconnect from peer: "
							+ peerIP
						);
					}
				}
				try {
					if (mySelf.serverOn) {
						centralServerStub.removePeer(myIP);
					}
				} catch (RemoteException e) {
					System.err.println("Could not unregister from the server: " + e);
				}
				UnicastRemoteObject.unexportObject(mySelf, true);
				break;
			}

		}//end while loop for getting ip from user

	} //end main
}
