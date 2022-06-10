package Program;

import org.junit.jupiter.api.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TestMaze {
    Maze maze1;
    Maze maze2;
    Maze maze3;

    @BeforeEach
    void setup() {
        maze1 = new Maze("No maze", "Robbert", "Standard");
        maze2 = new Maze("New maze", "Jack", "Themed", 15, 15);
        maze3 = new Maze("New maze title and author only", "Tom");
    }

    @Test
    void testGetAuthorFromMaze1() {
        assertEquals("Robbert", maze1.GetAuthor());
    }

    @Test
    void testGetAuthorFromMaze2() {
        assertEquals("Jack", maze2.GetAuthor());
    }

    @Test
    void testGetAuthorFromMaze3() {
        assertEquals("Tom", maze3.GetAuthor());
    }

    @Test
    void testGetTitleFromMaze1() {
        assertEquals("No maze", maze1.GetTitle());
    }

    @Test
    void testGetTitleFromMaze2() {
        assertEquals("New maze", maze2.GetTitle());
    }

    @Test
    void testGetTitleFromMaze3() {
        assertEquals("New maze title and author only", maze3.GetTitle());
    }

    @Test
    void testGetIDFromMaze1() {
        maze1.SaveMaze();
        assertEquals("1", maze1.GetID());
    }

    @Test
    void testGetDateTime() {
        LocalDateTime current = LocalDateTime.now();
        //Replace expect output with the current local datetime
        //assertEquals("2022/05/17 17:04", maze1.GetDateTime());
    }
}
