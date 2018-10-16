package Operations.Client.Utility;

import java.sql.*;
import java.util.ArrayList;


public class DBConnect
{
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://db4free.net/advjavarmi?useSSL=false";

    static final String USER = "swinjava";
    static final String PASS = "T8hs8QCP9pqcSPQ";

    public void dbconnectCheck() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(
                    DB_URL, USER, PASS);
//here sonoo is database name, root is username and password
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from customerDetails");
            while (rs.next())
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getInt(3) + "  " + rs.getString(4));
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public int dbconnectExecute(String queryCommand) {
        int numero=0;
        try {

            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(
                    DB_URL, USER, PASS);
            PreparedStatement ps=con.prepareStatement(queryCommand,Statement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            if(rs.next()){
                numero=rs.getInt(1);
            }
            System.out.println(numero);
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return numero;
    }

    public ArrayList<String> dbOrderTableData(int orderID) {
        ArrayList<String> odrList = new ArrayList<String>();
        try {

            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(
                    DB_URL, USER, PASS);
//here sonoo is database name, root is username and password
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Orders where OrderID="+orderID);
            while (rs.next())
            {
                odrList.add(rs.getString(2));
                odrList.add(rs.getString(3));
            }
                //System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return odrList;
    }

}
