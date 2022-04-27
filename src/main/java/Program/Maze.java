package Program;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Maze {
    //Fields for the Maze object
    private String title;
    private String author;
    private DateTimeFormatter theDateTime;
    public String cellSize;
    public String imageSize;
    public String logo;
    private String type;


    //Constructor for the initialization of a new Maze object
    public Maze (String title, String author, String cellSize, String type){
        this.title = title;
        this.author = author;
        this.cellSize = cellSize;
        //this.imageSize = imageSize;
        //this.logo = logo;
        this.type = type;


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

    //Retrieving the date and time of maze creation
    public String GetDateTime(){
        this.theDateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime current = LocalDateTime.now();
        return this.theDateTime.format(current);
    }

}




