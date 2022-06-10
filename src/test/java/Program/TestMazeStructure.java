package Program;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestMazeStructure {
    MazeStructure mazeStructure;

    @BeforeEach
    void setup() {
        mazeStructure = new MazeStructure(10, 10);
    }

    // Regular test cases
    @Test
    @DisplayName("Test get cell (1, 1)")
    void GetCellWhereXEqualsY() {
        assertEquals(mazeStructure.getCells()[1][1], mazeStructure.GetCell(1, 1));
    }

    @Test
    @DisplayName("Test get cell (2, 8)")
    void GetCellWhereXNotEqualsY() {
        assertEquals(mazeStructure.getCells()[2][8], mazeStructure.GetCell(2, 8));
    }

    // Boundary test cases
    @Test
    @DisplayName("Test get cell (0, 0)")
    void GetFirstCell() {
        assertEquals(mazeStructure.getCells()[0][0], mazeStructure.GetCell(0, 0));
    }

    @Test
    @DisplayName("Test get cell (9, 9)")
    void GetLastCell() {
        assertEquals(mazeStructure.getCells()[9][9], mazeStructure.GetCell(9, 9));
    }

    // Exceptional test cases
    @Test
    @DisplayName("Test get cell (-1, 1)")
    void GetCellNegativeX() {
        assertThrows(Exception.class, () -> {
            mazeStructure.GetCell(-1, 1);
        });
    }

    @Test
    @DisplayName("Test get cell (1, -1)")
    void GetCellNegativeY() {
        assertThrows(Exception.class, () -> {
            mazeStructure.GetCell(1, -1);
        });
    }

    @Test
    @DisplayName("Test get cell (-1, -1)")
    void GetCellNegativeXAndY() {
        assertThrows(Exception.class, () -> {
            mazeStructure.GetCell(-1, -1);
        });
    }

    @Test
    @DisplayName("Test get cell (10, 5)")
    void GetCellXLargerThanWidth() {
        assertThrows(Exception.class, () -> {
            mazeStructure.GetCell(10, 5);
        });
    }

    @Test
    @DisplayName("Test get cell (5, 10)")
    void GetCellYLargerThanHeight() {
        assertThrows(Exception.class, () -> {
            mazeStructure.GetCell(5, 10);
        });
    }

    @Test
    @DisplayName("Test get cell (-1, 11)")
    void GetCellNegativeXAndYTooLarge() {
        assertThrows(Exception.class, () -> {
            mazeStructure.GetCell(-1, 10);
        });
    }

    @Test
    @DisplayName("Test get cell (10, -1)")
    void GetCellNegativeYAndXTooLarge() {
        assertThrows(Exception.class, () -> {
            mazeStructure.GetCell(10, -1);
        });
    }

    // Normal test cases
    @Test
    @DisplayName("Test set cell at any position to another and get cell at both position")
    void SetCellAndGetCell() {
        I_Cell aCell = mazeStructure.getCells()[1][2];
        mazeStructure.SetCell(aCell, 3, 4);
        assertEquals(mazeStructure.GetCell(3, 4), aCell,
                "It should not change the cell at its original position, " +
                        "but set the new position to the cell as well");
        assertEquals(mazeStructure.GetCell(3, 4), aCell);
    }

    // Boundary test cases
    @Test
    @DisplayName("Test set cell at (9, 9) to (0, 0)")
    void SetFirstCellToLastCell() {
        I_Cell aCell = mazeStructure.getCells()[9][9];
        mazeStructure.SetCell(aCell, 0, 0);
        assertEquals(mazeStructure.GetCell(0, 0), aCell);
    }

    @Test
    @DisplayName("Test set cell at (0, 0) to (9, 9)")
    void SetLastCellToFirstCell() {
        I_Cell aCell = mazeStructure.getCells()[0][0];
        mazeStructure.SetCell(aCell, 9, 9);
        assertEquals(mazeStructure.GetCell(9, 9), aCell);
    }

    // Exceptional test cases
    @Test
    @DisplayName("Test set cell at any position to (-1, -1)")
    void SetCellNegative() {
        I_Cell aCell = mazeStructure.getCells()[0][0];
        assertThrows(Exception.class, () -> {
            mazeStructure.SetCell(aCell, -1, -1);
        });
    }

    @Test
    @DisplayName("Test set cell at any position to (-1, -1)")
    void SetCellExceedMazeSize() {
        I_Cell aCell = mazeStructure.getCells()[0][0];
        assertThrows(Exception.class, () -> {
            mazeStructure.SetCell(aCell, 10, 10);
        });
    }

    // Normal test cases
    @Test
    @DisplayName("Test get neighbours of a basic cell (1, 1)")
    void GetNeighboursOfX1Y1() {
        BasicCell[] cellNeighbors = mazeStructure.GetBasicCellNeighbors(1, 1);
        assertEquals(mazeStructure.GetBasicCell(1, 0), cellNeighbors[0]);
        assertEquals(mazeStructure.GetBasicCell(2, 1), cellNeighbors[1]);
        assertEquals(mazeStructure.GetBasicCell(1, 2), cellNeighbors[2]);
        assertEquals(mazeStructure.GetBasicCell(0, 1), cellNeighbors[3]);
    }

    @Test
    @DisplayName("Test get neighbours of a basic cell (4, 5)")
    void GetNeighboursOfX4Y5() {
        BasicCell[] cellNeighbors = mazeStructure.GetBasicCellNeighbors(4, 5);
        assertEquals(mazeStructure.GetBasicCell(4, 4), cellNeighbors[0]);
        assertEquals(mazeStructure.GetBasicCell(5, 5), cellNeighbors[1]);
        assertEquals(mazeStructure.GetBasicCell(4, 6), cellNeighbors[2]);
        assertEquals(mazeStructure.GetBasicCell(3, 5), cellNeighbors[3]);

        // Test get directions to valid cells with four directions carvable
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        assertArrayEquals(directions, mazeStructure.GetDirectionsToValidCells(4, 5, true));
    }

    // Boundary test cases
    @Test
    @DisplayName("Test get neighbours of any cell on the first row (5, 0)")
    void GetNeighboursOfX5Y0() {
        BasicCell[] cellNeighbors = mazeStructure.GetBasicCellNeighbors(5, 0);
        assertNull(cellNeighbors[0], "Top border reached");
        assertEquals(mazeStructure.GetBasicCell(6, 0), cellNeighbors[1]);
        assertEquals(mazeStructure.GetBasicCell(5, 1), cellNeighbors[2]);
        assertEquals(mazeStructure.GetBasicCell(4, 0), cellNeighbors[3]);

        // Test get directions to valid cells on the first row which has three directions carvable
        Direction[] directions = {Direction.EAST, Direction.SOUTH, Direction.WEST};
        assertArrayEquals(directions, mazeStructure.GetDirectionsToValidCells(5, 0, true));
    }

    @Test
    @DisplayName("Test get neighbours of any cell on the first column (0, 4)")
    void GetNeighboursOfX0Y4() {
        BasicCell[] cellNeighbors = mazeStructure.GetBasicCellNeighbors(0, 4);
        assertEquals(mazeStructure.GetBasicCell(0, 3), cellNeighbors[0]);
        assertEquals(mazeStructure.GetBasicCell(1, 4), cellNeighbors[1]);
        assertEquals(mazeStructure.GetBasicCell(0, 5), cellNeighbors[2]);
        assertNull(cellNeighbors[3], "Left border reached");

        // Test get directions to valid cells on the first column which has three directions carvable
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH};
        assertArrayEquals(directions, mazeStructure.GetDirectionsToValidCells(0, 4, true));
    }

    @Test
    @DisplayName("Test get neighbours of any cell on the last row (3, 9)")
    void GetNeighboursOfX3Y9() {
        BasicCell[] cellNeighbors = mazeStructure.GetBasicCellNeighbors(3, 9);
        assertEquals(mazeStructure.GetBasicCell(3, 8), cellNeighbors[0]);
        assertEquals(mazeStructure.GetBasicCell(4, 9), cellNeighbors[1]);
        assertNull(cellNeighbors[2], "Bottom border reached");
        assertEquals(mazeStructure.GetBasicCell(2, 9), cellNeighbors[3]);

        // Test get directions to valid cells on the last row which has three directions carvable
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.WEST};
        assertArrayEquals(directions, mazeStructure.GetDirectionsToValidCells(3, 9, true));
    }

    @Test
    @DisplayName("Test get neighbours of any cell on the last column (9, 8)")
    void GetNeighboursOfX9Y8() {
        BasicCell[] cellNeighbors = mazeStructure.GetBasicCellNeighbors(9, 8);
        assertEquals(mazeStructure.GetBasicCell(9, 7), cellNeighbors[0]);
        assertNull(cellNeighbors[1], "Right border reached");
        assertEquals(mazeStructure.GetBasicCell(9, 9), cellNeighbors[2]);
        assertEquals(mazeStructure.GetBasicCell(8, 8), cellNeighbors[3]);

        // Test get directions to valid cells on the last column which has three directions carvable
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.WEST};
        assertArrayEquals(directions, mazeStructure.GetDirectionsToValidCells(9, 8, true));
    }

    @Test
    @DisplayName("Test get neighbours of cell on the top left corner (0, 0)")
    void GetNeighboursOfX0Y0() {
        BasicCell[] cellNeighbors = mazeStructure.GetBasicCellNeighbors(0, 0);
        assertNull(cellNeighbors[0], "Top border reached");
        assertEquals(mazeStructure.GetBasicCell(1, 0), cellNeighbors[1]);
        assertEquals(mazeStructure.GetBasicCell(0, 1), cellNeighbors[2]);
        assertNull(cellNeighbors[3], "Left border reached");

        // Test get directions to valid cells in top left corner which has three directions carvable
        Direction[] directions = {Direction.EAST, Direction.SOUTH};
        assertArrayEquals(directions, mazeStructure.GetDirectionsToValidCells(0, 0, true));
    }

    @Test
    @DisplayName("Test get neighbours of cell on the bottom left corner (0, 9)")
    void GetNeighboursOfX0Y9() {
        BasicCell[] cellNeighbors= mazeStructure.GetBasicCellNeighbors(0, 9);
        assertEquals(mazeStructure.GetBasicCell(0, 8), cellNeighbors[0]);
        assertEquals(mazeStructure.GetBasicCell(1, 9), cellNeighbors[1]);
        assertNull(cellNeighbors[2], "South border reached");
        assertNull(cellNeighbors[3], "Left border reached");

        // Test get directions to valid cells in bottom left corner which has three directions carvable
        Direction[] directions = {Direction.NORTH, Direction.EAST};
        assertArrayEquals(directions, mazeStructure.GetDirectionsToValidCells(0, 9, true));
    }

    @Test
    @DisplayName("Test get neighbours of cell on the top right corner (9, 0)")
    void GetNeighboursOfX9Y0() {
        BasicCell[] cellNeighbors = mazeStructure.GetBasicCellNeighbors(9, 0);
        assertNull(cellNeighbors[0], "Top border reached");
        assertNull(cellNeighbors[1], "Right border reached");
        assertEquals(mazeStructure.GetBasicCell(9, 1), cellNeighbors[2]);
        assertEquals(mazeStructure.GetBasicCell(8, 0), cellNeighbors[3]);

        // Test get directions to valid cells in top right corner which has three directions carvable
        Direction[] directions = {Direction.SOUTH, Direction.WEST};
        assertArrayEquals(directions, mazeStructure.GetDirectionsToValidCells(9, 0, true));
    }

    @Test
    @DisplayName("Test get neighbours of cell on the bottom right corner (9, 9)")
    void GetNeighboursOfX9Y9() {
        BasicCell[] cellNeighbors = mazeStructure.GetBasicCellNeighbors(9, 9);
        assertEquals(mazeStructure.GetBasicCell(9, 8), cellNeighbors[0]);
        assertNull(cellNeighbors[1], "Right border reached");
        assertNull(cellNeighbors[2], "South border reached");
        assertEquals(mazeStructure.GetBasicCell(8, 9), cellNeighbors[3]);

        // Test get directions to valid cells in bottom right corner which has three directions carvable
        Direction[] directions = {Direction.NORTH, Direction.WEST};
        assertArrayEquals(directions, mazeStructure.GetDirectionsToValidCells(9, 9, true));
    }

    @Test
    @DisplayName("Test get directions to valid cells when not allowing the cell to connect with others")
    void GetDirectionsToValidCellOfCellWithNoConnectionAllowed() {
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        assertArrayEquals(directions, mazeStructure.GetDirectionsToValidCells(2, 8, false),
                "The cell has connection to other cells, check hasAnyConnections()");
    }

    // Exceptional test cases
    @Test
    @DisplayName("Test get neighbours of an invalid cell (-1, 10)")
    void GetNeighboursOutOfBoundary() {
        assertThrows(Exception.class, () -> {
            mazeStructure.GetBasicCellNeighbors(-1, 10);
        });
    }

    // Normal cases
    @Test
    @DisplayName("Test get directions to valid cells")
    void GetNormalDirection() {
        Direction[] cellNeighbours = mazeStructure.GetDirectionsToValidCells(3, 7, false);
        Direction[] expectedDirections = new Direction[] {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        assertArrayEquals(expectedDirections, cellNeighbours);
    }

    @Test
    @DisplayName("Test get directions to valid cells")
    void GetDirection() {
        Direction[] cellNeighbours = mazeStructure.GetDirectionsToValidCells(0, 0, false);
        Direction[] expectedDirections = new Direction[] {Direction.EAST, Direction.SOUTH};
        assertArrayEquals(expectedDirections, cellNeighbours);
    }

    @Test
    @DisplayName("Test get default width and default height")
    void GetDefaultDimension() {
        assertEquals(10, mazeStructure.getWidth());
        assertEquals(10, mazeStructure.getHeight());
    }

    @Test
    @DisplayName("Test get width and height of new maze")
    void GetDimensionOfNewMazeStructure() {
        MazeStructure newMazeStructure = new MazeStructure(4, 6);
        assertEquals(4, newMazeStructure.getWidth());
        assertEquals(6, newMazeStructure.getHeight());
    }

    // Normal cases
    @Test
    @DisplayName("Test insert cell to a given position")
    void TestInsertCell() {
        BasicCell newCell = new BasicCell();
        mazeStructure.InsertCell(2, 8, newCell);
        assertEquals(newCell, mazeStructure.GetCell(2, 8));
    }

    @Test
    @DisplayName("Test replace the cell at a given position with the new cell")
    void InsertCell() {
        BasicCell originalCell = new BasicCell();
        BasicCell newCell = new BasicCell();

        mazeStructure.SetCell(originalCell, 3, 4);
        assertEquals(originalCell, mazeStructure.GetCell(3, 4));
        assertNotEquals(newCell, mazeStructure.GetCell(3, 4));

        mazeStructure.InsertCell(3, 4, newCell);
        assertEquals(newCell, mazeStructure.GetCell(3, 4));
        assertNotEquals(originalCell, mazeStructure.GetCell(3, 4),
                "Original cell should be replaced with new cell");
    }

    // Exceptional test cases
    @Test
    @DisplayName("Test insert cell")
    void InsertCell1() {
        BasicCell newCell = new BasicCell();
        assertThrows(Exception.class, () -> {
            mazeStructure.InsertCell(-1, 11, newCell);
        });
    }
}
