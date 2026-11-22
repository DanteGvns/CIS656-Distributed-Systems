import java.rmi.*;

public interface Callback extends Remote{

	public void showMessage(String message)throws RemoteException;
}