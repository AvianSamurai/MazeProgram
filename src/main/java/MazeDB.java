import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MazeDB {
    // Class Config
    private static final String PROPERTIES_FILE = "db.props";
    private static final String CREATE_DB_STRUCTURE_COMMAND = "CREATE TABLE mazes (" +
            "id PRIMARY UNIQUE UNSIGNED int(32) NOT NULL, " +
            "name varchar(128) NOT NULL, " +
            "author_name varchar(128) NOT NULL, " +
            "creation_date DATETIME NOT NULL, " +
            "last_modified DATETIME NOT NULL);"; // [UNTESTED]

    // Local variables
    private String databaseURL;
    private String schema;
    private String username, password;

    /**
     * Connects to the database and ensures that the correct
     * structure has been created
     *
     * @throws FileNotFoundException Thrown if db.props couldn't be found
     * @throws IOException Thrown if an error occurs while reading the db.props file
     */
    public MazeDB() throws IOException {
        // Get the database's properties
        Properties dbProps = new Properties();
        FileReader propsReader = new FileReader(System.getProperty("user.dir") + "\\" + PROPERTIES_FILE);
        dbProps.load(propsReader);
        databaseURL = dbProps.getProperty("jdbc.url");
        schema = dbProps.getProperty("jdbc.schema");
        username = dbProps.getProperty("jdbc.username");
        password = dbProps.getProperty("jdbc.password");

        // Connect to the database

        // Test the database structure
    }

    /**
     * Get a list of mazes from the database
     *
     * @return
     */
    public MazeData[] GetMazes() {
        return null;
    }

    /**
     * Saves maze to database or overwrites maze in database
     * if maze already exists
     *
     * @param mazeToSave Maze data to save to database
     */
    public void SaveMaze(MazeData mazeToSave) {}

    /**
     * Checks if maze already exists in Database
     *
     * @return True if maze exists
     */
    public boolean MazeExists() {
        return false;
    }
}
