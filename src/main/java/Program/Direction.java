package Program;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    /**
     * Gets the index of the border calculated in the clockwise direction. <br/>
     * NORTH => 0 <br/>
     * EAST  => 1 <br/>
     * SOUTH => 2 <br/>
     * WEST  => 3
     *
     * @return The clockwise border index
     */
    public int GetBorderIndex() {
        switch (this) {
            case NORTH:
                return 0;

            case EAST:
                return 1;

            case SOUTH:
                return 2;

            case WEST:
                return 3;
        }
        return -1;
    }

    /**
     * Gets the opposite direction to the current direction
     *
     * @return Opposite direction
     */
    public Direction GetOppositeDirection() {
        switch (this) {
            case NORTH:
                return SOUTH;

            case EAST:
                return WEST;

            case SOUTH:
                return NORTH;

            case WEST:
                return EAST;
        }
        return null;
    }
}
