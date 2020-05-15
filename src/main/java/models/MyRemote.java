package models;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

//Server Interface
public interface MyRemote extends Remote {
    public String sayHello() throws RemoteException;
   // public void addObserver(ClientInterface o) throws RemoteException;
    public Person verifyLoginPerson(String username, String password) throws RemoteException;
    public ArrayList<Person> getStoredUser() throws RemoteException;
    public String addBP(BusinessPlan BP) throws RemoteException;
    public String addNewBP(BusinessPlan BP) throws RemoteException;
    public BusinessPlan findBP(int year) throws RemoteException;
    public void changeEditable(int year, boolean bool) throws RemoteException;
    public void addPerson(String username, String password, String department, Boolean isAdmin) throws RemoteException;
    public void logOut() throws RemoteException;
    public ArrayList<BusinessPlan> findDepAllBP() throws RemoteException;
    public ArrayList<String> compare(BusinessPlan BP1, BusinessPlan BP2) throws RemoteException;
    public void registerForCallBack(MyRemoteClient client) throws RemoteException;
    public void unregisterForCallBack(MyRemoteClient client) throws RemoteException;
	public void setStoredBP(ArrayList<BusinessPlan> storedBP) throws RemoteException;
	public void setStoredUser(ArrayList<Person> storedUser) throws RemoteException;
	public void updateObserver(BusinessPlan currentBP) throws RemoteException;
}
