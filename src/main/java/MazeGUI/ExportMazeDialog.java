package MazeGUI;

import javax.swing.*;
import java.awt.*;

import static java.awt.Font.BOLD;

public class ExportMazeDialog {
    private static final int OUTER_MARGIN = 5;
    private static final Font TITLE_FONT = new Font("Arial", BOLD, 24);
    private static final GridBagConstraints DEFAULT_GBC = new GridBagConstraints(0, 0, 1, 1,
            0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(5, 5, 5, 5), 5,5);

    private JFrame outerFrame;
    private GridBagConstraints gBC;

    public ExportMazeDialog() {
        // Create outer frame and set size
        outerFrame = new JFrame("Export Maze");
        outerFrame.setSize(600,400);

        // Create default grid bag constraints
        gBC = (GridBagConstraints) DEFAULT_GBC.clone();;

        // Create outer panel and rig up the grid bag layout manager
        JPanel outerPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        outerPanel.setLayout(gbl);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        outerFrame.getContentPane().add(outerPanel);

        // Create title
        JLabel title = new JLabel("Export Maze to Image");
        title.setFont(TITLE_FONT);
        gBC.gridwidth = 3;
        gBC.weightx = 1;
        outerPanel.add(title, gBC); RestoreGBCDefaults();

        // Horizontal Rule
        JSeparator hr = new JSeparator(SwingConstants.HORIZONTAL);
        gBC.weightx = 1;
        gBC.gridwidth = 3;
        NextY();
        outerPanel.add(hr, gBC); RestoreGBCDefaults();

        // Create Searchbar
        JLabel searchLabel = new JLabel("Search: "); // Label
        NextY();
        outerPanel.add(searchLabel, gBC);

        JTextField searchBar = new JTextField(); // Text Field
        NextX(); gBC.weightx = 1;
        outerPanel.add(searchBar, gBC); RestoreGBCDefaults();

        JButton searchButton = new JButton("Search"); // Search Button
        NextX();
        outerPanel.add(searchButton, gBC); RestoreGBCDefaults();

        // Horizontal Rule
        JSeparator hr2 = new JSeparator(SwingConstants.HORIZONTAL);
        gBC.weightx = 1;
        gBC.gridwidth = 3;
        NextY();
        outerPanel.add(hr2, gBC);

        // Create Table
        JPanel temp = new JPanel();
        NextY();
        gBC.weighty = 1;
        outerPanel.add(temp, gBC); RestoreGBCDefaults();

        // show window
        outerFrame.setVisible(true);
    }

    private void RestoreGBCDefaults() {
        int x = gBC.gridx;
        int y = gBC.gridy;
        gBC = (GridBagConstraints) DEFAULT_GBC.clone();
        gBC.gridx = x;
        gBC.gridy = y;
    }

    private void NextX() {
        gBC.gridx++;
    }

    private void NextY() {
        gBC.gridx = 0;
        gBC.gridy++;
    }
}
