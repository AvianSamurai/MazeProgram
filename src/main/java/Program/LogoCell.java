package Program;

import Utils.Debug;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class LogoCell implements I_Cell{

    private Image cellImage;

    private LogoCell(Image cellImage) {
        this.cellImage = cellImage;
    }

    @Override
    public boolean isPartOfMaze() {
        return false;
    }

    @Override
    public BufferedImage getCellImageRepresentation(int width, int height) {
        return new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
    }
}
