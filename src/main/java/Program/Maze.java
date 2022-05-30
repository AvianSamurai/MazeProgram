package Program;

import DB.MazeDB;
import Utils.Debug;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Maze {
    //Fields for the Maze object
    private String title;
    private String author;
    private transient DateTimeFormatter theDateTime;
    private int id = -1;
    public String cellSize;
    public String imageSize;
    public String logo;
    private String type;
    private MazeStructure m;
    private int[] startPos, endPos;


    /**
     * Creates a new Maze object containing no maze
     *
     * @param title
     * @param author
     * @param cellSize
     * @param type
     * @deprecated
     */
    public Maze (String title, String author, String cellSize, String type){
        this.title = title.replaceAll("[^a-zA-Z0-9 ]", "");
        this.author = author.replaceAll("[^a-zA-Z0-9 ]", "");
        this.cellSize = cellSize;
        this.imageSize = imageSize;
        this.logo = logo;
        //this.imageSize = imageSize;
        //this.logo = logo;
        this.type = type;
    }

    /**
     * Creates a new maze containing a randomly generated maze of the specific type with and automatic cell size
     *
     * @param title Maze title
     * @param author Maze author
     * @param type
     * @param width
     * @param height
     */
    public Maze (String title, String author, String cellSize, String type, int width, int height) {
        this(title, author, "auto", type);

        startPos = new int[]{0, 0};
        endPos = new int[]{width - 1, height - 1};

        switch (type.toLowerCase()) {
            default:
            case "standard":
                m = MazeFactory.CreateBasicMaze(width, height);
                break;

            case "themed":
                m = MazeFactory.CreateThemedMaze(width, height);
                break;

            case "logo":
                m = MazeFactory.CreateLogoMaze(width, height);
                break;
        }
    }

    /**
     * Creates a new maze object with only a title and author
     *
     * @param title title of the maze
     * @param author author of the maze
     * @deprecated
     */
    public Maze (String title, String author){
        this.title = title;
        this.author = author;
    }

    /**
     * Gets the author of the maze
     *
     * @return maze author
     */
    public String GetAuthor() {
        return this.author;
    }

    /**
     * Gets the title of the maze
     *
     * @return maze title
     */
    public String GetTitle(){
        return this.title;
    }

    /**
     * Gets a string representation of size of the maze in cells
     *
     * @return maze size
     */
    public String getCellSize(){return this.cellSize;}

    /**
     * Returns a 2D x,y int representing the starting position of the maze
     *
     * @return the start position of the maze
     */
    public int[] GetStartPos() { return this.startPos; }

    /**
     * Returns a 2D x,y int representing the ending position of the maze
     *
     * @return the end position of the maze
     */
    public int[] GetEndPos() { return this.endPos; }

    /**
     * Sets the starting point of the maze to a 2D x,y coordinate
     *
     * @param startPos new start position
     */
    public void SetStartPos(int[] startPos) { this.startPos = startPos; }


    /**
     * Sets the ending point of the maze to a 2D x,y coordinate
     *
     * @param endPos new end position
     */
    public void SetEndPos(int[] endPos) { this.endPos = endPos; }

    /**
     * Gets the original maze type <br/>
     * e.g. standard, themed
     *
     * @return maze type
     */
    public String getType(){return this.type;}

    /**
     * Returns the contained maze structure, this may be null if the maze was constructed with a deprecated constructor
     * @return the maze structure object
     */
    public MazeStructure getMazeStructure() { return m; }

    /**
     * Saves the maze to database
     * @return true if save was successful, false otherwise
     */
    public boolean SaveMaze() {
        MazeDB mazeDB;
        try {
            mazeDB = new MazeDB();
        } catch (Exception e) {
            e.printStackTrace();
            Debug.LogLn("Failed to open a database connection when saving maze");
            return false;
        }

        boolean newMaze = false;

        if(id == -1) { // This should only be the case if the maze is brand new
            id = mazeDB.GetNextAvailableID();
            newMaze = true;
        }

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(I_Cell.class, new InterfaceGsonSerializer());
        builder.registerTypeAdapter(Image.class, new BufferedImageGsonSerializer());
        builder.registerTypeAdapter(BufferedImage.class, new BufferedImageGsonSerializer());
        Gson gson = builder.create();
        String mazeString = gson.toJson(this);
        Debug.LogLn("Saving Maze...");

        if(newMaze) {
            try {
                int nextID = mazeDB.GetNextAvailableID();
                mazeDB.CreateUpdateDelete("INSERT INTO saved_mazes (id, name, author_name, json_data, creation_date, last_modified) " +
                        "VALUES ('" + id + "', '" + title + "','" + author + "','" + mazeString + "','" + GetDateTime() + "','" + GetDateTime() + "');");
            } catch (SQLException e) {
                Debug.LogLn("Failed to insert new maze into database");
                e.printStackTrace();
                mazeDB.disconnect();
                return false;
            }
            mazeDB.disconnect();
            return true;
        } else {
            try {
                mazeDB.CreateUpdateDelete("UPDATE saved_mazes SET json_data = '" + mazeString + "', last_modified = '" + GetDateTime() + "' " +
                        "WHERE id = " + id);
            } catch (SQLException e) {
                Debug.LogLn("Failed to update maze in database");
                e.printStackTrace();
                mazeDB.disconnect();
                return false;
            }
        }

        mazeDB.disconnect();
        return true;
    }

    /**
     * Loads the maze from the database
     *
     * @param id id to find maze by
     * @return the new maze object
     */
    public static Maze LoadMazeFromID(int id) {
        MazeDB mazeDB;
        Maze finalMaze = null;
        try {
            mazeDB = new MazeDB();
        } catch (Exception e) {
            e.printStackTrace();
            Debug.LogLn("Failed to open a database connection when loading maze");
            return null;
        }

        ResultSet result;
        try {
            Debug.LogLn("Loading maze by id (id = " + id + ")");
            result = mazeDB.Query("SELECT json_data FROM saved_mazes WHERE id = " + id);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(I_Cell.class, new InterfaceGsonSerializer());
            gsonBuilder.registerTypeAdapter(Image.class, new BufferedImageGsonSerializer());
            gsonBuilder.registerTypeAdapter(BufferedImage.class, new BufferedImageGsonSerializer());
            Gson gson = gsonBuilder.create();
            if(result.next()) {
                if(result.getString(1) != null) {
                    finalMaze = gson.fromJson(result.getString(1), Maze.class);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Debug.Log("Failed to load json data from database");
            return null;
        }

        return finalMaze;
    }

    /**
     * Gets the date and time in a sql friendly way
     *
     * @return date and time in string format
     */
    public String GetDateTime(){
        this.theDateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime current = LocalDateTime.now();
        return this.theDateTime.format(current);
    }



}




