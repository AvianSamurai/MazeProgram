import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class DatabaseCon {

    private final String propertiesFile = System.getProperty("user.dir") + "\\MazeProgram\\db.props";
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
    public DatabaseCon() throws IOException {
        // Get the database's properties [UNTESTED]
        Properties dbProps = new Properties();
        FileReader propsReader = new FileReader(propertiesFile);
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
}
