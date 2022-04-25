package DB;

import Utils.Debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class DBHelper {
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
     * @param db An active database object
     * @param searchString The string to search the database with
     * @return A 2D array of strings containing the results
     */
    public static String[][] GetMazeListBySearchString(MazeDB db, String searchString) {
        String searchStr = searchString.trim().toLowerCase();
        ResultSet r;
        try {
            r = db.Query("SELECT id, name, author_name, creation_date, last_modified FROM saved_mazes");
            ArrayList<String[]> data = new ArrayList<String[]>();
            while(r.next()) {
                if(r.getString(1).equals(searchStr) ||
                        r.getString(2).toLowerCase().contains(searchStr) ||
                        r.getString(3).toLowerCase().contains(searchStr)) {
                    data.add(new String[]{r.getString(1), r.getString(2),
                            r.getString(3), r.getString(4), r.getString(5)});
                }
            }
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
     * @param db an active database connection
     * @param resetFirst if true, DATABASE WILL BE RESET
     * @throws FileNotFoundException Thrown if the TestData.csv file cannot be found
     */
    public static void LoadTestDataIntoDatabase(MazeDB db, boolean resetFirst) throws FileNotFoundException {
        MazeDB database = db;

        if(resetFirst) {
            ClearDatabaseAndDisconnect(db);
            try {
                db = new MazeDB();
            } catch (Exception e) {
                Debug.LogLn("Failed to setup new database after clearing old database");
                e.printStackTrace();
            }
        }

        Scanner testDataReader = new Scanner(new File("src/main/resources/DB/TestData.csv"));
        while(testDataReader.hasNext()) {
            String[] currentLine = testDataReader.nextLine().split(",");
            try {
                db.Query("INSERT INTO saved_mazes (id, name, author_name, creation_date, last_modified)" +
                        "VALUES (" + currentLine[0] + ", '" + currentLine[1] + "', '" + currentLine[2] +
                        "', STR_TO_DATE('" + currentLine[3] + "', '%d/%m/%Y'), STR_TO_DATE('" + currentLine[4] + "', '%d/%m/%Y'))");
            } catch (SQLException e) {
                Debug.LogLn("SQL query failed while loading test data into database: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * DO NOT USE THIS METHOD IN ANY NON-TESTING METHOD
     * NOT FOR PRODUCTION
     * @param db database object to clear all data in and disconnect
     */
    public static void ClearDatabaseAndDisconnect(MazeDB db) {
        try {
            db.CreateUpdateDelete("DROP TABLE saved_mazes");
            db.disconnect();
        } catch (SQLException e) {
            Debug.LogLn("Could not clear database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
