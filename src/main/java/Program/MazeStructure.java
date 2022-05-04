package Program;

import MazeGUI.MazeGUI;
import Utils.Debug;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ComponentInputMapUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
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

    /**
     * Gets an array of directions around a cell to other cells that are part of the maze.
     * For Example basic cells are part of the maze where-as logo cells or cells outside the bounds of the maze are not.
     *
     * if allowCellsWithOtherConnections is false, then the array will not include directions to cells
     * that are already connected to other cells.
     *
     * @param x the x position of the cell to search around
     * @param y the y position of the cell to search around
     * @param allowCellsWithOtherConnections whether to include cells with other connections in the final array or not
     * @return an array of carveable directions
     */
    public Direction[] GetDirectionsToUncarvedCells(int x, int y, boolean allowCellsWithOtherConnections) {
        BasicCell[] cellNeighbors = GetBasicCellNeighbors(x, y);
        ArrayList<Direction> acceptableDirections = new ArrayList<Direction>();
        for(int i = 0; i < 4; i++) {
            if(cellNeighbors[i] != null && (allowCellsWithOtherConnections || !cellNeighbors[i].hasAnyConnections())) {
                acceptableDirections.add(Direction.IntToDirection(i));
            }
        }
        Direction[] finalDirections = new Direction[acceptableDirections.size()];
        return acceptableDirections.toArray(finalDirections);
    }

    /**
     * Carves a connection between the cell at [x, y] and the cell in the given direction
     *
     * @param x the x position to carve from
     * @param y the y position to carve from
     * @param dir the direction to carve in
     * @return returns true if carve was successful
     */
    public boolean CarveInDirection(int x, int y, Direction dir) {
        BasicCell currentCell = GetBasicCell(x, y);
        int[] offset = dir.GetOffset();
        BasicCell nextCell = GetBasicCell(x + offset[0], y + offset[1]);
        if(currentCell != null && nextCell != null) {
            currentCell.CreateConnection(nextCell, dir);
            return true;
        }
        return false;
    }

    /**
     * For debug only
     * Creates a pop-up window containing an image of the current maze.
     */
    public void DebugDisplayMaze() {
        BufferedImage bi = new BufferedImage(width * 32, height * 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setBackground(Color.white);
        //g.fillRect(0, 0, width*32, height*32);
        for(int i = 0; i < 20; i++) {
            CarveRandomly();
        }
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
        Random r = new Random(System.nanoTime());
        int currentX = r.nextInt(width);
        int currentY = r.nextInt(height);
        Direction[] carveDir = GetDirectionsToUncarvedCells(currentX, currentY, false);
        int limit = 100;
        while(carveDir.length > 0 && limit > 0) {
            Direction nextDir = carveDir[r.nextInt(carveDir.length)];
            CarveInDirection(currentX, currentY, nextDir);
            currentX += nextDir.GetOffset()[0];
            currentY += nextDir.GetOffset()[1];
            carveDir = GetDirectionsToUncarvedCells(currentX, currentY, false);
            limit--;
        }
    }
}
