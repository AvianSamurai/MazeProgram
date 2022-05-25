package Program;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageCell extends BasicCell {
    private Image cellImage;

    public ImageCell() throws IOException {
        super();
        cellImage = ImageIO.read(this.getClass().getResource("PlaceHolder.png"));
    }

    public ImageCell(Image cellImage) {
        super();
        this.cellImage = cellImage;
    }

    public BufferedImage GetCellImage() {
        return (BufferedImage) cellImage;
    }

    public void SetCellImage(Image im) {
        cellImage = im;
    }

    @Override
    public BufferedImage getCellImageRepresentation(int width, int height) {
        BufferedImage borderedCell = super.getCellImageRepresentation(width, height);
        Graphics2D renderedCell = (Graphics2D) borderedCell.getGraphics();
        renderedCell.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        renderedCell.drawImage(cellImage, BasicCell.BORDER_WIDTH/2, BasicCell.BORDER_WIDTH/2,
                width - BasicCell.BORDER_WIDTH, height - BasicCell.BORDER_WIDTH, null);
        return  borderedCell;
    }
}
