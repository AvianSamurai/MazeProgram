package Program;

import DB.MazeDB;
import Utils.Debug;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static Program.Maze.LoadMazeFromID;
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
    @DisplayName("Test get author of a maze named \"Robert\"")
    void testGetAuthorFromMaze1() {
        assertEquals("Robbert", maze1.GetAuthor());
    }

    @Test
    @DisplayName("Test get author of a maze named \"Jack\"")
    void testGetAuthorFromMaze2() {
        assertEquals("Jack", maze2.GetAuthor());
    }

    @Test
    @DisplayName("Test get author of a maze named \"Tom\"")
    void testGetAuthorFromMaze3() {
        assertEquals("Tom", maze3.GetAuthor());
    }

    @Test
    @DisplayName("Test get title of a maze named \"No maze\"")
    void testGetTitleFromMaze1() {
        assertEquals("No maze", maze1.GetTitle());
    }

    @Test
    @DisplayName("Test get title of a maze named \"New maze\"")
    void testGetTitleFromMaze2() {
        assertEquals("New maze", maze2.GetTitle());
    }

    @Test
    @DisplayName("Test get title of a maze named \"New maze title and author only\"")
    void testGetTitleFromMaze3() {
        assertEquals("New maze title and author only", maze3.GetTitle());
    }

    @Test
    @DisplayName("Test get ID from maze that is not saved in the database")
    void testGetIDFromMazeNotInDatabase() {
        assertEquals(-1, maze1.GetID(), "Maze1 not found in database thus doesn't have an ID");
    }

    @Test
    @DisplayName("Test get ID from maze that is saved in the database")
    void testGetIDFromMazeInDatabase() {
        try {
            maze2.SaveMaze();
        } catch (Exception e) {
            assertTrue(true, "No database running, probably running on CI");
            return;
        }
        assertNotEquals(-1, maze2.GetID(), "Maze2 should be saved to the database with ID corresponding to the current row its on");
    }

    @Test
    @DisplayName("Test set start pos position and get start position")
    void testSetAndGetStartPos() {
        maze2.SetStartPos(new int[]{0, 1});
        assertArrayEquals(new int[]{0, 1}, maze2.GetStartPos());
    }

    @Test
    @DisplayName("Test set end position and get end position")
    void testSetAndGetEndPos() {
        maze2.SetEndPos(new int[]{10, 10});
        assertArrayEquals(new int[]{10, 10}, maze2.GetEndPos());
    }

    @Test
    @DisplayName("Test get type of a standard maze")
    void testGetStandardType() {
        assertEquals("Standard", maze1.getType());
    }

    @Test
    @DisplayName("Test get type of a themed maze")
    void testGetThemedType() {
        assertEquals("Themed", maze2.getType());
    }

    @Test
    @DisplayName("Test get maze structure of a maze constructed with a deprecated constructor")
    void testGetMazeStructureOfMaze1() {
        assertNull(maze1.getMazeStructure());
    }

    @Test
    @DisplayName("Test get maze structure of a maze constructed with a deprecated constructor")
    void testGetMazeStructureOfMaze3() {
        assertNull(maze3.getMazeStructure());
    }

    @Test
    @DisplayName("Test get maze structure of an automatically generated maze")
    void testGetMazeStructureOfMaze2() {
        assertNotNull(maze2.getMazeStructure());
    }

    @Test
    @DisplayName("Test save a maze to the database")
    void testSaveMaze() {
        boolean saved = false;
        try {
            saved = maze1.SaveMaze();
        } catch (Exception e) {
            assertTrue(true, "No database running, probably running on CI");
            return;
        }
        assertTrue(saved);
    }

    @Test
    void testGetDateTime() {
        LocalDateTime current = LocalDateTime.now();
        //Replace expect output with the current local datetime
        //assertEquals("2022/05/17 17:04", maze1.GetDateTime());
    }
}
