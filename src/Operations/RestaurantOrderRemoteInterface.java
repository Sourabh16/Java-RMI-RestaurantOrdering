package Operations;

import pojo.menuDetails;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

// Creating Remote interface for our application
public interface RestaurantOrderRemoteInterface extends Remote {

    void animation() throws RemoteException;

    Map<String, ArrayList<menuDetails>> loadDropDown(String mealType) throws RemoteException;

    Map<String, ArrayList<menuDetails>> getDisplayChoiceData(String selectedItem, String item) throws RemoteException;

    void insertOrderDataToDB(String toogleGroupValue, String customerName, String custTableNumber, String selectedItem, String FoodItem) throws RemoteException;
}