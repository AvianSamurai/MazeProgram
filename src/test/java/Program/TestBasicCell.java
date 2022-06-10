package Program;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestBasicCell {
    BasicCell basicCell;

    @BeforeEach
    void setup() {
        basicCell = new BasicCell();
    }

    @Test
    @DisplayName("Test convert basic cell to image cell")
    void testConvertToImageCell() {
        basicCell.SetBorders(true, true, true, true);
        ImageCell imageCell = basicCell.ConvertToImageCell();
        assertEquals(imageCell.GetBorders(), basicCell.GetBorders());
    }

    @Test
    @DisplayName("Test convert basic cell to image cell if image cell is null")
    void testConvertToNullImageCell() {
        basicCell.SetBorders(true, true, true, true);
        ImageCell imageCell = null;
        assertThrows(Exception.class, () -> {
            imageCell.GetBorders();
        });
    }

    @Test
    @DisplayName("Test if the cell is traversable")
    void testCellTraversable() {
        assertTrue(basicCell.isPartOfMaze());
    }

    @Test
    @DisplayName("Test if the cell is not traversable")
    void testCellNotTraversable() {
        assertFalse(!basicCell.isPartOfMaze());
    }
}
