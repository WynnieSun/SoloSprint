package models;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote{
	//public void notifyChange(Object observable, Object updateMsg) throws RemoteException;
	//ANOTHER WAY
	public String notifyMe(String message) throws RemoteException;
}

