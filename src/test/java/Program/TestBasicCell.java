package Program;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class TestBasicCell {
     BasicCell basicCell;
     BasicCell otherCell;

    @BeforeEach
    void setup() {
        basicCell = new BasicCell();
        otherCell = new BasicCell();
    }

    @Test
    @DisplayName("Test delete north border (set it to false)")
    void testDeleteNorthBorder() {
        basicCell.SetBorders(new boolean[] {true, true, true, true});
        basicCell.SetBorder(Direction.NORTH, false);
        assertEquals(false, basicCell.GetBorders()[0],
                "North border should be deleted");
        // Other 3 borders should remain unchanged
        assertEquals(true, basicCell.GetBorders()[1]);
        assertEquals(true, basicCell.GetBorders()[2]);
        assertEquals(true, basicCell.GetBorders()[3]);
    }

    @Test
    @DisplayName("Test create a north border (set it to true)")
    void testCreateNorthBorder() {
        basicCell.SetBorders(new boolean[] {false, false, false, false});
        basicCell.SetBorder(Direction.NORTH, true);
        assertEquals(true, basicCell.GetBorders()[0],
                "North border should be created");
        // Other 3 directions don't have borders
        assertEquals(false, basicCell.GetBorders()[1]);
        assertEquals(false, basicCell.GetBorders()[2]);
        assertEquals(false, basicCell.GetBorders()[3]);
    }

    @Test
    @DisplayName("Test connect one cell to another (left to right)")
    void testConnectLeftCellToRightCell() {
        basicCell.SetBorders(new boolean[] {true, true, true, true});
        otherCell.SetBorders(new boolean[] {true, true, true, true});
        basicCell.CreateConnection(otherCell, Direction.EAST);  // Should merge east border of left cell with west border of right cell

        // Check basic cell borders after merging
        assertEquals(false, basicCell.GetBorders()[1],
                "East border should be deleted");
        // Other 3 borders should remain unchanged
        assertEquals(true, basicCell.GetBorders()[0]);
        assertEquals(true, basicCell.GetBorders()[2]);
        assertEquals(true, basicCell.GetBorders()[3]);

        // Check other cell borders after merging
        assertEquals(false, otherCell.GetBorders()[3],
                "West border should be deleted");
        // Other 3 borders should remain unchanged
        assertEquals(true, otherCell.GetBorders()[0]);
        assertEquals(true, otherCell.GetBorders()[1]);
        assertEquals(true, otherCell.GetBorders()[2]);

        // Try to compare the actual array with an expected array directly,
        // but they are stored in different memory, apply toString for comparison instead,
        // may not be very reliable
        /*
        assertEquals("[true, false, true, true]", Arrays.toString(basicCell.GetBorders()),
                "Other 3 borders should remain unchanged");
        assertEquals("[true, true, true, false]", Arrays.toString(otherCell.GetBorders()),
                "Other 3 borders should remain unchanged");
         */
    }

    @Test
    @DisplayName("Test if a cell with no borders has any connections")
    void testCellWithNoBorders() {
        basicCell.SetBorders(false, false, false, false);
        assertEquals(true, basicCell.hasAnyConnections(),
                "The cell doesn't have any borders, should have connection to cells in 4 directions");
    }

    @Test
    @DisplayName("Test if a cell with one border has any connections")
    void testCellWithOneBorder(){
        basicCell.SetBorders(false, false, false, true);
        assertEquals(true, basicCell.hasAnyConnections(),
                "The cell has a border, should still have connection to cells in other 3 directions");
    }

    @Test
    @DisplayName("Test if a cell with all borders has any connections")
    void testCellWithAllBorders() {
        basicCell.SetBorders(true, true, true, true);
        assertEquals(false, basicCell.hasAnyConnections(),
                "The cell has borders in every direction, can't connect to any other cells");
    }

    @Test
    @DisplayName("Test if a cell with all borders has any connections after delete a border")
    void testConnectionAfterDeleteABorder() {
        basicCell.SetBorders(true, true, true, true);
        basicCell.SetBorder(Direction.NORTH, false);
        assertEquals(true, basicCell.hasAnyConnections(),
                "The cell should has connection to north cell after north border is deleted");
    }

    @Test
    @DisplayName("Test convert basic cell to image cell")
    void testConvertToImageCell() {
        basicCell.SetBorders(true, true, true, true);
        ImageCell imageCell = basicCell.ConvertToImageCell();
        assertEquals(imageCell.GetBorders(), basicCell.GetBorders());
    }

    @Test
    @DisplayName("Test if the cell is traversable")
    void testCellTraversable() {
        assertEquals(true, basicCell.isPartOfMaze());
    }

    // Exceptional test case
    @Test
    @DisplayName("Test more than four borders")
    void testSetBorders() {
        boolean[] fiveBorders = new boolean[5];
        assertThrows(Exception.class, () -> {
            basicCell.SetBorders(fiveBorders);
        });
    }

    @Test
    @DisplayName("Test less than four borders")
    void testThreeBorders() {
        boolean[] threeBorders = new boolean[3];
        assertThrows(Exception.class, () -> {
            basicCell.SetBorders(threeBorders);
        });
    }
}
