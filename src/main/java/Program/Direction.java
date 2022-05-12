package Program;

public enum Direction {
    NORTH(0), SOUTH(2), EAST(1), WEST(3);

    private int cardinalDir;
    Direction(int dir) {
        cardinalDir = dir;
    }

    /**
     * Gets an int representing the direction calculated in the clockwise direction. <br/>
     * NORTH => 0 <br/>
     * EAST  => 1 <br/>
     * SOUTH => 2 <br/>
     * WEST  => 3
     *
     * @return The clockwise border index
     */
    public int GetIntValue() {
        return this.cardinalDir;
    }

    /**
     * Gets the opposite direction to the current direction
     *
     * @return Opposite direction
     */
    public Direction GetOppositeDirection() {
        int newDir = GetIntValue() + 2;
        return IntToDirection(newDir > 3 ? newDir - 4 : newDir);
    }

    /**
     * Returns a direction derived from an int calculated in the clockwise direction
     *
     * @param i int to convert
     * @return Direction calculated from int
     */
    public static Direction IntToDirection(int i) {
        switch (i) {
            case 0:
                return NORTH;

            case 1:
                return EAST;

            case 2:
                return SOUTH;

            case 3:
                return WEST;
        }
        return null;
    }

    /**
     * returns a set of x, y co-ordinates representing an offset based on a direction <br/>
     * [0, -1] for North<br/>
     * [0,  1] for South<br/>
     * [1,  0] for East<br/>
     * [-1, 0] for West
     *
     * @return the offset coordinate
     */
    public int[] GetOffset() {
        switch (this) {
            case NORTH:
                return new int[] {0, -1};

            case SOUTH:
                return new int[] {0, 1};

            case EAST:
                return new int[] {1, 0};

            default:
                return new int[]{-1, 0};
        }
    }

    /**
     * Converts an X and Y value to a direction, direction must be cardinal (no diagonals) and ints must be 0 or 1
     *
     * @param x x offset coordinate
     * @param y y offset coordinate
     * @return the direction
     */
    public static Direction OffsetToDirection(int x, int y) {
        if(x == 0 && y == -1) { return NORTH; }
        if(x == 1 && y == 0) { return EAST; }
        if(x == 0 && y == 1) { return SOUTH; }
        if(x == -1 && y == 0) { return WEST; }
        return null;
    }
}
