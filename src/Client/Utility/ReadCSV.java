package Client.Utility;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pojo.menuDetails;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class ReadCSV {

    public ArrayList<menuDetails> csvqueryDropDownList() {

        ArrayList<menuDetails> strList = new ArrayList<>();
        String csvFile = "src/Operations/data.csv";
        String line;
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] food = line.split(cvsSplitBy);
                System.out.println("food:"+ Arrays.toString(food));

                menuDetails menuDesc = new menuDetails(food[0], food[1], food[2],
                        food[3], food[4], food[5], food[6], food[7], food[8], food[9]);

                strList.add(menuDesc);
            }
        } catch (IOException e) {
            System.out.println("exception:" + e);
        }
        return strList;
    }

    public ObservableList<String> csvqueryLoadTableData(String Food, String Beverage) {

        // ArrayList<String> strList = new ArrayList<String>();
        ObservableList<String> strList = FXCollections.observableArrayList();
        String csvFile = "src/sample/Operations.Client.Operations.Client.Data/data.csv";
        //BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] food = line.split(cvsSplitBy);
                if (Food.equals(food[1]) && food[0].equals(Beverage)) {
                    strList.add(food[2]);
                    System.out.println("Country [MealType= " + food[0] + " , Meal Name=" + food[2] + "]");
                    // System.out.println("Country [MealType= " + food[2] + " , Meal Name=" + food[5] + "]");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return strList;
    }
}
