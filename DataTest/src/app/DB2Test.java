package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DB2Test {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;


    public void openConnection(){

// Step 1: Load IBM DB2 JDBC driver	

        try {

            DriverManager.registerDriver(new com.ibm.db2.jcc.DB2Driver());

        }

        catch(Exception cnfex) {

            System.out.println("Problem in loading or registering IBM DB2 JDBC driver");

            cnfex.printStackTrace();
        }

// Step 2: Opening database connection


        try {

            connection = DriverManager.getConnection("jdbc:db2://62.44.108.24:50000/SAMPLE:currentSchema=COFFEE_PROJECT;", "db2admin", "db2admin");

            statement = connection.createStatement();

        }

        catch(SQLException s){

            s.printStackTrace();

        }

    }

    public void closeConnection(){

        try {

            if(null != connection) {

                // cleanup resources, once after processing

                resultSet.close();

                statement.close();


                // and then finally close connection

                connection.close();

            }

        }

        catch (SQLException s) {

            s.printStackTrace();

        }

    }

    public void select(String stmnt, int column) {

        try{

            resultSet = statement.executeQuery(stmnt);

            String result = "";

            while(resultSet.next()) {

                for (int i = 1; i <= column; i++) {

                    result += resultSet.getString(i);

                    if (i == column) result += " \n";
                    else             result += ", ";
                }
            }

            System.out.println("Executing query: " + stmnt + "\n");
            System.out.println("Result output \n");
            System.out.println("---------------------------------- \n");
            System.out.println(result);

        }
        catch (SQLException s)
        {
            s.printStackTrace();
        }

    }
    public String selectToString(String stmnt, int column) {

        try{

            resultSet = statement.executeQuery(stmnt);

            String result = "";

            while(resultSet.next()) {

                for (int i = 1; i <= column; i++) {

                    result += resultSet.getString(i);

                    if (i == column) result += " \n";
                    else             result += ", ";
                }
                result += " \n";
            }

            //System.out.println("Executing query: " + stmnt + "\n");
          //  System.out.println("Result output \n");
        //System.out.println("---------------------------------- \n");
            return result;
        }
        catch (SQLException s)
        {
            s.printStackTrace();
        }
        return null;
    }

    public void insert(String stmnt) {

        try{

            statement.executeUpdate(stmnt);

        }

        catch (SQLException s){

            s.printStackTrace();

        }

        System.out.println("Successfully inserted!");

    }


    public void delete(String stmnt) {

        try{

            statement.executeUpdate(stmnt);

        }

        catch (SQLException s){

            s.printStackTrace();

        }

        System.out.println("Successfully deleted!");

    }

    public static void main(String[] args) {
        DB2Test db2Obj = new DB2Test();
        String stmnt;

        db2Obj.openConnection();

        // Set the schema to COFFEE_PROJECT (optional depending on your DB setup)


        //Query to find all compatible machine models for coffee with CODE '070081'

        /*
        stmnt = "SELECT CM.MODEL_NAME AS Compatible_with_070081 " +
                "FROM COFFEE_MACHINES CM " +
                "JOIN SUPPORTS S ON CM.CODE = S.MACHINE_CODE " +
                "JOIN COFFEES C ON S.COFFEE_TYPE_CODE = C.COFFEE_TYPE_CODE " +
                "WHERE C.CODE = '070081'";

         */
        stmnt="SELECT * FROM COFFEES WHERE COFFEES.NAME LIKE '%Blue%';";



        db2Obj.select(stmnt,9);

        db2Obj.closeConnection();
    }

public ResultSet getResultSet(String stmnt) {
    try {
        resultSet = statement.executeQuery(stmnt);
        return resultSet;
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}
    public List<String> selectToList(String query) {
        List<String> list = new ArrayList<>();
        try {
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
   
    
     
   

 