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

public class customerController implements Initializable {

    private ToggleGroup radioGroup = new ToggleGroup();
    private RestaurantOrderRemoteInterface restaurantOrderRemoteInterface;
    @FXML
    private ComboBox<String> foodID, BeveragesID;
    @FXML
    private Button BTenterData, BTchoiceDisplay, BTdisplayOrder;
    @FXML
    private TextField cusName, cusTable;
    @FXML
    private RadioButton rdBrkfast, rdLunch, rdDinner;
    @FXML
    private TableView<menuDetails> tblDisplay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        foodID.setDisable(true);
        BeveragesID.setDisable(true);
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
        try {
            int value;
            //System.out.printf(foodID.getSelectionModel().getSelectedItem());
            if (cusTable.getText().trim().isEmpty()) {
                value = 0;
            } else {
                value = Integer.parseInt(cusTable.getText());
            }
            if (event.getSource() == BTenterData && cusName.getText().trim().isEmpty()) {
                System.out.print(cusName.getText());//testing line
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter customer name : ", ButtonType.CLOSE);
                alert.showAndWait();
            } else if (value <= 0 || value >= 9) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Enter the table number between 1 and 8 properly : ", ButtonType.CLOSE);
                alert.showAndWait();
                //System.out.printf(cusName.getText());//testing line
            } else if (!rdBrkfast.isSelected() && !rdLunch.isSelected() && !rdDinner.isSelected()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "select Menu : ", ButtonType.CLOSE);
                alert.showAndWait();
                //System.out.printf(cusName.getText());//testing line
            } else if (foodID.getSelectionModel().isEmpty() || BeveragesID.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Select Food and Beverages : ", ButtonType.CLOSE);
                alert.showAndWait();
                //System.out.printf(cusName.getText());//testing line
            } else {
                RadioButton selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();
                String toogleGroupValue = selectedRadioButton.getText();
                System.out.print(toogleGroupValue);//testing line
                System.out.print(cusName.getText());
                restaurantOrderRemoteInterface.insertOrderDataToDB(toogleGroupValue, cusName.getText(), cusTable.getText(),
                        foodID.getSelectionModel().getSelectedItem(), BeveragesID.getSelectionModel().getSelectedItem());
                BTdisplayOrder.setDisable(false);
            }
        } catch (RemoteException e) {
            System.out.println("exception:" + e.toString());
        }
    }


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
        ObservableList<menuDetails> strList = FXCollections.observableArrayList();
        ArrayList<String> orderData = new ArrayList<>();

        if (event.getSource() == BTdisplayOrder) {
            DBConnect db = new DBConnect();
//            orderData = db.dbOrderTableData(OrderID);
            ReadCSV rd = new ReadCSV();


//            strList = rd.csvqueryLoadTableData(orderData.get(0), orderData.get(1));
            tblDisplay.setEditable(true);


            TableColumn<menuDetails, String> ItemNameCol = new TableColumn<>("Item Name");
            ItemNameCol.setMinWidth(200);
            ItemNameCol.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
            TableColumn<menuDetails, String> EnergyCol = new TableColumn<>("Energy");
            EnergyCol.setMinWidth(200);
            EnergyCol.setCellValueFactory(new PropertyValueFactory<>("Energy"));
            TableColumn<menuDetails, String> ProtienCol = new TableColumn<>("Protien");
            ProtienCol.setMinWidth(200);
            ProtienCol.setCellValueFactory(new PropertyValueFactory<>("Protien"));
            TableColumn<menuDetails, String> CarbohydrateCol = new TableColumn<>("Carbohydrate");
            CarbohydrateCol.setMinWidth(200);
            CarbohydrateCol.setCellValueFactory(new PropertyValueFactory<>("Carbohydrate"));
            TableColumn<menuDetails, String> TotalFatCol = new TableColumn<>("TotalFat");
            TotalFatCol.setMinWidth(200);
            TotalFatCol.setCellValueFactory(new PropertyValueFactory<>("TotalFat"));
            TableColumn<menuDetails, String> FibreCol = new TableColumn<>("Fibre");
            FibreCol.setMinWidth(200);
            FibreCol.setCellValueFactory(new PropertyValueFactory<>("Fibre"));
            TableColumn<menuDetails, String> PriceCol = new TableColumn<>("Price");
            PriceCol.setMinWidth(200);
            PriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

            //ItemDetails IDDetail=new ItemDetails("Total",strList.get(1).getEnergy());


            tblDisplay.setItems(strList);
            tblDisplay.setVisible(true);
            tblDisplay.getColumns().addAll(ItemNameCol, EnergyCol, ProtienCol, CarbohydrateCol, TotalFatCol, FibreCol, PriceCol);

        }
    }
}
