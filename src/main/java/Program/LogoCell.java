package Program;

import java.awt.image.BufferedImage;

public class LogoCell implements I_Cell{
    @Override
    public boolean isPartOfMaze() {
        return false;
    }

    @Override
    public BufferedImage getCellImageRepresentation(int width, int height) {
        return null;
    }
}
