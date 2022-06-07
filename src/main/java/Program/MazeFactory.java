package Program;

import Utils.Debug;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
     * Prompts the user to select an image and automatically places it in the center of a new maze object.
     * The logo's largest dimension will be 1/3 of the size of the maze's relevant dimension
     *
     * @param width the width in cells of the maze
     * @param height the height in cells of the maze
     * @return the new maze object
     */
    public static MazeStructure CreateLogoMaze(int width, int height) {
        MazeStructure m = new MazeStructure(width, height);

        JFileChooser fileChooser = new JFileChooser();
        FileFilter imageFilter = new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(imageFilter);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) { return m; }

        File selectedFile = fileChooser.getSelectedFile();
        System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        BufferedImage logo;
        try {
            logo = ImageIO.read(selectedFile);
        } catch (IOException e) {
            Debug.LogLn("Failed to open logo when creating new maze");
            e.printStackTrace();
            return m;
        }

        int maxWidth = width/3;
        if(LogoCell.GetLogoCellHeightFromWidth(logo, maxWidth) > maxWidth) {
            maxWidth = LogoCell.GetLogoCellWidthFromHeight(logo, maxWidth);
        }
        LogoCell[][] logocells = LogoCell.CreateLogoCellGroup(logo, maxWidth, false);
        int newHeight = LogoCell.GetLogoCellHeightFromWidth(logo, maxWidth);

        m.InsertLogoCellGroup((width - maxWidth)/2, (height - newHeight)/2, logocells, true);

        return m;
    }
}
