import java.rmi.*;
import java.rmi.server.*;
import java.util.Date;

public class MethodRemote extends UnicastRemoteObject implements Method{

	MethodRemote()throws RemoteException{
		super();
	}

	public String action(String userInput){
		String response = "";

		if (userInput.trim().equals("time")) {
			response = new Date().toString();
		} else if (!userInput.isEmpty()) {
			response = userInput.toUpperCase();
		}
		
		return response;
	}
}
