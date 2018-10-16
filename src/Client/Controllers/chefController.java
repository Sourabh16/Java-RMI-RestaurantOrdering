package Client.Controllers;

import Operations.Client.Utility.DBConnect;
import Operations.RestaurantOrderRemoteInterface;
import Utility.ReadCSV;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pojo.menuDetails;
import pojo.order;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import static Utility.commonConstants.BEVERAGE_LIST;
import static Utility.commonConstants.FOOD_LIST;

public class chefController implements Initializable {


    private RestaurantOrderRemoteInterface restaurantOrderRemoteInterface;

    @FXML
    private Button BTPrepare, BTquit, BTquit1;

    @FXML
    private ListView waitingListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getRegistryInterface();
    }//close initialize

    ObservableList<order> strList = FXCollections.observableArrayList();
    //ObservableList<orderList> WaitingList = FXCollections.observableArrayList();

    private void getRegistryInterface() {
        try {
            Registry registry = LocateRegistry.getRegistry(null);

            // Looking up the registry for the remote object  or unbundling or unmarshaling
            restaurantOrderRemoteInterface = (RestaurantOrderRemoteInterface) registry.lookup("HelloAnimation_v1");
        } catch (RemoteException | NotBoundException e) {
            System.out.println("exception while getting interface" + e.toString());
        }
    }


    @FXML
    private void validateEntry(ActionEvent event) throws RemoteException {

        ArrayList<order> temp =restaurantOrderRemoteInterface.getWaitingList();
        System.out.println(">>"+temp.get(0).getBeverageName());
        strList.addAll(temp);
        loadListView(strList);
       // waitingListView.add(strList);


    }//close validate Name

    private void loadListView(ObservableList<order> list) {
        //System.out.printf(list.get(1).getItemName());
       // String Order = cusName.getText().toString() + "|" + "Table:" + cusTable.getText().toString() + "|" + list.get(0).getItemName() + "&" + list.get(1).getItemName();
        System.out.printf(list.get(0).getBeverageName());
        //waitingList.getItems().;
        waitingListView.getItems().add(list.get(0).getBeverageName());
        if(!waitingListView.getSelectionModel().isEmpty()) {
            BTPrepare.setDisable(false);
        }
    }











}
