package Program;

import java.util.Random;

public class MazeAlgorithms {

    /**
     * Uses Hunt-and-Kill algorithm to generate a maze
     *
     * @param m MazeStructure variable to modify
     */
    public static void GenerateMaze(MazeStructure m) {
        CarveRandomly(m, -1, -1);
        int[] nextStartingPos;
        while((nextStartingPos = GetNextCellToCarveFromAndConnectIt(m)) != null) {
            CarveRandomly(m, nextStartingPos[0], nextStartingPos[1]);
        }
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
        int limit = 100; // This is here to avoid infinite loops

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
