package Program;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestMazeAlgorithms {
    MazeStructure m;

    @BeforeEach
    void Setup() {
        m = MazeFactory.CreateBasicMaze(5, 5);
    }

    @Test
    @DisplayName("Test null maze structure object")
    void testNullMazeStructure() {
        assertThrows(NullPointerException.class, () -> Program.MazeAlgorithms.GenerateMaze(null));
    }

    @Test
    @DisplayName("Text extremely large maze structre")
    void testExtremelyLargeMazeStructure() {
        assertThrows(OutOfMemoryError.class, () -> Program.MazeAlgorithms.GenerateMaze(new MazeStructure(Integer.MAX_VALUE, Integer.MAX_VALUE)));
    }

    @Test
    @DisplayName("Test that maze generation has connected all cells")
    void testAllCellsHaveAConnection() {
        MazeAlgorithms.GenerateMaze(m);
        boolean anyCellsNotConnected = false;
        for(int x = 0; x < 5; x++) {
            for(int y = 0; y < 5; y++) {
                BasicCell b = m.GetBasicCell(x, y);
                if(b != null) {
                    if(!b.hasAnyConnections()) {
                        anyCellsNotConnected = true;
                    }
                }
            }
        }
        assertFalse(anyCellsNotConnected);
    }
}
