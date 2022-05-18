package Program;

import Program.MazeFactory;
import Program.MazeStructure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MazeAlgorithmsTest {
    MazeStructure m;

    @BeforeEach
    void Setup() {
        m = MazeFactory.CreateBasicMaze(5, 5);
    }

    @Test
    void testNullMazeStructure() {
        assertThrows(NullPointerException.class, () -> Program.MazeAlgorithms.GenerateMaze(null));
    }

    @Test
    void testExtremelyLargeMazeStructure() {
        assertThrows(OutOfMemoryError.class, () -> Program.MazeAlgorithms.GenerateMaze(new MazeStructure(Integer.MAX_VALUE, Integer.MAX_VALUE)));
    }

    @Test
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
