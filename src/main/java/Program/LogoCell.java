package Program;

import Utils.Debug;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.nio.Buffer;

public class LogoCell implements I_Cell{

    private Image cellImage;

    private LogoCell(Image cellImage) {
        this.cellImage = cellImage;
    }

    public BufferedImage GetCellImage() {
        return (BufferedImage) cellImage;
    }

    public static void InsertLogoIntoMaze(MazeStructure m, BufferedImage logoImage, int cellsWide, int xPos, int yPos) throws InterruptedException {
        int cellSize = (int) Math.ceil(logoImage.getWidth(null) / (double)cellsWide);
        int cellsTall = (int) Math.ceil(logoImage.getHeight(null) / (double)cellSize);
        LogoCell[][] logoCellSet = new LogoCell[cellsWide][cellsTall];
        BufferedImage logo = new BufferedImage(cellsWide * cellSize, cellsTall * cellSize, BufferedImage.TYPE_INT_ARGB);
        ((Graphics2D)logo.getGraphics()).drawImage(logoImage, (cellSize*cellsWide - logoImage.getWidth())/2, (cellSize*cellsTall - logoImage.getHeight())/2, null);

        for(int x = 0; x < cellsWide; x++) {
            for(int y = 0; y < cellsTall; y++) {
                BufferedImage subcell = logo.getSubimage(x*cellSize, y*cellSize, cellSize, cellSize);
                int[] pixels = new int[(int) Math.pow(cellSize, 2)];
                PixelGrabber pixGrabber = new PixelGrabber(subcell.getScaledInstance(cellSize/4, cellSize/4, Image.SCALE_FAST), 0, 0, cellSize, cellSize, pixels, 0 ,cellSize);
                pixGrabber.grabPixels();
                logoCellSet[x][y] = null;
                for(int pixel : pixels) {
                    Color color = new Color(pixel);
                    if(color != Color.BLACK) {
                        logoCellSet[x][y] = new LogoCell(subcell);
                        break;
                    }
                }
            }
        }

        m.InsertCellGroup(xPos, yPos, logoCellSet);
    }

    @Override
    public boolean isPartOfMaze() {
        return false;
    }

    @Override
    public BufferedImage getCellImageRepresentation(int width, int height) {
        return new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
    }
}
