package Program;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestLogoCell {
    LogoCell[][] logoCellGroup;
    BufferedImage bi;

    @BeforeEach
    void setup() {
        bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();

        g.setColor(Color.GREEN);
        g.drawRect(8, 8, 16, 16);

        logoCellGroup = LogoCell.CreateLogoCellGroup(bi, 16, false);
    }

    @Test
    @DisplayName("Test get image is not null")
    void testGetImageIsNotNull() {
        assertTrue(logoCellGroup[5][7].GetCellImage() != null);
    }

    @Test
    @DisplayName("Test logo cell group has correct dimensions")
    void testLogoCellgroupIsCorrectSize() {
        assertTrue(logoCellGroup.length == 16 && logoCellGroup[0].length == 16);
    }

    @Test
    @DisplayName("Test logo cell group has constant width for tall images")
    void testLogoCellGroupHasConstantWidthForTallImages() {
        BufferedImage bi2 = new BufferedImage(32, 329, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        g2.setColor(Color.GREEN);
        g2.drawRect(8, 8, 16, 16);

        logoCellGroup = LogoCell.CreateLogoCellGroup(bi2, 32, false);

        assertTrue(logoCellGroup.length == 32 && logoCellGroup[0].length != 32);
    }

    @Test
    @DisplayName("Test logo cell group has constant width for wide image")
    void testLogoCellGroupHasConstantWidthForWideImages() {
        BufferedImage bi2 = new BufferedImage(390, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        g2.setColor(Color.GREEN);
        g2.drawRect(8, 8, 16, 16);

        logoCellGroup = LogoCell.CreateLogoCellGroup(bi2, 32, false);

        assertTrue(logoCellGroup.length == 32 && logoCellGroup[0].length != 32);
    }

    @Test
    @DisplayName("Test all logo cell group sub array lengths are same size")
    void testAllLogoCellGroupsSameSize() {
        boolean isCorrectSize = true;
        for(LogoCell[] lArray : logoCellGroup) {
            if(lArray.length != 16) { isCorrectSize = false; }
        }

        assertTrue(isCorrectSize);
    }

    @Test
    @DisplayName("Test clear logos in maze clears logos")
    void testIClearLogosInMazeClearsLogoCells() {
        MazeStructure m = MazeFactory.CreateBasicMaze(20, 20);
        for(int x = 0; x < 20; x++) {
            for(int y = 0; y < 20; y++) {
                m.InsertCell(x, y, logoCellGroup[0][0]);
            }
        }

        LogoCell.ClearLogosInMaze(m);

        boolean allClear = true;
        for(int x = 0; x < 20; x++) {
            for(int y = 0; y < 20; y++) {
                if(m.GetCell(x, y) instanceof LogoCell) { allClear = false; }
            }
        }

        assertTrue(allClear);
    }

    @Test
    @DisplayName("Get height from width for square cell")
    void testCellGetHeightFromWidthForSquareCell() {
        BufferedImage bi2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        g2.setColor(Color.GREEN);
        g2.drawRect(8, 8, 16, 16);

        assertEquals(16, LogoCell.GetLogoCellHeightFromWidth(bi2, 16));
    }
    @Test
    @DisplayName("Get height from width for tall cell")
    void testCellGetHeightFromWidthForTallCell() {
        BufferedImage bi2 = new BufferedImage(32, 64, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        g2.setColor(Color.GREEN);
        g2.drawRect(8, 8, 16, 16);

        assertEquals(32, LogoCell.GetLogoCellHeightFromWidth(bi2, 16));
    }
    @Test
    @DisplayName("Get height from width for wide cell")
    void testCellGetHeightFromWidthForWideCell() {
        BufferedImage bi2 = new BufferedImage(64, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        g2.setColor(Color.GREEN);
        g2.drawRect(8, 8, 16, 16);

        assertEquals(8, LogoCell.GetLogoCellHeightFromWidth(bi2, 16));
    }
    @Test
    @DisplayName("Get width from height minus 1 for square cell")
    void testCellGetWidthFromHeightForSquareCell() {
        BufferedImage bi2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        g2.setColor(Color.GREEN);
        g2.drawRect(8, 8, 16, 16);

        assertEquals(16, LogoCell.GetLogoCellWidthFromHeight(bi2, 16));
    }
    @Test
    @DisplayName("Get width from height minus 1 for tall cell")
    void testCellGetWidthFromHeightForTallCell() {
        BufferedImage bi2 = new BufferedImage(8, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        g2.setColor(Color.GREEN);
        g2.drawRect(8, 8, 16, 16);

        assertEquals(4, LogoCell.GetLogoCellWidthFromHeight(bi2, 16));
    }
    @Test
    @DisplayName("Get width from height minus 1 for wide cell")
    void testCellGetWidthFromHeightForWideCell() {
        BufferedImage bi2 = new BufferedImage(64, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        g2.setColor(Color.GREEN);
        g2.drawRect(8, 8, 16, 16);

        assertEquals(32, LogoCell.GetLogoCellWidthFromHeight(bi2, 16));
    }

    @Test
    @DisplayName("Test logo cell is not part of maze")
    void testIsNotPartOfMaze() {
        assertTrue(!logoCellGroup[0][0].isPartOfMaze());
    }

    @Test
    @DisplayName("Each logo cell returns correct part of image")
    void testLogoCellReturnsCorrectPartOfImage() {
        boolean isSameColour = true;
        int size = logoCellGroup[0][0].GetCellImage().getWidth();
        for(int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                if(logoCellGroup[x][y].GetCellImage().getRGB(size/2, size/2) !=
                logoCellGroup[x][y].getCellImageRepresentation(size, size).getRGB(size/2, size/2)) {
                    isSameColour = false;
                }
            }
            assertTrue(isSameColour);
        }
    }
}
