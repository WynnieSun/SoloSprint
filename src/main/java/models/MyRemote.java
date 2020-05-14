package models;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

//Server Interface
public interface MyRemote extends Remote {
    public String sayHello() throws RemoteException;
    public Person verifyLoginPerson(String username, String password) throws RemoteException;
    public ArrayList<Person> getStoredUser() throws RemoteException;
    public String addBP(BusinessPlan BP) throws RemoteException;
    public String addNewBP(BusinessPlan BP) throws RemoteException;
    public BusinessPlan findBP(int year) throws RemoteException;
    public void changeEditable(int year, boolean bool) throws RemoteException;
    public void addPerson(String username, String password, String department, Boolean isAdmin)throws RemoteException;
    public void logOut() throws RemoteException;
    public ArrayList<BusinessPlan> findDepAllBP() throws RemoteException;
    public void register(MyRemoteClient client) throws RemoteException;
    public void unregister(MyRemoteClient client) throws RemoteException;
    public void callback() throws RemoteException;
    public ArrayList<MyRemoteClient> getClientList() throws RemoteException;
    public ArrayList<String> compare(BusinessPlan BP1, BusinessPlan BP2) throws RemoteException;
    
    
}
