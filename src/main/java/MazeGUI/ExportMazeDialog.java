package MazeGUI;

import DB.MazeDB;
import Utils.Debug;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static java.awt.Font.*;
import static java.awt.GridBagConstraints.NONE;

public class ExportMazeDialog {
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

    private static boolean isOpen = false;

    public static void Open() {
        if(!isOpen && !OpenMazeDialog.GetIsOpen()) {
            new ExportMazeDialog();
            isOpen = true;
        }
    }

    public static boolean GetIsOpen() {return isOpen; }

    private ExportMazeDialog() {
        // Create outer frame and set size
        outerFrame = new JFrame("Export Maze to Image");
        outerFrame.setSize(WINDOW_SIZE);
        outerFrame.addWindowListener(windowListener);

        // Set icon
        outerFrame.setIconImage(new ImageIcon(this.getClass().getResource("MazeCo.png")).getImage());

        // Create default grid bag constraints
        gBC = (GridBagConstraints) DEFAULT_GBC.clone();;

        // Create outer panel and rig up the grid bag layout manager
        JPanel outerPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        outerPanel.setLayout(gbl);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        outerFrame.getContentPane().add(outerPanel);

        // Create title
        JLabel title = new JLabel("Export maze to image");
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

        // Buttons cancel and export
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(cancelListener);
        NextY(); NextX();
        gBC.fill = GridBagConstraints.NONE;
        gBC.anchor = GridBagConstraints.EAST;
        outerPanel.add(cancelBtn, gBC); RestoreGBCDefaults();

        JButton openBtn = new JButton("Export");
        openBtn.addActionListener(openListener);
        NextX();
        outerPanel.add(openBtn, gBC); RestoreGBCDefaults();

        // show window
        outerFrame.setVisible(true);
        outerFrame.setAlwaysOnTop(true);
    }

    private JComponent CreateTable(){
        try {
            db = new MazeDB();
            String[][] data = db.GetMazeListBySearchString("");
            if(data.length < 1) {
                JLabel dbEmptyLabel = new JLabel("Database is empty");
                dbEmptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
                dbEmptyLabel.setFont(TABLE_FONT);
                return  dbEmptyLabel;
            }
            table = new JSelectionTable(new String[]{"ID", "Maze Name", "Author", "Date Created", "Last Edited"});
            table.setFont(TABLE_FONT);
            table.SetColumnWidth(0, 25);
            table.SetColumnWidth(1, 300);
            table.setNewData(data);
            table.setAutoCreateRowSorter(true);
            table.getTableHeader().setReorderingAllowed(false);
            table.setCellSelectionEnabled(false);
            table.setRowSelectionAllowed(true);
        } catch (Exception e) {
            e.printStackTrace();
            Debug.LogLn("Cannot connect to database: " + e.getMessage());
            JLabel dbEmptyLabel = new JLabel("Cannot connect to database");
            dbEmptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            dbEmptyLabel.setFont(TABLE_FONT);
            return  dbEmptyLabel;
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
                table.setNewData(db.GetMazeListBySearchString(searchBar.getText()));
            }
        }
    };

    ActionListener cancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            outerFrame.dispatchEvent(new WindowEvent(outerFrame, WindowEvent.WINDOW_CLOSING));
        }
    };

    ActionListener openListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) { // TODO
            outerFrame.dispatchEvent(new WindowEvent(outerFrame, WindowEvent.WINDOW_CLOSING)); // Temperary
        }
    };

    WindowListener windowListener = new WindowListener() {
        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            isOpen = false;
            if(db != null) {
                db.disconnect();
            }
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