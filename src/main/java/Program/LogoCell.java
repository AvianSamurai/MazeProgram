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

    /**
     * Gets the image contained in the logo cell
     *
     * @return the contained buffered image
     */
    public BufferedImage GetCellImage() {
        return (BufferedImage) cellImage;
    }

    /**
     * Replaces all logo cells in maze structure with Basic Cells
     *
     * @param m the maze structure to edit
     */
    public static void ClearLogosInMaze(MazeStructure m) {
        ArrayList<int[]> editedCells = new ArrayList<int[]>();

        // Loops through all cells in the maze
        for(int x = 0; x < m.getWidth(); x++) {
            for(int y = 0; y < m.getHeight(); y++) {
                // If the cell type is a logo cell, set it to a new basic cell
                I_Cell cell = m.GetCell(x, y);
                if(cell instanceof LogoCell) {
                    m.SetCell(new BasicCell(), x, y);
                    editedCells.add(new int[] {x, y});
                }
            }
        }

        // Removes all borders of the new cells
        for(int[] xy : editedCells) {
            for(Direction dir : m.GetDirectionsToValidCells(xy[0], xy[1], true)) {
                m.CarveInDirection(xy[0], xy[1], dir);
            }
        }
    }

    /**
     * When given the width in cells of a logo, this method will return the height of the logo in cells that is required
     * to maintain the correct aspect ratio
     *
     * @param cellsWide requested width of logo
     * @param logoImage The image to calculate for
     * @return height of logo in cells
     */
    public static int GetLogoCellHeightFromWidth(BufferedImage logoImage, int cellsWide) {
        int cellSize = (int) Math.ceil(logoImage.getWidth(null) / (double)cellsWide);
        int cellsTall = (int) Math.ceil(logoImage.getHeight(null) / (double)cellSize);
        return cellsTall;
    }

    /**
     * When given the height in cells of a logo, this method will return the width of the logo in cells that is required
     * to maintain the correct aspect ratio
     *
     * @param cellsTall requested height of logo
     * @param logoImage The image to calculate for
     * @return width of logo in cells
     */
    public static int GetLogoCellWidthFromHeight(BufferedImage logoImage, int cellsTall) {
        int cellSize = (int) Math.ceil(logoImage.getHeight(null) / (double)cellsTall);
        int cellsWide = (int) Math.ceil(logoImage.getWidth(null) / (double)cellSize);
        return cellsWide - 1;
    }

    /**
     * Creates a 2D array of LogoCells containing the logo to be place in the maze. <br/>
     * Null objects in the array represent cells in the maze that are not to be overridden with logo cells
     * if shape to logo shape is true, the returned array will contain null values where logo cells containing only transparent
     * pixels would have been generated.
     *
     * @param logoImage The logo image to use
     * @param cellsWide How many cells the logo is to take up in the x direction, the y height of the logo will be
     *                  calculated from this information
     * @param shapeToLogoShape if this is true, then the returned cell group will contain null values anywhere the logo is
     *                         transparent.
     * @return The 2d array of logo cells
     */
    public static LogoCell[][] CreateLogoCellGroup(BufferedImage logoImage, int cellsWide, boolean shapeToLogoShape) {
        // Get individual cell pixel size based on requested width of logo measured in cells
        int cellSize = (int) Math.ceil(logoImage.getWidth(null) / (double)cellsWide);
        int cellsTall = GetLogoCellHeightFromWidth(logoImage, cellsWide);

        // Create a set of logo cells to hold the new logo
        LogoCell[][] logoCellSet = new LogoCell[cellsWide][cellsTall];

        // Get a resized version of the logo with white borders added so that width or height mod the cell size is 0
        BufferedImage logo = new BufferedImage(cellsWide * cellSize, cellsTall * cellSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)logo.getGraphics();
        g.setColor(new Color(0f, 0f, 0f, 0f));
        g.fillRect(0, 0, logo.getWidth(), logo.getHeight());
        g.drawImage(logoImage, (cellSize*cellsWide - logoImage.getWidth())/2, (cellSize*cellsTall - logoImage.getHeight())/2, null);

        // Loop though each of the new cells in the logo cell array and assemble the cell image
        for(int x = 0; x < cellsWide; x++) {
            for(int y = 0; y < cellsTall; y++) {
                // Create a buffered image to hold a small portion of the logo. this is the portion that will exist in this cell
                BufferedImage subcell = logo.getSubimage(x*cellSize, y*cellSize, cellSize, cellSize);

                // If logo is to be a shaped logo, ignore transparency
                if(shapeToLogoShape) {
                    // Get pixels in a smaller version of the subcell
                    int[] pixels = new int[(int) Math.pow(cellSize, 2)];
                    PixelGrabber pixGrabber = new PixelGrabber(subcell.getScaledInstance(10, 10, Image.SCALE_FAST), 0, 0, cellSize, cellSize, pixels, 0, cellSize);
                    try {
                        pixGrabber.grabPixels();
                        logoCellSet[x][y] = null;
                        // If any of the pixels are not transparent, then create a new cell to hold that section of the logo
                        for (int pixel : pixels) {
                            Color color = new Color((pixel >> 16) & 0xff, (pixel >> 8) & 0xff, pixel & 0xff, (pixel >> 24) & 0xff);
                            if (color.getAlpha() > 50) {
                                logoCellSet[x][y] = new LogoCell(subcell);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Debug.LogLn("Pixel grab was interrupted unexpectedly, Recovering from problem...");
                        logoCellSet[x][y] = new LogoCell(subcell);
                    }
                } else {
                    // create a new section for each logo subsection, even if the subsection is fully transparent
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
        // Create a buffered image to hold the logo subcell
        BufferedImage logoCellImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) logoCellImage.getGraphics();

        // Sets the background colour to white
        g.setBackground(Color.white);
        g.fillRect(0, 0, width, height);

        // Insert the logo subcell image
        g.drawImage(GetCellImage().getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);
        DrawBorders(logoCellImage);
        return logoCellImage;
    }
}
