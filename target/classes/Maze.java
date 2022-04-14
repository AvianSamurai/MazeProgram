package Program

// Import libraries here
import java.lang.math;

class Maze{
    // Global variables go here
    int width = 10;
    int height = 10;
    int[][] grid;

    // Directions
    int N = 1;
    int S = 2;
    int E = 4;
    int W = 8;

    // Start and End Coordinates
    int sx = 0;
    int sy = 0;
    int ex = 0;
    int ey = 0;

    // Maze Constructor
    public Maze(int width, int height, int sx, int sy, int ex, int ey) {
        this.width = width;
        this.height = height;

        // Generate grid and initialise it
        grid = new int[width][height];
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                grid[i][j] = 0;
            }
        }

        this.sx = sx;
        this.sy = sy;
        this.ex = ex;
        this.ey = ey;
    }

    // Generate a maze
    public void MazeGenerator(){
        
    }
}