package DB;

import com.mysql.jdbc.Connection;

import java.sql.*;
import java.util.Properties;

//I looked up static classes in java and apparently you cant declare a class as static because
//it is a top level class, but we can declare inner classes as static
public static class MazeDB {
    private static String db_driver;
    private static String db_url;
    private static String username;
    private static String pwd;

    //Initialising a connection object to create a session with a specific database
    public Connection connection;

    //The statement object is used for executing sql statements
    public Statement statement;

    //Open a connection to the database
    public Connection connection(){

        return connection;
    }

    //Disconnect from database
    public void disconnect(){

    }

    public ResultSet Query(String query) throws SQLException{

        //return result from query
        return null;
    }

    public int CreateUpdateDelete(String query) throws SQLException{

        //Returns the number of deleted objects or edite columns/rows
        return 0;
    }

    //Added a setup skeleton just in case
    public void Setup(){

    }
}
