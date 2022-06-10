package Program;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.jupiter.api.*;

import static Program.MazeFactory.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestMazeFactory {
    MazeStructure mazeStructure;

    @Test
    @DisplayName("Test create basic maze")
    void testCreateBasicMaze() {
        mazeStructure = CreateBasicMaze(15, 15);
        assertTrue(mazeStructure instanceof MazeStructure);
        assertEquals(mazeStructure.getWidth(), 15);
        assertEquals(mazeStructure.getHeight(), 15);
    }

    @Test
    @DisplayName("Test create themed maze")
    void testCreateThemedMaze() {
        mazeStructure = CreateThemedMaze(15, 15);
        assertTrue(mazeStructure instanceof MazeStructure);

        // Only top left cell and bottom right cell of maze are image cell
        assertTrue(mazeStructure.GetBasicCell(0, 0) instanceof ImageCell);
        assertTrue(mazeStructure.GetBasicCell(14, 14) instanceof ImageCell);

        // All other cells are just basic cell
        assertFalse(mazeStructure.GetBasicCell(1, 0) instanceof ImageCell);
        assertFalse(mazeStructure.GetBasicCell(0, 1) instanceof ImageCell);
        assertFalse(mazeStructure.GetBasicCell(13, 14) instanceof ImageCell);
        assertFalse(mazeStructure.GetBasicCell(14, 13) instanceof ImageCell);

        for (int x = 1; x < 14; x++) {
            for (int y = 1; y < 14; y++)
            assertFalse(mazeStructure.GetBasicCell(x, y) instanceof ImageCell);
        }
    }

    @Test
    @DisplayName("Test create logo maze (the mazeCo image is used for testing purpose)")
    void testCreateLogoMaze() {
        // *Need to select mazeCo image from local machine as the way method designed
        mazeStructure = CreateLogoMaze(15, 15);
        assertTrue(mazeStructure instanceof MazeStructure);
        for (int x = 5; x < 10; x++) {
            for (int y = 5; y < 10; y++) {
                assertTrue(mazeStructure.GetCell(x, y) instanceof LogoCell);
            }
        }
    }
}
