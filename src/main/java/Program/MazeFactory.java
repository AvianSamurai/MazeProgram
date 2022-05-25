package Program;

public class MazeFactory {
    /**
     * Creates a standard maze with no defined entry or exit
     *
     * @param width count of how many cells wide maze is to be
     * @param height count of how many cells tall maze is to be
     * @return a new MazeStructure object
     */
    public static MazeStructure CreateBasicMaze(int width, int height) {
        MazeStructure m = new MazeStructure(width, height);
        return m;
    }

    /**
     * Creates a themed maze with image cells at the top left and bottom right of the maze
     *
     * @param width count of how many cells wide maze is to be
     * @param height count of how many cells tall maze is to be
     * @return a new MazeStructure object
     */
    public static MazeStructure CreateThemedMaze(int width, int height) {
        MazeStructure m = new MazeStructure(width, height);
        m.SetCell(m.GetBasicCell(0 ,0).ConvertToImageCell(), 0, 0);
        m.SetCell(m.GetBasicCell(width - 1 ,height - 1).ConvertToImageCell(), width - 1, height - 1);
        return m;
    }

    /**
     * Creates a new empty maze
     *
     * @param width width of maze in cells
     * @param height height of maze in cells
     * @return the empty maze structure object
     */
    public static MazeStructure CreateEmptyMaze(int width, int height) {
        MazeStructure m = new MazeStructure(width, height);
        return m;
    }
}
