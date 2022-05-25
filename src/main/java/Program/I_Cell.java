package Program;

import java.awt.image.BufferedImage;

public interface I_Cell {

    /**
     * Returns if this cell is a traversable part of the maze.
     * For Example, a logo cell would not be a traversable part of the maze and would return false.
     *
     * @return true if this cell type is a traversable cell
     */
    public boolean isPartOfMaze();

    /**
     * Returns a buffered image of the given width and height representing what the cell looks like in
     * the final rendered maze image
     *
     * @param width the width of the buffered image
     * @param height the height of the buffered image
     * @return the cell image
     */
    public BufferedImage getCellImageRepresentation(int width, int height);
}
