package MazeGUI;

import java.io.File;
import javax.swing.filechooser.FileFilter;

// Source: https://www.codejava.net/java-se/swing/add-file-filter-for-jfilechooser-dialog
public class FileTypeFilter extends FileFilter {
    private String extension;
    private String description;

    public FileTypeFilter(String extension, String description) {
        this.extension = extension;
        this.description = description;
    }

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        return file.getName().endsWith(extension);
    }

    public String getDescription() {
        return description + String.format(" (*%s)", extension);
    }

    public String getExtension() {
        return this.extension;
    }
}