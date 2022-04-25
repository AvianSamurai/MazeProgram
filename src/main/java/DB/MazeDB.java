package DB;

import Utils.Debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.*;
import java.util.Scanner;

public class MazeDB {

    // Some class config
    private static final String PROPERTIES_FILE = "db.props";
    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";

    // useful constants
    public static final String SAVED_MAZES_TABLE_NAME = "saved_mazes";

    // Useful SQL queries
    private static final String CREATE_DB_STRUCTURE_COMMAND = "CREATE TABLE " + SAVED_MAZES_TABLE_NAME + " (" +
            "id int(32) UNSIGNED UNIQUE NOT NULL, " +
            "name varchar(128) NOT NULL, " +
            "author_name varchar(128) NOT NULL, " +
            "creation_date DATETIME NOT NULL, " +
            "last_modified DATETIME NOT NULL," +
            "PRIMARY KEY (id))";
    private static final String TEST_DB_STRUCTURE = "SHOW TABLES LIKE '" + SAVED_MAZES_TABLE_NAME + "'";

    private static String db_schema;
    private static String db_url;
    private static String username;
    private static String pwd;

    //Initialising a connection object to create a session with a specific database
    public Connection connection = null;

    //The statement object is used for executing sql statements
    public Statement statement = null;

    //Open a connection to the database

    /**
     * Opens a connection to the database
     *
     * @return The database connection object
     * @throws ClassNotFoundException Thrown if the JDBC_DRIVER class is not found. This should never happen,
     * but a database connection isn't possible if it does.
     */
    public Connection connection() throws ClassNotFoundException {
        Connection dbcon = null;
        // Get driver class
        Class.forName(JDBC_DRIVER);

        // Open a connection
        Debug.LogLn("Connecting to database...");
        try {
            dbcon = DriverManager.getConnection(db_url + "/" + db_schema, username, pwd);
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

    /**
     * Disconnects from the database and resets the database
     * connector, Setup will need to be preformed if MazeDB is to be used again
     * whether that's an automatic setup from invoking another method or
     * setup by calling Setup()
     */
    public void disconnect(){
        try {
            statement.close();
            connection.close();
            Debug.LogLn("Disconnected from database");
        } catch (SQLException e) {
            Debug.LogLn("Database access error occurred, Database assumed to be disconnected");
        }
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
        return statement.executeQuery(query);
    }

    /**
     * [NOT IMPLEMENTED]
     * Used to preform updates, creations, or deletions to the database
     *
     * @param query a raw SQL query
     * @return the number of deleted objects or edite columns/rows
     * @throws SQLException Thrown if the query is malformed
     */
    public int CreateUpdateDelete(String query) throws SQLException{
        //Returns the number of deleted objects or edite columns/rows
        return statement.executeUpdate(query);
    }

    /**
     * Gets the next unused id for saving a new maze to
     *
     * @return the next available id or -1 if there was a problem
     */
    public int getNextAvailableID() {
        try {
            ResultSet res = Query("SELECT MAX(id) FROM " + SAVED_MAZES_TABLE_NAME);
            if(res.next()) {
                return res.getInt(1) + 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Constructs a MazeDB object and runs the setup routine for the database
     *
     * The setup routine does the following:
     *  1) Get the properties from the properties file
     *  2) Connect to the database with that info
     *  3) Test if the database has had its structure setup
     *  4) Setup the database's structure
     *
     * Setup failed if any exception is thrown
     *
     * @throws IOException Thrown if the db.props file cannot be found
     * @throws ClassNotFoundException Thrown if the JDBC driver could not be found, if this is thrown
     * then connection is impossible
     * @throws SQLException Thrown if there is a database access error or if the connection to the
     * database was disconnected during setup
     */
    public MazeDB() throws IOException, ClassNotFoundException, SQLException {
        // Get the database's properties
        Properties dbProps = new Properties();
        FileReader propsReader = new FileReader(PROPERTIES_FILE);
        dbProps.load(propsReader);
        db_url = dbProps.getProperty("jdbc.url");
        db_schema = dbProps.getProperty("jdbc.schema");
        username = dbProps.getProperty("jdbc.username");
        pwd = dbProps.getProperty("jdbc.password");

        // Connect to the database
        if(connection == null) {
            connection();
            statement = connection.createStatement();
        }

        // Test the database structure
        if(!Query(TEST_DB_STRUCTURE).next()) {
            // unfortunately sql create table queries return 0 whether it was successful or not,
            // so we have to then test the structure again
            CreateUpdateDelete(CREATE_DB_STRUCTURE_COMMAND);
            if(!Query(TEST_DB_STRUCTURE).next()) {
                Debug.LogLn("Table '" + SAVED_MAZES_TABLE_NAME + "' doesn't exist and creation failed");
            } else {
                Debug.LogLn("Created table '" + SAVED_MAZES_TABLE_NAME + "'");
            }
        } else {
            Debug.LogLn("Found table '" + SAVED_MAZES_TABLE_NAME + "'");
        }
    }

    /**
     * Returns a 2D array of rows of strings where the search string matches either the ID, Name, or Author
     * rows are formatted as following
     * | id | name | author | creation_date | last_modified |
     *
     * The search string will be trimmed of any white space or zero space characters and the search will not be
     * case-sensitive
     *
     * this does not contain the maze data, to get the maze data you should use the id as names do not have to
     * be unique
     *
     * @param searchString The string to search the database with
     * @return A 2D array of strings containing the results
     */
    public String[][] GetMazeListBySearchString(String searchString) {
        String searchStr = searchString.trim().toLowerCase();
        ResultSet r;
        try {
            r = this.Query("SELECT id, name, author_name, creation_date, last_modified FROM saved_mazes");
            ArrayList<String[]> data = new ArrayList<String[]>();
            while(r.next()) {
                if(r.getString(1).equals(searchStr) ||
                        r.getString(2).toLowerCase().contains(searchStr) ||
                        r.getString(3).toLowerCase().contains(searchStr)) {
                    data.add(new String[]{r.getString(1), r.getString(2),
                            r.getString(3), r.getString(4), r.getString(5)});
                }
            }
            Debug.LogLn("Found " + data.toArray().length + " results matching '" + searchString + "' in saved_mazes");
            return data.toArray(String[][]::new);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Inserts the data in resources/DB/TestData.csv into the database
     * if resetFirst is true, the database will be reset before data is loaded in
     * USE WITH CAUTION, USE ONLY IN TESTING
     *
     * @param resetFirst if true, DATABASE WILL BE RESET
     * @throws FileNotFoundException Thrown if the TestData.csv file cannot be found
     */
    public void LoadTestDataIntoDatabase(boolean resetFirst) throws FileNotFoundException {
        MazeDB database = this;

        if(resetFirst) {
            ClearDatabase();
            try {
                CreateUpdateDelete(CREATE_DB_STRUCTURE_COMMAND);
            } catch (Exception e) {
                Debug.LogLn("Failed to setup new database after clearing old database");
                e.printStackTrace();
                return;
            }
        }

        Scanner testDataReader = new Scanner(new File("src/main/resources/DB/TestData.csv"));
        while(testDataReader.hasNext()) {
            String[] currentLine = testDataReader.nextLine().split(",");
            try {
                Query("INSERT INTO saved_mazes (id, name, author_name, creation_date, last_modified)" +
                        "VALUES (" + currentLine[0] + ", '" + currentLine[1] + "', '" + currentLine[2] +
                        "', STR_TO_DATE('" + currentLine[3] + "', '%d/%m/%Y'), STR_TO_DATE('" + currentLine[4] + "', '%d/%m/%Y'))");
            } catch (SQLException e) {
                Debug.LogLn("SQL query failed while loading test data into database: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        }

        Debug.LogLn("Loaded test data into database");
    }

    /**
     * DO NOT USE THIS METHOD IN ANY NON-TESTING METHOD
     * NOT FOR PRODUCTION
     */
    public void ClearDatabase() {
        try {
            this.CreateUpdateDelete("DROP TABLE saved_mazes");
        } catch (SQLException e) {
            Debug.LogLn("Could not clear database: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        Debug.LogLn("Cleared database");
    }
}