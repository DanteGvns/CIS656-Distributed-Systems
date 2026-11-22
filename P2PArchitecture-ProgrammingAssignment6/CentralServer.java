import java.net.InetAddress;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

public class CentralServer{

	public static void main(String args[]){
		try{
			String centralServerIP = InetAddress.getLocalHost().getHostName();
			System.setProperty("java.rmi.server.hostname", centralServerIP);
			LocateRegistry.createRegistry(5000);
			ServerMethodRemote stub = new ServerMethodRemote();
			Naming.rebind("rmi://" + centralServerIP + "/centralServer", stub);  
			System.out.println("The server is running.");
		}catch(Exception e){System.out.println(e);}
	}
}

