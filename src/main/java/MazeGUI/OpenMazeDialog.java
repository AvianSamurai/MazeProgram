package MazeGUI;

import DB.DBHelper;
import DB.JSelectionTable;
import DB.MazeDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static java.awt.Font.*;

public class OpenMazeDialog {
    // Parameters
    private static final int OUTER_MARGIN = 5;
    private static final Font TITLE_FONT = new Font("Arial", BOLD, 24);
    private static final Font TABLE_FONT = new Font("Arial", PLAIN, 16);
    private static final GridBagConstraints DEFAULT_GBC = new GridBagConstraints(0, 0, 1, 1,
            0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(5, 5, 5, 5), 5,5);
    private static final Dimension WINDOW_SIZE = new Dimension(900, 500);
    private static String[] ROW_NAMES = new String[] {"Maze Name", "Author", "Creation Date", "Last Edited"};

    // Table Model
    DefaultTableModel DTM = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    // Local variables
    private JTextField searchBar;
    private JFrame outerFrame;
    private JSelectionTable table = null;
    private GridBagConstraints gBC;
    private MazeDB db;

    public OpenMazeDialog() {
        // Create outer frame and set size
        outerFrame = new JFrame("Open Maze");
        outerFrame.setSize(WINDOW_SIZE);
        outerFrame.addWindowListener(windowListener);

        // Create default grid bag constraints
        gBC = (GridBagConstraints) DEFAULT_GBC.clone();;

        // Create outer panel and rig up the grid bag layout manager
        JPanel outerPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        outerPanel.setLayout(gbl);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        outerFrame.getContentPane().add(outerPanel);

        // Create title
        JLabel title = new JLabel("Open Maze from Database");
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

        searchBar = new JTextField(); // Text Field
        searchBar.requestFocus();
        searchBar.addActionListener(searchData);
        NextX(); gBC.weightx = 1;
        outerPanel.add(searchBar, gBC); RestoreGBCDefaults();

        JButton searchButton = new JButton("Search"); // Search Button
        searchButton.addActionListener(searchData);
        NextX();
        outerPanel.add(searchButton, gBC); RestoreGBCDefaults();

        // Horizontal Rule
        JSeparator hr2 = new JSeparator(SwingConstants.HORIZONTAL);
        gBC.weightx = 1;
        gBC.gridwidth = 3;
        NextY();
        outerPanel.add(hr2, gBC);

        // Create Table
        JScrollPane tableScrollPane = new JScrollPane(CreateTable());
        NextY();
        gBC.weighty = 1;
        outerPanel.add(tableScrollPane, gBC); RestoreGBCDefaults();

        // show window
        outerFrame.setVisible(true);
    }

    private JComponent CreateTable(){
        try {
            db = new MazeDB();
            String[][] data = DBHelper.GetMazeListBySearchString(db, "");
            if(data.length < 1) {
                JLabel dbEmptyLabel = new JLabel("Database is empty");
                dbEmptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
                dbEmptyLabel.setFont(TABLE_FONT);
                return  dbEmptyLabel;
            }
            table = new JSelectionTable(new String[]{"ID", "Maze Name", "Author", "Date Created", "Last Edited"});
            table.setFont(TABLE_FONT);
            table.setNewData(data);
        } catch (Exception e) {
            e.printStackTrace();
            return new JLabel("Database Failure: (" + e.getMessage() + ")");
        }
        return table;
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

    ActionListener searchData = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(table != null) {
                table.setNewData(DBHelper.GetMazeListBySearchString(db, searchBar.getText()));
            }
        }
    };

    WindowListener windowListener = new WindowListener() {
        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            db.disconnect();
        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    };
}