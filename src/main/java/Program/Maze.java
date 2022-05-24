package Program;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Maze {
    //Fields for the Maze object
    private String title;
    private String author;
    public DateTimeFormatter theDateTime;
    public String cellSize;
    public String imageSize;
    public String logo;
    private String type;
    private MazeStructure m;


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
        this.title = title;
        this.author = author;
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
        switch (type.toLowerCase()) {
            default:
            case "standard":
                m = MazeFactory.CreateBasicMaze(width, height);
                break;

            case "themed":
                m = MazeFactory.CreateThemedMaze(width, height);
                break;

            case "empty":
                m = MazeFactory.CreateEmptyMaze(width, height);
                break;
        }
    }

    public Maze (String title, String author){
        this.title = title;
        this.author = author;
    }

    //Retrieving private author String
    public String GetAuthor() {
        return this.author;
    }

    //Retrieving private title string
    public String GetTitle(){
        return this.title;
    }

    public String getCellSize(){return this.cellSize;}

    public String getType(){return this.type;}

    public MazeStructure getMazeStructure() { return m; }

    public void SaveMaze() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String mazeString = gson.toJson(m);

    }

    //Retrieving the date and time of maze creation
    public String GetDateTime(){
        this.theDateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime current = LocalDateTime.now();
        return this.theDateTime.format(current);
    }



}




