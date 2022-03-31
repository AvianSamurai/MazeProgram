import com.mysql.jdbc.Connection;

import java.sql.*;
import java.util.Properties;

public class MazeDB {
    private  String db_driver;
    private  String db_url;
    private  String username;
    private  String pwd;

    //Initialising a connection object to create a session with a specific database
    private Connection connection;

    //Properties object is used to maintain lists of values in which the key is a string and the value is a string
    //I think also used to set new entries in a database?
    private Properties properties;

    //The statement object is used for executing sql statements
    private Statement statement;

    //get the properties or set properties from user
    private Properties retrieveProps(){
        return properties;
    }

    //Open a connection to the database
    private Connection connection(){
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
    
}
