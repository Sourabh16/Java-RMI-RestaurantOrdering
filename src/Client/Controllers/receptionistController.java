package Client.Controllers;

import Operations.RestaurantOrderRemoteInterface;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.util.Duration;
import pojo.order;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class receptionistController implements Initializable {


    private RestaurantOrderRemoteInterface restaurantOrderRemoteInterface;

    @FXML
    private Button BTbill, btnRefresh;

    @FXML
    private ListView<order> servingListView;
    private ArrayList<order> tempArrayList;
    private String selectedOrderId;

    private Timeline timeline;
    private Integer timeSeconds = 10;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getRegistryInterface();
        getTimerData();

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


    private void getTimerData() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        // KeyFrame event handler
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            timeSeconds--;
                            if (timeSeconds <= 0) {
                                timeline.stop();
                                try {

                                    //todo comment
                                    System.out.println("dattatttttttttttttttttttttttt fetchd");
                                    setArrayListData();


                                } catch (RemoteException e) {
                                    System.out.println("w");
                                }
                            }
                        }));
        timeline.playFromStart();
    }

    @FXML
    private void validateEntry(ActionEvent event) throws RemoteException {

        try {
            setArrayListData();
        } catch (RemoteException e) {
            System.out.println("a");
        }
    }

    private void setArrayListData() throws RemoteException {
        if (!servingListView.getItems().isEmpty()) {
            servingListView.getItems().clear();
        }
        tempArrayList = restaurantOrderRemoteInterface.getWaitingList("1");
        loadListView(tempArrayList);
    }

    private void loadListView(ArrayList<order> list) {
        try {
            ObservableList<order> pendingOrders = FXCollections.observableArrayList(list);
            servingListView.getItems().addAll(pendingOrders);
            if (!servingListView.getSelectionModel().isEmpty()) {
                BTbill.setDisable(false);
            }
            servingListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                selectedOrderId = newValue.getOrderId();
            });
        } catch (Exception e) {
            System.out.println("errr");
        }
        timeline.playFromStart();
    }

    @FXML
    private void billMenu(ActionEvent event) throws RemoteException {
        try {
            restaurantOrderRemoteInterface.updateOrderStatus(selectedOrderId, "2");
            setArrayListData();
        } catch (Exception e) {
            System.out.println("e");
        }
    }


}
