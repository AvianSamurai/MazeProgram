package Program;

import Utils.Debug;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;

public class LogoCell extends BorderedCell implements I_Cell {

    private Image cellImage;


    private LogoCell(Image cellImage) {
        this.cellImage = cellImage;
    }

    public BufferedImage GetCellImage() {
        return (BufferedImage) cellImage;
    }

    public static void ClearLogosInMaze(MazeStructure m) {
        ArrayList<int[]> editedCells = new ArrayList<int[]>();

        for(int x = 0; x < m.getWidth(); x++) {
            for(int y = 0; y < m.getHeight(); y++) {
                I_Cell cell = m.GetCell(x, y);
                if(cell instanceof LogoCell) {
                    m.SetCell(new BasicCell(), x, y);
                    editedCells.add(new int[] {x, y});
                }
            }
        }

        for(int[] xy : editedCells) {
            for(Direction dir : m.GetDirectionsToValidCells(xy[0], xy[1], true)) {
                m.CarveInDirection(xy[0], xy[1], dir);
            }
        }
    }

    /**
     * Creates a 2D array of LogoCells containing the logo to be place in the maze. <br/>
     * Null objects in the array represent cells in the maze that are not to be overridden with logo cells
     *
     * @param logoImage The logo image to use
     * @param cellsWide How many cells the logo is to take up in the x direction, the y height of the logo will be
     *                  calculated from this information
     * @return The 2d array of logo cells
     */
    public static LogoCell[][] CreateLogoCellGroup(BufferedImage logoImage, int cellsWide) {

        int cellSize = (int) Math.ceil(logoImage.getWidth(null) / (double)cellsWide);
        int cellsTall = (int) Math.ceil(logoImage.getHeight(null) / (double)cellSize);
        LogoCell[][] logoCellSet = new LogoCell[cellsWide][cellsTall];
        BufferedImage logo = new BufferedImage(cellsWide * cellSize, cellsTall * cellSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)logo.getGraphics();
        g.setColor(new Color(0f, 0f, 0f, 0f));
        g.fillRect(0, 0, logo.getWidth(), logo.getHeight());
        g.drawImage(logoImage, (cellSize*cellsWide - logoImage.getWidth())/2, (cellSize*cellsTall - logoImage.getHeight())/2, null);

        for(int x = 0; x < cellsWide; x++) {
            for(int y = 0; y < cellsTall; y++) {
                BufferedImage subcell = logo.getSubimage(x*cellSize, y*cellSize, cellSize, cellSize);
                int[] pixels = new int[(int) Math.pow(cellSize, 2)];
                PixelGrabber pixGrabber = new PixelGrabber(subcell.getScaledInstance(10, 10, Image.SCALE_FAST), 0, 0, cellSize, cellSize, pixels, 0 ,cellSize);
                try {
                    pixGrabber.grabPixels();
                    logoCellSet[x][y] = null;
                    for (int pixel : pixels) {
                        Color color = new Color((pixel >> 16) & 0xff, (pixel >> 8) & 0xff, pixel & 0xff, (pixel >> 24) & 0xff);
                        if (color.getAlpha() > 50) {
                            logoCellSet[x][y] = new LogoCell(subcell);
                            break;
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    Debug.LogLn("Pixel grab was interrupted unexpectedly, Recovering from problem...");
                    logoCellSet[x][y] = new LogoCell(subcell);
                }
            }
        }
        return logoCellSet;
    }

    @Override
    public boolean isPartOfMaze() {
        return false;
    }

    @Override
    public BufferedImage getCellImageRepresentation(int width, int height) {
        BufferedImage logoCellImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        DrawBorders(logoCellImage);
        return logoCellImage;
    }
}
