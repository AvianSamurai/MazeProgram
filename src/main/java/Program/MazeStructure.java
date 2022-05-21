package Program;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.util.ArrayList;

public class MazeStructure {
    private int width, height;
    private I_Cell[][] cells;
    private int[][][] logoCellLocations;

    protected MazeStructure(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new I_Cell[width][height];
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
     * Creates a new set of logo cells and add them to the grid
     *
     * @param Logo Logo image to add to the grid
     */
    public void AddLogo(BufferedImage Logo) {

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
     * Sets the cell at the given position to the given cell
     *
     * @param newCell the cell to set at the given position
     * @param x cell x position
     * @param y cell y position
     */
    public void SetCell(I_Cell newCell, int x, int y) {
        cells[x][y] = newCell;
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
    public Direction[] GetDirectionsToValidCells(int x, int y, boolean allowCellsWithOtherConnections) {
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
        I_Cell iCell = GetCell(x, y);
        if(iCell instanceof BorderedCell) {
            BorderedCell currentCell = (BorderedCell) iCell;
            int[] offset = dir.GetOffset();
            BasicCell nextCell = GetBasicCell(x + offset[0], y + offset[1]);
            if (currentCell != null && nextCell != null) {
                currentCell.CreateConnection(nextCell, dir);
                return true;
            }
        }
        return false;
    }

    /**
     * Destroys a connection between the cell at [x, y] and the cell in the given direction
     *
     * @param x the x position to block from
     * @param y the y position to block from
     * @param dir the direction to block in
     * @return returns true if block was successful
     */
    public boolean BlockInDirection(int x, int y, Direction dir) {
        I_Cell iCell = GetCell(x, y);
        if(iCell instanceof BorderedCell) {
            BorderedCell currentCell = (BorderedCell) iCell;
            int[] offset = dir.GetOffset();
            BasicCell nextCell = GetBasicCell(x + offset[0], y + offset[1]);
            if (currentCell != null && nextCell != null) {
                currentCell.BlockConnection(nextCell, dir);
                return true;
            }
        }
        return false;
    }

    /**
     * returns the width of the maze in cells
     * @return amount of cells wide the maze is
     */
    public int getWidth() {
        return width;
    }

    /**
     * returns the height of the maze in cells
     * @return amount of cells tall the maze is
     */
    public int getHeight() {
        return height;
    }

    /**
     * Replaces the cell at the given co-ordinate with the new cell
     *
     * @param x the x coordinate of the new cell
     * @param y the y coordinate of the new cell
     * @param newCell the new cell
     */
    public void InsertCell(int x, int y, I_Cell newCell) {
        cells[x][y] = newCell;
    }

    /**
     * Replaces the area of cells beginning from the top left with the cells in the given 2d array
     * Any null values in the provided new cell array will not replace cells in the maze
     *
     * @param x the x coordinate of the top left cell
     * @param y the y coordinate of the top left cell
     * @param newCells the 2d array of cells to replace with
     * @param hasExteriorBorder if true, surrounds the inserted group with a border, otherwise, it does not
     */
    public void InsertCellGroup(int x, int y, I_Cell[][] newCells, boolean hasExteriorBorder) {
        for(int xPos = 0; xPos < newCells.length; xPos++) {
            for(int yPos = 0; yPos < newCells[0].length; yPos++) {
                if(newCells[xPos][yPos] != null) {
                    InsertCell(x + xPos, y + yPos, newCells[xPos][yPos]);
                }
            }
        }

        for(int xPos = 0; xPos < newCells.length; xPos++) {
            for(int yPos = 0; yPos < newCells[0].length; yPos++) {
                if(GetCell(x + xPos, y + yPos) instanceof LogoCell) {
                    for (Direction dir : GetDirectionsToValidCells(x + xPos, y + yPos, true)) {
                        if (!hasExteriorBorder) {
                            CarveInDirection(x + xPos, y + yPos, dir);
                        } else {
                            BlockInDirection(x + xPos, y + yPos, dir);
                        }
                    }
                }
            }
        }
    }

    /**
     * For debug only
     * Creates a pop-up window containing an image of the current maze.
     */
    public void DebugDisplayMaze(int size) {
        BufferedImage bi = new BufferedImage(width * size, height * size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setBackground(Color.white);
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                g.drawImage(GetCell(x, y).getCellImageRepresentation(size, size), x*size, y*size, size, size, null);
            }
        }
        JDialog imgDialog = new JDialog();
        imgDialog.add(new JLabel(new ImageIcon(bi)));
        imgDialog.setSize(new Dimension(width * size + 64, height * size + 64));
        imgDialog.setVisible(true);
    }

    /**
     * Resets all the basic cells to before random maze generation had occured
     */
    public void ResetBasicCells() {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                if(GetCell(x, y) instanceof BasicCell) {
                    InsertCell(x, y, new BasicCell());
                }
            }
        }
    }
}
