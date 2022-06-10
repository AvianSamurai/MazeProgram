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
    public ImageCell() {
        super();
        try {
            cellImage = ImageIO.read(this.getClass().getResource("PlaceHolder.png"));
        } catch (IOException e) { // This should never happen because the resource should always be available
            e.printStackTrace();
        }

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
        // Get the image and its properties
        BufferedImage cellim = (BufferedImage) im;
        int imwidth = cellim.getWidth();
        int imheight = cellim.getHeight();

        // Sets the image size to be a square the size of the image's largest dimension
        if(imheight < imwidth) {
            cellImage = new BufferedImage(imwidth, imwidth, BufferedImage.TYPE_INT_ARGB);
            ((Graphics2D)cellImage.getGraphics()).drawImage(cellim, 0, (imwidth - imheight)/2, imwidth, imheight, null);
        } else {
            cellImage = new BufferedImage(imheight, imheight, BufferedImage.TYPE_INT_ARGB);
            ((Graphics2D)cellImage.getGraphics()).drawImage(cellim,  (imheight - imwidth)/2, 0, imwidth, imheight, null);
        }
    }

    public void SetCellArrow(Direction dir, boolean isStart) {
        SetBorder(dir, false);

        // Load the arrow image
        BufferedImage arrowImage;
        try {
            arrowImage = ImageIO.read(this.getClass().getResource("ArrowImage.png"));
        } catch (IOException e) { // This should never happen
            e.printStackTrace();
            return;
        }

        // Resize the image
        BufferedImage resizedArrow = new BufferedImage(ARROW_IMAGE_SIZE, ARROW_IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        ((Graphics2D)resizedArrow.getGraphics()).drawImage(arrowImage, 0, isStart ? 0 : ARROW_IMAGE_SIZE - arrowImage.getHeight(), null);

        // Create a buffered image to hold the final arrow image
        cellImage = new BufferedImage(ARROW_IMAGE_SIZE, ARROW_IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) cellImage.getGraphics();

        // Works out the rotation the array should have based on what side of the maze it is next to and applies the
        // rotation
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

        // Draw the rotated arrow onto the buffered image
        g.drawRenderedImage(resizedArrow, null);
    }

    @Override
    public BufferedImage getCellImageRepresentation(int width, int height) {
        // Get a buffered image containing the borders
        BufferedImage borderedCell = super.getCellImageRepresentation(width, height);
        Graphics2D renderedCell = (Graphics2D) borderedCell.getGraphics();

        // Render the cell image onto the buffered image
        renderedCell.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        renderedCell.drawImage(cellImage, BasicCell.BORDER_WIDTH/2, BasicCell.BORDER_WIDTH/2,
                width - BasicCell.BORDER_WIDTH, height - BasicCell.BORDER_WIDTH, null);

        return  borderedCell;
    }
}
