package Program;

import Utils.Debug;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestImageCell {

    ImageCell im;
    BufferedImage bi;

    @BeforeEach
    void setup() {
        bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();

        g.setColor(Color.GREEN);
        g.drawRect(8, 8, 16, 16);

        im = new ImageCell(bi);
    }

    @Test
    @DisplayName("Test get image is image cell was created with")
    void testGetImageIsImage() {
        assertTrue(im.GetCellImage().equals(bi));
    }

    @Test
    @DisplayName("Test set image replaces the image in the cell")
    void testSetImageIsImage() {
        BufferedImage bi2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        g2.setColor(Color.GREEN);
        g2.drawRect(8, 8, 16, 16);

        im.SetCellImage(bi2);

        assertTrue(!im.GetCellImage().equals(bi));
    }

    @Test
    @DisplayName("Test set image makes a non square image square for tall image")
    void testImageIsSquareForTall() {
        BufferedImage bi2 = new BufferedImage(32, 329, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        g2.setColor(Color.GREEN);
        g2.drawRect(8, 8, 16, 16);

        im.SetCellImage(bi2);

        assertTrue(im.GetCellImage().getWidth() == im.GetCellImage().getHeight());
    }

    @Test
    @DisplayName("Test set image makes a non square image square for wide image")
    void testImageIsSquareForWide() {
        BufferedImage bi2 = new BufferedImage(480, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        g2.setColor(Color.GREEN);
        g2.drawRect(8, 8, 16, 16);

        im.SetCellImage(bi2);

        assertTrue(im.GetCellImage().getWidth() == im.GetCellImage().getHeight());
    }

    @Test
    @DisplayName("Test set cell arrow image changes cell image with true is start")
    void testImageChangesForSetCellArrow() {
        im.SetCellArrow(Direction.WEST, true);

        assertTrue(!im.GetCellImage().equals(bi));
    }

    @Test
    @DisplayName("Test set cell arrow image changes cell image with false is start")
    void testImageChangesForSetCellArrowForFalseIsStart() {
        im.SetCellArrow(Direction.WEST, false);

        assertTrue(!im.GetCellImage().equals(bi));
    }

    @Test
    @DisplayName("Test cell image representation contains cell image")
    void testCellImageRepresentation() {
        BufferedImage outBi = im.getCellImageRepresentation(32, 32);
        float colourDifference = outBi.getRGB(16, 16) / Color.GREEN.getRGB();
        assertTrue(colourDifference < 1.025f && colourDifference > 0.975f);
    }
}
