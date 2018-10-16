package Operations;

import Client.Utility.ReadCSV;
import pojo.menuDetails;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static Client.Utility.commonConstants.*;
/*
Developing the Implementation Class
In the Implementation class (Remote Object) of this application, we are trying to create a window which displays GUI content, using JavaFX.
*/

// Implementing the remote interface RestaurantOrderRemoteInterface that contains void animation() throws RemoteException;
public class RestaurantOrderImpl implements RestaurantOrderRemoteInterface {

//    @Override
//    public void animation() throws RemoteException {
//
//    }

    @Override
    public void animation() throws RemoteException {
        System.out.println("default animation invoked");
    }

    @Override
    public Map<String, ArrayList<menuDetails>> loadDropDown(String mealType) {
        ReadCSV read = new ReadCSV();
        return getConsumables(read.csvqueryDropDownList(), mealType);
    }

    /**
     * returns a map containing separated food and beverage arrayList from total foodList
     *
     * @param foodList total foodList received from server
     * @param mealType mealType selected by user
     * @return a map containing separated food and beverage arrayList
     */
    private Map<String, ArrayList<menuDetails>> getConsumables(ArrayList<menuDetails> foodList, String mealType) {
        // using streams and lambda functionality to filter data on mealType and MenuDescription
        ArrayList<menuDetails> foodItemsList = foodList.stream()
                .filter(n -> n.getMenuDesc().equalsIgnoreCase(FOOD) && n.getMealType().equalsIgnoreCase(mealType))
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<menuDetails> beverageItemsList = foodList.stream()
                .filter(n -> n.getMenuDesc().equalsIgnoreCase(BEVERAGE) && n.getMealType().equalsIgnoreCase(mealType))
                .collect(Collectors.toCollection(ArrayList::new));

        Map<String, ArrayList<menuDetails>> tempMap = new HashMap<>();
        tempMap.put(FOOD_LIST, foodItemsList);
        tempMap.put(BEVERAGE_LIST, beverageItemsList);
        return tempMap;
    }


}
