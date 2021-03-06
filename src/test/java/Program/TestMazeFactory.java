package Program;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.jupiter.api.*;

import java.awt.image.BufferedImage;

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
        mazeStructure = CreateLogoMazeTestHook(new BufferedImage(30, 45, BufferedImage.TYPE_INT_ARGB), null, 15, 15);
        assertTrue(mazeStructure instanceof MazeStructure);
        boolean anyCellIsLogoCell = false;
        for (int x = 0; x < mazeStructure.getWidth(); x++) {
            for (int y = 0; y < mazeStructure.getHeight(); y++) {
                if(mazeStructure.GetCell(x, y) instanceof LogoCell) {
                    anyCellIsLogoCell = true;
                }
            }
        }

        assertTrue(anyCellIsLogoCell);
    }
}
