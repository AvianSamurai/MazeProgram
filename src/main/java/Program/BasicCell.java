package Program;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BasicCell implements I_Cell {

    public static final int BORDER_WIDTH = 2;

    private boolean[] borders;

    public BasicCell() {
        borders = new boolean[]{true, true, true, true};
    }

    /**
     * Removes the border of this cell in the given direction and removes the border of the other cell in the opposite
     * direction, this results in the cells appearing to be connected
     *
     * @param otherCell The other cell to connect to
     * @param dir the direction of the other cell from this cell
     */
    public void CreateConnection(BasicCell otherCell, Direction dir) {
        borders[dir.GetIntValue()] = false;
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

    public ImageCell ConvertToImageCell() {
        ImageCell imCell = new ImageCell();
        imCell.SetBorders(borders);
        return imCell;
    }

    @Override
    public boolean isPartOfMaze() {
        return true;
    }

    @Override
    public BufferedImage getCellImageRepresentation(int width, int height) {
        BufferedImage cellImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) cellImage.getGraphics();

        g.setBackground(Color.white);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(BORDER_WIDTH));

        if(GetBorders()[0]) {
            g.drawLine(0, 0, width, 0);
        }
        if(GetBorders()[1]) {
            g.drawLine(width, 0, width, height);
        }
        if(GetBorders()[2]) {
            g.drawLine(0, height, width, height);
        }
        if(GetBorders()[3]) {
            g.drawLine(0, 0, 0, height);
        }

        g.dispose();
        return cellImage;
    }
}
