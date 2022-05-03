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
}
