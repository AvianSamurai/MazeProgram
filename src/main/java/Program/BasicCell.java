package Program;

public class BasicCell implements I_Cell {

    boolean[] borders;

    public BasicCell() {
        borders = new boolean[]{true, true, true, true};
    }

    /**
     * Removes the border in the given direction of this cell and removes the border of the other cell in the opposite
     * direction, this results in the cells appearing to be connected
     *
     * @param otherCell The other cell to connect to
     * @param dir the direction of the other cell from this cell
     */
    public void CreateConnection(BasicCell otherCell, Direction dir) {
        borders[dir.GetBorderIndex()] = false;
        otherCell.SetBorder(dir.GetOppositeDirection(), false);
    }

    /**
     * used for setting the state of all borders at the same time
     *
     * @param north true if cell should have a north border
     * @param east true if cell should have an east border
     * @param south true if cell should have a south border
     * @param west true if cell should have a west border
     */
    public void SetBorders(boolean north, boolean east, boolean south, boolean west) {
        borders[0] = north;
        borders[1] = east;
        borders[2] = south;
        borders[3] = west;
    }

    /**
     * Sets whether the cell should have a border or not in the given direction
     *
     * @param borderDirection the direction of the border from the center of the cell to change the state of
     * @param hasBorder true if the cell has a border
     */
    public void SetBorder(Direction borderDirection, boolean hasBorder) {
        borders[borderDirection.GetBorderIndex()] = hasBorder;
    }

    /**
     * Gets an array of booleans where each boolean represents whether a cell has a border or not in that direction
     * indexes starts at 0 for north and increment in the clockwise direction
     *
     * @return an array of border indexes
     */
    public boolean[] GetBorders() {
        return borders;
    }

    /**
     * Checks if this cell has any open borders
     *
     * @return true if connected
     */
    public boolean hasAnyConnections() {
        for(boolean b : borders) {
            if (!b) return true;
        }
        return false;
    }

    @Override
    public boolean isPartOfMaze() {
        return true;
    }
}
