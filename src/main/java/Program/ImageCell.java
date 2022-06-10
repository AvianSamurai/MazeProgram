package Program;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageCell extends BasicCell {
    public static final int ARROW_IMAGE_SIZE = 256;
    private Image cellImage;

    /**
     * Creates a new image cell that contains a placeholder image
     *
     * @throws IOException thrown if the placeholder image couldn't be loaded, this should never happen
     */
    public ImageCell() throws IOException {
        super();
        cellImage = ImageIO.read(this.getClass().getResource("PlaceHolder.png"));

    }

    /**
     * Creates a new image cell containing the given image
     *
     * @param cellImage the image that the cellImage is to contain
     */
    public ImageCell(Image cellImage) {
        super();
        this.cellImage = cellImage;
    }

    /**
     * Get the image contained in the cell
     *
     * @return the contained buffered image
     */
    public BufferedImage GetCellImage() {
        return (BufferedImage) cellImage;
    }

    /**
     * Replaces the contained image in the cell
     *
     * @param im the new image
     */
    public void SetCellImage(Image im) {
        BufferedImage cellim = (BufferedImage) im;
        int imwidth = cellim.getWidth();
        int imheight = cellim.getHeight();

        if(imheight < imwidth) {
            cellImage = new BufferedImage(imwidth, imwidth, BufferedImage.TYPE_INT_ARGB);
            ((Graphics2D)cellImage.getGraphics()).drawImage(cellim, 0, (imwidth - imheight)/2, imwidth, imheight, null);
        } else {
            cellImage = new BufferedImage(imheight, imheight, BufferedImage.TYPE_INT_ARGB);
            ((Graphics2D)cellImage.getGraphics()).drawImage(cellim,  (imheight - imwidth)/2, 0, imwidth, imheight, null);
        }
    }

    /**
     * Sets the cell's image to be an arrow pointing in the given direction<br/>
     * Also takes into account whether this arrow is being used to represent the start of the maze or the end of the maze
     * and positions it correctly.
     *
     * @param dir The direction the arrow should point
     * @param isStart whether it is the start of the maze or not
     */
    public void SetCellArrow(Direction dir, boolean isStart) {
        SetBorder(dir, false);

        BufferedImage arrowImage;
        try {
            arrowImage = ImageIO.read(this.getClass().getResource("ArrowImage.png"));
        } catch (IOException e) { // This should never happen
            e.printStackTrace();
            return;
        }

        BufferedImage resizedArrow = new BufferedImage(ARROW_IMAGE_SIZE, ARROW_IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        ((Graphics2D)resizedArrow.getGraphics()).drawImage(arrowImage, 0, isStart ? 0 : ARROW_IMAGE_SIZE - arrowImage.getHeight(), null);

        cellImage = new BufferedImage(ARROW_IMAGE_SIZE, ARROW_IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) cellImage.getGraphics();

        switch (isStart ? dir.GetOppositeDirection() : dir) {

            case WEST:
                g.rotate(Math.PI / 2, ARROW_IMAGE_SIZE / 2, ARROW_IMAGE_SIZE / 2);
                break;

            case NORTH:
                g.rotate(Math.PI, ARROW_IMAGE_SIZE/2, ARROW_IMAGE_SIZE/2);
                break;

            case SOUTH:
                g.rotate(0, ARROW_IMAGE_SIZE/2, ARROW_IMAGE_SIZE/2);
                break;

            case EAST:
                g.rotate(3 * Math.PI / 2, ARROW_IMAGE_SIZE/2, ARROW_IMAGE_SIZE/2);
                break;
        }
        g.drawRenderedImage(resizedArrow, null);
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
