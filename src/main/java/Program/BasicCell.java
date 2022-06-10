package Program;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BasicCell extends BorderedCell implements I_Cell {

    public BasicCell() {
        this.SetBorders(true, true, true, true);
    }

    /**
     * Converts the basic cell to an image cell with the same borders
     *
     * @return an image cell with the same borders as the previous basic cell
     */
    public ImageCell ConvertToImageCell() {
        ImageCell imCell;
        imCell = new ImageCell();
        imCell.SetBorders(GetBorders());
        return imCell;
    }

    @Override
    public boolean isPartOfMaze() {
        return true;
    }

    @Override
    public BufferedImage getCellImageRepresentation(int width, int height) {
        // Create the buffered image and get the graphics engine
        BufferedImage cellImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) cellImage.getGraphics();

        // Sets the background of the buffered image to white
        g.setBackground(Color.white);
        g.fillRect(0, 0, width, height);
        g.dispose();

        // Add the borders to the buffered image
        DrawBorders(cellImage);

        return cellImage;
    }
}
