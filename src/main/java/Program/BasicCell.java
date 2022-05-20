package Program;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BasicCell extends BorderedCell implements I_Cell {

    public BasicCell() {
        this.SetBorders(true, true, true, true);
    }

    public ImageCell ConvertToImageCell() {
        ImageCell imCell = new ImageCell();
        imCell.SetBorders(GetBorders());
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
        g.dispose();

        DrawBorders(cellImage);

        return cellImage;
    }
}
