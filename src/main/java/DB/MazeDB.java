package DB;

import java.io.FileNotFoundException;
import Utils.Debug;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.sql.*;

public class MazeDB {

    // Some class config
    private static final String PROPERTIES_FILE = "db.props";
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String CREATE_DB_STRUCTURE_COMMAND = "CREATE TABLE mazes (" +
            "id PRIMARY UNIQUE UNSIGNED int(32) NOT NULL, " +
            "name varchar(128) NOT NULL, " +
            "author_name varchar(128) NOT NULL, " +
            "creation_date DATETIME NOT NULL, " +
            "last_modified DATETIME NOT NULL);"; // [UNTESTED]

    private static String db_schema;
    private static String db_url;
    private static String username;
    private static String pwd;

    //Initialising a connection object to create a session with a specific database
    public static Connection connection;

    //The statement object is used for executing sql statements
    public static Statement statement;

    //Open a connection to the database
    public static Connection connection() throws ClassNotFoundException {
        Connection dbcon = null;
        // Get driver class
        Class.forName(JDBC_DRIVER);

        // Open a connection
        Debug.LogLn("Connecting to database...");
        try {
            dbcon = DriverManager.getConnection(db_url, username, pwd);
        } catch (SQLException e) {
            Debug.LogLn("Connection Failed");
            e.printStackTrace();
            return null;
        }
        Debug.LogLn("Connection Successful");

        return dbcon;
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

    public static void Setup() throws IOException {
        // Get the database's properties
        Properties dbProps = new Properties();
        FileReader propsReader = new FileReader(System.getProperty("user.dir") + "\\" + PROPERTIES_FILE);
        dbProps.load(propsReader);
        db_url = dbProps.getProperty("jdbc.url");
        db_schema = dbProps.getProperty("jdbc.schema");
        username = dbProps.getProperty("jdbc.username");
        pwd = dbProps.getProperty("jdbc.password");

        // Connect to the database

        // Test the database structure
    }
}