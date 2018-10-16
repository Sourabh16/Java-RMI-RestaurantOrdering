package Client.Controllers;

import Operations.RestaurantOrderRemoteInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

public class receptionistController implements Initializable {


    private RestaurantOrderRemoteInterface restaurantOrderRemoteInterface;

    @FXML
    private Button BTPrepare, BTquit, BTquit1;

    @FXML
    private ListView servingListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getRegistryInterface();
    }//close initialize

    private void getRegistryInterface() {
        try {
            Registry registry = LocateRegistry.getRegistry(null);

            // Looking up the registry for the remote object  or unbundling or unmarshaling
            restaurantOrderRemoteInterface = (RestaurantOrderRemoteInterface) registry.lookup("HelloAnimation_v1");
        } catch (RemoteException | NotBoundException e) {
            System.out.println("exception while getting interface" + e.toString());
        }
    }










}
