package Program;

import MazeGUI.MazeGUI;
import Utils.Debug;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Random;

public class MazeStructure {
    private int width, height;
    private I_Cell[][] cells;

    protected MazeStructure(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new BasicCell[width][height];
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                cells[x][y] = new BasicCell();
            }
        }

    }

    /**
     * Get the I_Cell at the given position in the maze
     *
     * @param x cell x position
     * @param y cell y position
     * @return reference to the I_Cell at that position
     */
    public I_Cell GetCell(int x, int y) {
        if(x > width - 1 || x < 0 || y > height - 1 || y < 0) {
            throw new IndexOutOfBoundsException(String.format("Maze size is [%d, %d], Given index was [%d, %d]", width, height, x, y));
        }
        return cells[x][y];
    }

    /**
     * Returns the basic cell at the requested position, or null if the cell is not of type BasicCell
     *
     * @param x cell x position
     * @param y cell y position
     * @return the basic cell
     */
    public BasicCell GetBasicCell(int x, int y) {
        I_Cell cell = GetCell(x, y);
        if(cell instanceof BasicCell) {
            return (BasicCell) cell;
        } else {
            return null;
        }
    }

    /**
     * Get an array of cell neighbors of a cell at [x, y]
     * Array will contain cells represented in a clockwise ordering starting with index 0 = north
     * if a neighbor cell is not a BasicCell or does not exist, the array will contain null at that position instead
     *
     * @param x the x position to search around
     * @param y the y position to search around
     * @return an array of neighboring cells in clockwise ordering
     */
    public BasicCell[] GetBasicCellNeighbors(int x, int y) {
        BasicCell[] cellArray = new BasicCell[4];
        if(y > 0) {
            cellArray[0] = GetBasicCell(x, y - 1);
        } else {
            cellArray[0] = null;
        }
        if(x < width - 1) {
            cellArray[1] = GetBasicCell(x + 1, y);
        } else {
            cellArray[1] = null;
        }
        if(y < height - 1) {
            cellArray[2] = GetBasicCell(x, y + 1);
        } else {
            cellArray[2] = null;
        }
        if(x > 0) {
            cellArray[3] = GetBasicCell(x - 1, y);
        } else {
            cellArray[3] = null;
        }
        return cellArray;
    }

    public void DebugDisplayMaze() {
        BufferedImage bi = new BufferedImage(width * 32, height * 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setBackground(Color.white);
        //g.fillRect(0, 0, width*32, height*32);
        CarveRandomly();
        CarveRandomly();
        CarveRandomly();
        CarveRandomly();
        CarveRandomly();
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                g.drawImage(GetBasicCell(x, y).getCellImageRepresentation(32, 32), x*32, y*32, 32, 32, null);
            }
        }
        JDialog imgDialog = new JDialog();
        imgDialog.add(new JLabel(new ImageIcon(bi)));
        imgDialog.setSize(new Dimension((width+4) * 32, (height+4) * 32));
        imgDialog.setVisible(true);
    }

    private void CarveRandomly() {
        Random r = new Random(System.currentTimeMillis());
        int currentX = r.nextInt(width);
        int currentY = r.nextInt(height);
        int dir = r.nextInt(4);
        BasicCell nextCell;
        int limit = 100;
        while((nextCell = GetBasicCellNeighbors(currentX, currentY)[dir]) != null && limit > 0) {
            if(!nextCell.hasAnyConnections()) {
                GetBasicCell(currentX, currentY).CreateConnection(nextCell, Direction.IntToDirection(dir));
                currentX += Direction.IntToDirection(dir).GetOffset()[0];
                currentY += Direction.IntToDirection(dir).GetOffset()[1];
            }
            dir = r.nextInt(4);
            limit--;
        }
    }
}
