package Program;

import MazeGUI.MazeEditor;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MazeAlgorithms {

    /**
     * Uses Hunt-and-Kill algorithm to generate a maze
     *
     * @param m MazeStructure variable to modify
     */
    public static void GenerateMaze(MazeStructure m) {
        CarveRandomly(m, -1, -1);
        int[] nextStartingPos = GetNextCellToCarveFromAndConnectIt(m);
        while(nextStartingPos != null) {
            CarveRandomly(m, nextStartingPos[0], nextStartingPos[1]);
            nextStartingPos = GetNextCellToCarveFromAndConnectIt(m);

            // Search for any remaining un-carved cells
            if(nextStartingPos == null) {
                nextStartingPos = SearchForRemainingCells(m);
            }
        }

    }

    private static int[] SearchForRemainingCells(MazeStructure m) {
        for(int x = 0; x < m.getWidth(); x++) {
            for(int y = 0; y < m.getHeight(); y++) {
                if(m.GetBasicCell(x, y) != null && !m.GetBasicCell(x, y).hasAnyConnections()) {
                    BasicCell[] cellNeighbors = m.GetBasicCellNeighbors(x, y);
                    for(int i = 0; i < 4; i++) {
                        if(cellNeighbors[i] != null) {
                            return new int[] {x, y};
                        }
                    }
                }
            }
        }
        return null;
    }

    public static int[][] GenerateSolution(MazeStructure m, int startX, int startY, int endX, int endY) {
        ArrayList<int[]> solutionPositions = new ArrayList<int[]>();

        int[][] mazeWeights = MaxIntValueArray(m.getWidth(), m.getHeight());

        Stack<int[]> stack = new Stack<int[]>();
        stack.push(new int[] {startX, startY});
        mazeWeights[startX][startY] = 0;

        while(!stack.empty()) {
            int[] pos = stack.pop();

            BasicCell currentCell = m.GetBasicCell(pos[0], pos[1]);
            if(currentCell == null) { break; }

            Direction[] dirs = m.GetDirectionsToValidCells(pos[0], pos[1], true);
            for(Direction dir : dirs) {

                int[] offset = dir.GetOffset();
                int newX = pos[0] + offset[0];
                int newY = pos[1] + offset[1];

                if (mazeWeights[newX][newY] == Integer.MAX_VALUE && !currentCell.GetBorders()[dir.GetIntValue()]) {

                    BasicCell cell = m.GetBasicCell(newX, newY);

                    if (cell != null) {
                        mazeWeights[newX][newY] = mazeWeights[pos[0]][pos[1]] + 1;
                        stack.push(new int[]{newX, newY});
                    }
                }
            }
        }
        if(mazeWeights[endX][endY] == Integer.MAX_VALUE) {
            return null;
        }
        int currentX = endX;
        int currentY = endY;
        int minWeight = mazeWeights[endX][endY];
        while(true) {

            Direction[] dirs = m.GetDirectionsToValidCells(currentX, currentY, true);
            Direction dirToMove = null;
            BasicCell currentCell = m.GetBasicCell(currentX, currentY);
            for(Direction dir : dirs) {
                if(currentCell != null && !currentCell.GetBorders()[dir.GetIntValue()]) {
                    int[] offset = dir.GetOffset();
                    if (mazeWeights[currentX + offset[0]][currentY + offset[1]] < minWeight) {
                        minWeight = mazeWeights[currentX][currentY];
                        dirToMove = dir;
                    }
                }
            }
            currentX += dirToMove.GetOffset()[0];
            currentY += dirToMove.GetOffset()[1];
            solutionPositions.add(new int[]{currentX, currentY});

            if(currentX == startX && currentY == startY) {
                break;
            }
        }

        return solutionPositions.toArray(int[][]::new);
    }

    static int[][] MaxIntValueArray(int xsize, int ysize) {
        int[][] finalArray = new int[xsize][ysize];
        for(int x = 0; x < xsize; x++) {
            for(int y = 0; y < ysize; y++) {
                finalArray[x][y] = Integer.MAX_VALUE;
            }
        }
        return finalArray;
    }

    private static int[] GetNextCellToCarveFromAndConnectIt(MazeStructure m) {
        for(int x = 0; x < m.getWidth(); x++) {
            for(int y = 0; y < m.getHeight(); y++) {

                if(m.GetBasicCell(x, y) != null && !m.GetBasicCell(x, y).hasAnyConnections()) {
                    BasicCell[] cellNeighbors = m.GetBasicCellNeighbors(x, y);
                    for(int i = 0; i < 4; i++) {
                        if(cellNeighbors[i] != null && cellNeighbors[i].hasAnyConnections()) {
                            m.GetBasicCell(x, y).CreateConnection(cellNeighbors[i], Direction.IntToDirection(i));
                            return new int[] {x, y};
                        }
                    }
                }
            }
        }
        return null;
    }

    private static void CarveRandomly(MazeStructure mazeStruct, int x, int y) {
        // Create a random number generator and select a random starting point
        Random r = new Random(System.nanoTime());
        int currentX = x;
        int currentY = y;
        if(x < 0 || y < 0) {
            currentX = r.nextInt(mazeStruct.getWidth());
            currentY = r.nextInt(mazeStruct.getHeight());
        }

        // Get all the valid directions around the starting point
        Direction[] carveDir = mazeStruct.GetDirectionsToValidCells(currentX, currentY, false);
        int limit = 1000; // This is here to avoid infinite loops

        while(carveDir.length > 0 && limit > 0) { // While the current cell has at least 1 direction we can carve in
            // Choose a random direction to carve in from the list of valid directions
            Direction nextDir = carveDir[r.nextInt(carveDir.length)];

            // Carve in that direction
            mazeStruct.CarveInDirection(currentX, currentY, nextDir);

            // Move the current position in the direction we carved in
            currentX += nextDir.GetOffset()[0];
            currentY += nextDir.GetOffset()[1];

            // Get valid directions to carve in around new position
            carveDir = mazeStruct.GetDirectionsToValidCells(currentX, currentY, false);

            limit--;
        }
    }

    
}
