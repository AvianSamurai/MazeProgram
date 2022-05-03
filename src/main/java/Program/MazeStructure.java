package Program;

import MazeGUI.MazeGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.nio.Buffer;

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
            throw new IndexOutOfBoundsException(String.format("Maze size is [{0}, {1}], Given index was [{2}, {3}]", width, height, x, y));
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
        if(cell.getCellType().equals("BasicCell")) {
            return (BasicCell) cell;
        } else {
            return null;
        }
    }



    public void DebugDisplayMaze() {
        BufferedImage bi = new BufferedImage(width * 32, height * 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setBackground(Color.white);
        g.fillRect(0, 0, width*32, height*32);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        ((BasicCell)cells[3][4]).CreateConnection((BasicCell)cells[4][4], Direction.EAST);
        ((BasicCell)cells[4][4]).CreateConnection((BasicCell)cells[4][5], Direction.SOUTH);
        ((BasicCell)cells[4][5]).CreateConnection((BasicCell)cells[4][6], Direction.SOUTH);
        ((BasicCell)cells[4][6]).CreateConnection((BasicCell)cells[5][6], Direction.EAST);
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                if(((BasicCell)cells[x][y]).GetBorders()[0]) {
                    g.drawLine(x*32, y*32, x*32 + 32, y*32);
                }
                if(((BasicCell)cells[x][y]).GetBorders()[1]) {
                    g.drawLine(x*32 + 32, y*32, x*32 + 32, y*32 + 32);
                }
                if(((BasicCell)cells[x][y]).GetBorders()[2]) {
                    g.drawLine(x*32, y*32 + 32, x*32 + 32, y*32 + 32);
                }
                if(((BasicCell)cells[x][y]).GetBorders()[3]) {
                    g.drawLine(x*32, y*32, x*32, y*32 + 32);
                }
            }
        }
        JDialog imgDialog = new JDialog();
        imgDialog.add(new JLabel(new ImageIcon(bi)));
        imgDialog.setVisible(true);
    }
}
