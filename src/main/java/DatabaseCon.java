import java.utils.Properties;

public class DatabaseCon {

    private final String propertiesFile = System.getProperty("user.dir") + "\\MazeProgram\\db.props";
    private URL databaseURL;
    private String schema;
    private String username, password;

    /**
     * Connects to the database and ensures that the correct
     * structure has been created
     */
    public DatabaseCon() {
        // Get the database's properties
        Properties dbProps = new Properties();
        FileReader propsReader = new FileReader(propertiesFile);

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
}
