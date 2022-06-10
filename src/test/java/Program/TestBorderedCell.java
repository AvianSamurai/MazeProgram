package Program;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestBorderedCell {
     BasicCell basicCell;
     BasicCell anotherCell;

    @BeforeEach
    void setup() {
        basicCell = new BasicCell();
        anotherCell = new BasicCell();
    }

    @Test
    @DisplayName("Test delete north border (set it to false)")
    void testDeleteNorthBorder() {
        basicCell.SetBorders(new boolean[] {true, true, true, true});
        basicCell.SetBorder(Direction.NORTH, false);
        assertArrayEquals(new boolean[] {false, true, true, true}, basicCell.GetBorders(),
                "North border should be deleted and other 3 borders should remain unchanged");
    }

    @Test
    @DisplayName("Test create a north border (set it to true)")
    void testCreateNorthBorder() {
        basicCell.SetBorders(new boolean[] {false, false, false, false});
        basicCell.SetBorder(Direction.NORTH, true);
        assertArrayEquals(new boolean[] {true, false, false, false}, basicCell.GetBorders(),
                "North border should be created and other 3 borders should remain with no borders");
    }

    @Test
    @DisplayName("Test connect one cell to another (left to right)")
    void testConnectLeftCellToRightCell() {
        basicCell.SetBorders(new boolean[] {true, true, true, true});
        anotherCell.SetBorders(new boolean[] {true, true, true, true});
        basicCell.CreateConnection(anotherCell, Direction.EAST);  // Should merge east border of left cell with west border of right cell

        // Check borders of the basic cell after merging
        assertArrayEquals(new boolean[] {true, false, true, true}, basicCell.GetBorders(),
                "East border should be deleted and other 3 borders should remain unchanged");

        // Check borders of another cell after merging
        assertArrayEquals(new boolean[] {true, true, true, false}, anotherCell.GetBorders(),
                "West border should be deleted and other 3 borders should remain unchanged");
    }

    @Test
    @DisplayName("Test block a cell with its north cell by creating a border between them")
    void testBlockConnection() {
        basicCell.SetBorders(false, false, false, false);
        anotherCell.SetBorders(false, false, false, false);
        basicCell.BlockConnection(anotherCell, Direction.NORTH);
        assertArrayEquals(new boolean[] {true, false, false, false}, basicCell.GetBorders());
        assertArrayEquals(new boolean[] {false, false, true, false}, anotherCell.GetBorders());
    }

    @Test
    @DisplayName("Test creating connection to two neighbour cells first then block their connection")
    void testBlockConnectionAfterCreatingConnection() {
        basicCell.SetBorders(true, true, true, true);
        anotherCell.SetBorders(true, true, true, true);

        // Create connection to its east cell first
        basicCell.CreateConnection(anotherCell, Direction.EAST);
        assertArrayEquals(new boolean[] {true, false, true, true}, basicCell.GetBorders());
        assertArrayEquals(new boolean[] {true, true, true, false}, anotherCell.GetBorders());

        // Block the connection just created
        basicCell.BlockConnection(anotherCell, Direction.EAST);
        assertArrayEquals(new boolean[] {true, true, true, true}, basicCell.GetBorders());
        assertArrayEquals(new boolean[] {true, true, true, true}, anotherCell.GetBorders());
    }

    @Test
    @DisplayName("Test border count of a cell with no borders")
    void testGetBorderCountIfNoBorders() {
        basicCell.SetBorders(false, false, false, false);
        assertEquals(0, basicCell.GetBorderCount());
    }

    @Test
    @DisplayName("Test border count of a cell with only one border")
    void testGetBorderCountIfOnlyOneBorder() {
        basicCell.SetBorders(true, false, false, false);
        assertEquals(1, basicCell.GetBorderCount());
    }

    @Test
    @DisplayName("Test border count of a cell with four borders")
    void testGetBorderCountIfFullBordered() {
        basicCell.SetBorders(true, true, true, true);
        assertEquals(4, basicCell.GetBorderCount());
    }

    @Test
    @DisplayName("Test if a cell with no borders has any connections")
    void testCellWithNoBorders() {
        basicCell.SetBorders(false, false, false, false);
        assertTrue(basicCell.hasAnyConnections(), "The cell doesn't have any borders, should have connection to cells in 4 directions");
    }

    @Test
    @DisplayName("Test if a cell with one border has any connections")
    void testCellWithOneBorder(){
        basicCell.SetBorders(false, false, false, true);
        assertTrue(basicCell.hasAnyConnections(), "The cell has a border, should still have connection to cells in other 3 directions");
    }

    @Test
    @DisplayName("Test if a cell with all borders has any connections")
    void testCellWithAllBorders() {
        basicCell.SetBorders(true, true, true, true);
        assertFalse(basicCell.hasAnyConnections(), "The cell has borders in every direction, can't connect to any other cells");
    }

    @Test
    @DisplayName("Test if a cell with all borders has any connections after delete a border")
    void testConnectionAfterDeleteABorder() {
        basicCell.SetBorders(true, true, true, true);
        basicCell.SetBorder(Direction.NORTH, false);
        assertTrue(basicCell.hasAnyConnections(), "The cell should has connection to north cell after north border is deleted");
    }

    // Exceptional test cases
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
