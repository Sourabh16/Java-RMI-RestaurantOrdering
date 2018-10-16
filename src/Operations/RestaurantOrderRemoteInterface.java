package Operations;

import pojo.menuDetails;
import pojo.order;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

// Creating Remote interface for our application
public interface RestaurantOrderRemoteInterface extends Remote {

    void animation() throws RemoteException;

    Map<String, ArrayList<menuDetails>> loadDropDown(String mealType) throws RemoteException;

    void insertOrderDataToDB(String toogleGroupValue, String customerName, String custTableNumber, String selectedItem, String FoodItem) throws RemoteException;

    ArrayList<order> getWaitingList() throws RemoteException;
}