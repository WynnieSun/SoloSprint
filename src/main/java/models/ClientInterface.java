package models;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote{
	public void notifyChange(String message) throws RemoteException;
}
