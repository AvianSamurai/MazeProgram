package Program;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Maze {
    //Fields for the Maze object
    private String title;
    private String author;
    private DateTimeFormatter theDateTime;
    public int cellSize;
    public int imageSize;
    public String logo;


    //Constructor for the initialization of a new Maze object
    public Maze (String title, String author, int cellSize, int imageSize, String logo){
        this.title = title;
        this.author = author;
        this.cellSize = cellSize;
        this.imageSize = imageSize;
        this.logo = logo;


    }

    //Overloading constructor in case logo, title, and author is not entered in object call.
    public Maze (int cellSize){
        this.cellSize = cellSize;
    }

    //Retrieving private author String
    public String GetAuthor() {
        return this.author;
    }

    //Retrieving private title string
    public String GetTitle(){
        return this.title;
    }

    //Retrieving the date and time of maze creation
    public String GetDateTime(){
        this.theDateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime current = LocalDateTime.now();
        return this.theDateTime.format(current);
    }
}



