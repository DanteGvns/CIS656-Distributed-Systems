import java.rmi.*;
import java.rmi.server.*;

public interface Method extends Remote{

	public String action(String userInput, Callback client)throws RemoteException;
	public String getServerName()throws RemoteException;
}
