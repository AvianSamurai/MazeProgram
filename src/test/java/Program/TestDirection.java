package Program;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestDirection {
    Direction north;
    Direction east;
    Direction south;
    Direction west;

    @BeforeEach
    void setup() {
        north = Direction.NORTH;
        east = Direction.EAST;
        south = Direction.SOUTH;
        west = Direction.WEST;
    }

    //Test method GetIntValue()
    @Test
    @DisplayName("Test get north direction")
    void testGetNorthValue() {
        assertEquals(0, north.GetIntValue());
    }

    @Test
    @DisplayName("Test get east direction")
    void testGetEastValue() {
        assertEquals(1, east.GetIntValue());
    }

    @Test
    @DisplayName("Test get south direction")
    void testGetSouthValue() {
        assertEquals(2, south.GetIntValue());
    }

    @Test
    @DisplayName("Test get west direction")
    void testGetWestValue() {
        assertEquals(3, west.GetIntValue());
    }

    //Test method GetOppositeDirection()
    @Test
    @DisplayName("Test get opposite direction to north")
    void testOppositeToNorth() {
        assertEquals(Direction.SOUTH, north.GetOppositeDirection());
    }

    @Test
    @DisplayName("Test get opposite direction to east")
    void testOppositeToEast() {
        assertEquals(Direction.WEST, east.GetOppositeDirection());
    }

    @Test
    @DisplayName("Test get opposite direction to south")
    void testOppositeToSouth() {
        assertEquals(Direction.NORTH, south.GetOppositeDirection());
    }

    @Test
    @DisplayName("Test get opposite direction to west")
    void testOppositeToWest() {
        assertEquals(Direction.EAST, west.GetOppositeDirection());
    }

    @Test
    @DisplayName("Test derive direction from 0")
    void test0ToDirection() {
        assertEquals(Direction.NORTH, Direction.IntToDirection(0));
    }

    @Test
    @DisplayName("Test derive direction from 1")
    void test1ToDirection() {
        assertEquals(Direction.EAST, Direction.IntToDirection(1));
    }

    @Test
    @DisplayName("Test derive direction from 2")
    void test2ToDirection() {
        assertEquals(Direction.SOUTH, Direction.IntToDirection(2));
    }

    @Test
    @DisplayName("Test derive direction from 3")
    void test3ToDirection() {
        assertEquals(Direction.WEST, Direction.IntToDirection(3));
    }

    @Test
    @DisplayName("Get offset based on north")
    void testGetOffsetNorth(){
        assertArrayEquals(new int[] {0, -1}, north.GetOffset());
    }

    @Test
    @DisplayName("Get offset based on south")
    void testGetOffsetSouth(){
        assertArrayEquals(new int[] {0, 1}, south.GetOffset());
    }

    @Test
    @DisplayName("Get offset based on east")
    void testGetOffsetEast(){
        assertArrayEquals(new int[] {1, 0}, east.GetOffset());
    }

    @Test
    @DisplayName("Get offset based on west")
    void testGetOffsetWast(){
        assertArrayEquals(new int[] {-1, 0}, west.GetOffset());
    }
}
