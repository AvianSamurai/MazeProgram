package Program;

public interface I_Cell {
    /**
     * Returns if this cell is a traversable part of the maze.
     * For Example, a logo cell would not be a traversable part of the maze and would return false.
     *
     * @return true if this cell type is a traversable cell
     */
    public boolean isPartOfMaze();

    public String getCellType();
}
