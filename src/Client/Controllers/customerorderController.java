package Client.Controllers;

import Operations.Client.Utility.DBConnect;
import Operations.RestaurantOrderRemoteInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pojo.menuDetails;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import static Client.Utility.commonConstants.BEVERAGE_LIST;
import static Client.Utility.commonConstants.FOOD_LIST;

public class customerorderController implements Initializable {

    private ToggleGroup radioGroup = new ToggleGroup();
    private RestaurantOrderRemoteInterface restaurantOrderRemoteInterface;
    @FXML
    private ComboBox<String> foodID, BeveragesID;
    @FXML
    private Button BTPrepare;
    @FXML
    private Button BTbill;
    @FXML
    private Button BTenterData, BTchoiceDisplay, BTdisplayOrder;
    @FXML
    private TextField cusName, cusTable;
    @FXML
    private RadioButton rdBrkfast, rdLunch, rdDinner;
    @FXML
    private TableView tblDisplay;
    private int OrderID = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        foodID.setDisable(true);
        BeveragesID.setDisable(true);
        BTPrepare.setDisable(true);
        BTbill.setDisable(true);
        BTdisplayOrder.setDisable(true);
        //cusTable.setDisable(true);
        //Toggle Menu
        rdBrkfast.setToggleGroup(radioGroup);
        rdLunch.setToggleGroup(radioGroup);
        rdDinner.setToggleGroup(radioGroup);

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


    //EnterData Button validation
    @FXML
    private void validateName(ActionEvent event) {
        int value;
        //System.out.printf(foodID.getSelectionModel().getSelectedItem());
        if (cusTable.getText().trim().isEmpty()) {
            value = 0;
        } else {
            value = Integer.parseInt(cusTable.getText());
        }
        if (event.getSource() == BTenterData && cusName.getText().trim().isEmpty()) {
            System.out.printf(cusName.getText());//testing line
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter customer name : ", ButtonType.CLOSE);
            alert.showAndWait();
            return;
        } else if (value <= 0 || value >= 9) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Enter the table number between 1 and 8 properly : ", ButtonType.CLOSE);
            alert.showAndWait();
            //System.out.printf(cusName.getText());//testing line
            return;
        } else if (!rdBrkfast.isSelected() && !rdLunch.isSelected() && !rdDinner.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "select Menu : ", ButtonType.CLOSE);
            alert.showAndWait();
            //System.out.printf(cusName.getText());//testing line
            return;
        } else if (foodID.getSelectionModel().isEmpty() || BeveragesID.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Select Food and Beverages : ", ButtonType.CLOSE);
            alert.showAndWait();
            //System.out.printf(cusName.getText());//testing line
            return;
        } else {
            RadioButton selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();
            String toogleGroupValue = selectedRadioButton.getText();
            System.out.printf(toogleGroupValue);//testing line
            System.out.printf(cusName.getText());
            String insertStmt =
                    "INSERT INTO customerDetails\n" +
                            "(cusName, cusTable,MealType)\n" +
                            "VALUES\n" +
                            "('" + cusName.getText() + "'," + Integer.parseInt(cusTable.getText()) + ",'" + toogleGroupValue + "');\n";
            //System.out.printf(insertStmt);
            Operations.Client.Utility.DBConnect db = new Operations.Client.Utility.DBConnect();
            OrderID = db.dbconnectExecute(insertStmt);
            insertStmt =
                    "INSERT INTO Orders\n" +
                            "(OrderID,FoodName,BeverageName)\n" +
                            "VALUES\n" +
                            "('" + OrderID + "','" + foodID.getSelectionModel().getSelectedItem() + "','" + BeveragesID.getSelectionModel().getSelectedItem() + "');";
            //System.out.printf(insertStmt);
            db.dbconnectExecute(insertStmt);
            BTdisplayOrder.setDisable(false);
        }
    }//close validate Name

    //Display Choice Button validation
    @FXML
    private void validateDisplay(ActionEvent event) {
        try {
            if (event.getSource() == BTchoiceDisplay) {
                ObservableList<String> drFood = FXCollections.observableArrayList();        // observableList for food drop down
                ObservableList<String> drBeverage = FXCollections.observableArrayList();    // observableList for food drop down
                if (rdBrkfast.isSelected()) {
                    setFoodBeverageProperties(drFood, drBeverage, "BreakFast");
                } else if (rdLunch.isSelected()) {
                    setFoodBeverageProperties(drFood, drBeverage, "Lunch");
                } else if (rdDinner.isSelected()) {
                    setFoodBeverageProperties(drFood, drBeverage, "Dinner");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Select Menu : ", ButtonType.CLOSE);
                    alert.showAndWait();
                    //System.out.printf(cusName.getText());//testing line
                }

            }
        } catch (RemoteException e) {
            System.out.println("exception:" + e.toString());
        }
    }

    /**
     * setting food and beverage properties to the dropdowns
     *
     * @param drFood     food dropdown
     * @param drBeverage beverage dropdown
     * @param mealType   selected meal type
     * @throws RemoteException @{@link RemoteException}
     */
    private void setFoodBeverageProperties(ObservableList<String> drFood, ObservableList<String> drBeverage, String mealType) throws RemoteException {
        // call to server function to load data returns hashMap
        Map<String, ArrayList<menuDetails>> foodMap = restaurantOrderRemoteInterface.loadDropDown(mealType);

        // getting food list from hashMap and converting it to observable list
        ObservableList<menuDetails> foodList = FXCollections.observableArrayList(foodMap.get(FOOD_LIST));

        // getting beverage list from hashMap
        ObservableList<menuDetails> beverageList = FXCollections.observableArrayList(foodMap.get(BEVERAGE_LIST));

        // adding food list to observable list of type string from menu item arraylist,
        // uses stream functionality to get itemNames from the observableList
        foodList.stream().map(menuDetails::getItemName).forEach(drFood::add);

        // adding beverage list to observable list of type string from menu item arrayList,
        // uses stream functionality to get itemNames from the observableList
        beverageList.stream().map(menuDetails::getItemName).forEach(drBeverage::add);

        foodID.setItems(drFood);                        //setting observable list of food to foodDropDown
        BeveragesID.setItems(drBeverage);               //setting observable list of food to beverageDropDown
        foodID.setDisable(false);
        BeveragesID.setDisable(false);
    }

    //load data to table
    @FXML
    private void loadTableData(ActionEvent event) {
        ArrayList<String> orderData = new ArrayList<String>();
        if (event.getSource() == BTdisplayOrder) {
            DBConnect db = new DBConnect();
            orderData = db.dbOrderTableData(OrderID);


        }

    }
}
