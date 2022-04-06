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

    // Useful SQL queries
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
    public static Connection connection = null;

    //The statement object is used for executing sql statements
    public static Statement statement;

    //Open a connection to the database

    /**
     * Opens a connection to the database
     *
     * @return The database connection object
     * @throws ClassNotFoundException Thrown if the JDBC_DRIVER class is not found. This should never happen,
     * but a database connection isn't possible if it does.
     */
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

        // Save the connection for ease of use
        connection = dbcon;

        return dbcon;
    }

    //Disconnect from database
    public void disconnect(){

    }

    /**
     * [NOT IMPLEMENTED]
     * Used to get the response from a database query
     *
     * @param query a raw SQL query
     * @return The results from the query
     * @throws SQLException Thrown if the query is malformed
     */
    public ResultSet Query(String query) throws SQLException{

        //return result from query
        return null;
    }

    /**
     * [NOT IMPLEMENTED]
     * Used to preform updates, creations, or deletions to the database
     *
     * @param query a raw SQL query
     * @return 1 if action was a success, 0 otherwise
     * @throws SQLException Thrown if the query is malformed
     */
    public int CreateUpdateDelete(String query) throws SQLException{

        //Returns the number of deleted objects or edite columns/rows
        return 0;
    }

    /**
     * Runs the setup routine for the database class
     *
     * The setup routine does the following:
     *  1) Get the properties from the properties file
     *  2) Connect to the database with that info
     *  3) Test if the database has had its structure setup [NOT IMPLEMENTED]
     *  4) Setup the database's structure [NOT IMPLEMENTED]
     *
     * @throws IOException Thrown if the db.props file cannot be found
     * @throws ClassNotFoundException Thrown if the JDBC driver could not be found, if this is thrown
     * then connection is impossible
     */
    public static void Setup() throws IOException, ClassNotFoundException {
        // Get the database's properties
        Properties dbProps = new Properties();
        FileReader propsReader = new FileReader(System.getProperty("user.dir") + "\\" + PROPERTIES_FILE);
        dbProps.load(propsReader);
        db_url = dbProps.getProperty("jdbc.url");
        db_schema = dbProps.getProperty("jdbc.schema");
        username = dbProps.getProperty("jdbc.username");
        pwd = dbProps.getProperty("jdbc.password");

        // Connect to the database
        if(connection == null) {
            connection();
        }

        // Test the database structure

    }
}