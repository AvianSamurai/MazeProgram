package Program;

import org.junit.jupiter.api.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TestMaze {
    Maze maze1;
    Maze maze2;

    @BeforeEach
    void setup() {
        maze1 = new Maze("Cool maze", "Robbert", "64", "logo.png");
        maze2 = new Maze("Wow maze", "Jack");
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
    void testGetTitleFromMaze1() {
        assertEquals("Cool maze", maze1.GetTitle());
    }

    @Test
    void testGetTitleFromMaze2() {
        assertEquals("Wow maze", maze2.GetTitle());
    }

    @Test
    void testGetDateTime() {
        LocalDateTime current = LocalDateTime.now();
        //Replace expect output with the current local datetime
        //assertEquals("2022/05/17 17:04", maze1.GetDateTime());
    }
}
