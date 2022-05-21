package Program;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BorderedCell {

    public static final int BORDER_WIDTH = 2;

    private boolean[] borders = new boolean[4];

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
     * used for setting the state of all borders to a given array. The array ordering is clockwise (north, east, south, west).
     *
     * @param borders an array of booleans representing border state
     */
    public void SetBorders(boolean[] borders) {
        if(borders.length != 4) {
            throw new IndexOutOfBoundsException(
                    String.format("border arrays must be 4 elements long, size given was %s", borders.length));
        }

        this.borders = borders;
    }

    /**
     * Sets whether the cell should have a border or not in the given direction
     *
     * @param borderDirection the direction of the border from the center of the cell to change the state of
     * @param hasBorder true if the cell has a border
     */
    public void SetBorder(Direction borderDirection, boolean hasBorder) {
        borders[borderDirection.GetIntValue()] = hasBorder;
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
     * Removes the border of this cell in the given direction and removes the border of the other cell in the opposite
     * direction, this results in the cells appearing to be connected
     *
     * @param otherCell The other cell to connect to
     * @param dir the direction of the other cell from this cell
     */
    public void CreateConnection(BasicCell otherCell, Direction dir) {
        SetBorder(dir, false);
        otherCell.SetBorder(dir.GetOppositeDirection(), false);
    }

    /**
     * Creates a border on this cell in the given direction, and creates a border on the other cell in the
     * opposite direction, appearing to disconnect the cells
     *
     * @param otherCell The other cell to disconnect from
     * @param dir the direction of the other cell from this cell
     */
    public void BlockConnection(BasicCell otherCell, Direction dir) {
        SetBorder(dir, true);
        otherCell.SetBorder(dir.GetOppositeDirection(), true);
    }

    /**
     * Checks if this cell has any open borders
     *
     * @return true if connected
     */
    public boolean hasAnyConnections() {
        for(boolean b : GetBorders()) {
            if (!b) return true;
        }
        return false;
    }

    /**
     * Edits a buffered image by adding the borders to it
     *
     * @param input the buffered image to edit
     * @return the edited image, this edit will also be applied to the input variable as that variable is a reference.
     * This return if purely for convenience.
     */
    public BufferedImage DrawBorders (BufferedImage input) {
        Graphics2D g = (Graphics2D) input.getGraphics();
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(BORDER_WIDTH));

        if(GetBorders()[0]) {
            g.drawLine(0, 0, input.getWidth(), 0);
        }
        if(GetBorders()[1]) {
            g.drawLine(input.getWidth(), 0, input.getWidth(), input.getHeight());
        }
        if(GetBorders()[2]) {
            g.drawLine(0, input.getHeight(), input.getWidth(), input.getHeight());
        }
        if(GetBorders()[3]) {
            g.drawLine(0, 0, 0, input.getHeight());
        }

        g.dispose();
        return input;
    }
}
