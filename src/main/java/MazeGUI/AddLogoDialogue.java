package MazeGUI;

import Program.I_Cell;
import Program.LogoCell;
import Program.MazeStructure;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class AddLogoDialogue extends JDialog {

    private static BufferedImage logo;
    private static MazeEditor mazePanel;
    private JSpinner spinner;

    private AddLogoDialogue(Frame owner, MazeStructure m) {
        super(owner, "Logo Cell Size");

        spinner = new JSpinner();

        // Work out the max logo size
        int maxHeight = LogoCell.GetLogoCellHeightFromWidth(logo, m.getWidth());
        int maxWidth = m.getWidth();
        if(maxHeight > m.getHeight()) {
            maxHeight = m.getHeight();
            maxWidth = LogoCell.GetLogoCellWidthFromHeight(logo, m.getHeight());
        }

        // Build the spinner model
        SpinnerNumberModel model = new SpinnerNumberModel();
        model.setMinimum(1);
        model.setMaximum(maxWidth);
        model.setStepSize(1);
        model.setValue(1);
        spinner.setModel(model);

        // Build the layout manager
        FlowLayout layout = new FlowLayout();
        this.setLayout(layout);

        // Build the button
        JButton btn = new JButton("Add Logo");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetAndClose();
            }
        });

        // Add the components
        this.add(new JLabel("Logo width in cells: "));
        this.add(spinner);
        this.add(btn);
        btn.setSelected(true);

        // Show it
        this.setMinimumSize(new Dimension(300, 75));
        super.setLocationRelativeTo(getParent());
        this.setVisible(true);
        this.repaint();
    }

    protected void SetAndClose() {
        mazePanel.SelectTool(ToolsEnum.PLACE_LOGO);
        mazePanel.SetLogoToPlace(logo, (int)spinner.getValue());
        this.setVisible(false);
        this.dispose();
    }

    public static void OpenAddLogoDialogue(Frame owner, MazeEditor mPanel, String pathToImage) {
        // No point even constructing this if the path is bad
        File logoFile = new File(pathToImage);
        if(!logoFile.exists()) {
            JOptionPane.showMessageDialog(owner, "File does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            logo = ImageIO.read(logoFile);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(owner, "File is not a supported image file or is corrupted", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        mazePanel = mPanel;
        new AddLogoDialogue(owner, mazePanel.GetMazeStructure());
    }

    private void CreateErrorMessage() {

    }
}
