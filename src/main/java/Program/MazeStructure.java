package Program;

public class MazeStructure {
    private int width, height;
    private I_Cell[][] cells;

    protected MazeStructure(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new BasicCell[width][height];
    }
}
