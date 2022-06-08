package MazeGUI;

import DB.MazeDB;
import Program.Maze;
import Utils.Debug;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;

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
    private BufferedImage[] mazeImages;
    private Maze[] mazes;
    private JDialog imageDialog;

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

    private void ExportCMultipleMazeDialog() {
        if(table.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Please select one or more maze(s) to export");
            return;
        }

        // Create outer frame and set size
        outerFrame = new JFrame("Export Maze to Image");
        outerFrame.setSize(600, 600);
        outerFrame.addWindowListener(windowListener);

        // Set icon
        outerFrame.setIconImage(new ImageIcon(this.getClass().getResource("MazeCo.png")).getImage());

        // Create default grid bag constraints
        gBC = (GridBagConstraints) DEFAULT_GBC.clone();

        // Create outer panel and rig up the grid bag layout manager
        JPanel outerPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        outerPanel.setLayout(gbl);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mazeImages = new BufferedImage[table.getSelectedRowCount()];
        mazes = new Maze[table.getSelectedRowCount()];
        imageDialog = new JDialog();
        outerFrame.dispatchEvent(new WindowEvent(outerFrame, WindowEvent.WINDOW_CLOSING));
        for (int i = 0; i < table.getSelectedRowCount(); i++) {
            int id = Integer.parseInt((String) table.getValueAt(table.getSelectedRows()[i], 0));
            mazes[i] = Maze.LoadMazeFromID(id);
            if (mazes[i] != null) {
                int start = 9;
                int add = start + 208;
                int res = mazes[i].getMazeStructure().getWidth() * mazes[i].getMazeStructure().getHeight();
                for (int j = 64; j >= 16; j--) {
                    if (res >= start && res <= add) {
                        mazeImages[i] = mazes[i].getMazeStructure().getMazeImage(j);
                    }
                    start = add;
                    add += 208;
                }

            } else {
                JOptionPane.showMessageDialog(null, "Maze failed to open");
            }
            id++;
        }

        JLabel[] mazeNames = new JLabel[table.getSelectedRowCount()];
        JButton[] imageButtons = new JButton[table.getSelectedRowCount()];
        for (int i = 0; i < table.getSelectedRowCount(); i++) {
            mazeNames[i] = new JLabel((String) table.getValueAt(table.getSelectedRows()[i], 1));
            outerPanel.add(mazeNames[i], gBC); RestoreGBCDefaults();
            imageButtons[i] = new JButton();
            imageButtons[i].setSize(100, 100);
            imageButtons[i].setForeground(Color.RED);
            imageButtons[i].setFocusPainted(true);
            imageButtons[i].setMargin(new Insets(0, 0, 0, 0));
            imageButtons[i].setContentAreaFilled(false);
            float ratio;
            int width;
            int height;
            if (mazeImages[i].getWidth() == mazeImages[i].getHeight()){
                width = imageButtons[i].getWidth();
                height = imageButtons[i].getHeight();
            }
            else if (mazeImages[i].getWidth() > mazeImages[i].getHeight()) {
                ratio = mazeImages[i].getWidth() / imageButtons[i].getWidth();
                width = imageButtons[i].getWidth();
                height = Math.round(mazeImages[i].getHeight() / ratio);
            }
            else {
                ratio = mazeImages[i].getHeight() / imageButtons[i].getHeight();
                height = imageButtons[i].getHeight();
                width = Math.round(mazeImages[i].getWidth() / ratio);
            }
            imageButtons[i].setIcon(new ImageIcon(mazeImages[i].getScaledInstance(width, height, Image.SCALE_SMOOTH)));

            int w = 600;
            int h = 600;
            imageDialog.setSize(new Dimension(w, h));
            if (mazeImages[i].getWidth() == mazeImages[i].getHeight()){
                w = 600;
                h = 600;
            }
            else if (mazeImages[i].getWidth() > mazeImages[i].getHeight()) {
                ratio = mazeImages[i].getWidth() / 600f;
                w = 600;
                h = Math.round(mazeImages[i].getHeight() / ratio);
            }
            else {
                ratio = mazeImages[i].getHeight() / 600f;
                h = 600;
                w = Math.round(mazeImages[i].getWidth() / ratio);
            }
            imageDialog.add(new JLabel(new ImageIcon(mazeImages[i].getScaledInstance(w, h, Image.SCALE_SMOOTH))));
            imageButtons[i].addActionListener(imageButtonListener);
            NextX();
            outerPanel.add(imageButtons[i], gBC); RestoreGBCDefaults();

            JLabel resolution = new JLabel(mazeImages[i].getWidth() + "*" + mazeImages[i].getHeight());
            NextX();
            outerPanel.add(resolution, gBC); RestoreGBCDefaults();

            JLabel showSolution = new JLabel("Include a copy with optimal solution");
            NextY();
            outerPanel.add(showSolution, gBC); RestoreGBCDefaults();

            ButtonGroup G = new ButtonGroup();

            JRadioButton yes = new JRadioButton("Yes");
            NextX();
            outerPanel.add(yes, gBC); RestoreGBCDefaults();

            JRadioButton no = new JRadioButton("No");
            NextX();
            outerPanel.add(no, gBC); RestoreGBCDefaults();

            G.add(yes);
            G.add(no);

            JSeparator hr = new JSeparator(SwingConstants.HORIZONTAL);
            gBC.gridwidth = 3;
            NextY();
            outerPanel.add(hr, gBC); RestoreGBCDefaults();

            NextY();
        }

        // Buttons cancel and export
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(cancelListener);
        NextY(); NextX();
        gBC.insets = new Insets(30, 0, 0, 15);
        gBC.fill = GridBagConstraints.NONE;
        gBC.anchor = GridBagConstraints.EAST;
        outerPanel.add(cancelBtn, gBC); RestoreGBCDefaults();

        JButton exportBtn = new JButton("Export");
        exportBtn.addActionListener(downloadListener);
        NextX();
        gBC.insets = new Insets(30, 0, 0, 0);
        outerPanel.add(exportBtn, gBC); RestoreGBCDefaults();

        JScrollPane scrollPane = new JScrollPane(outerPanel);
        outerFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // show window
        outerFrame.setVisible(true);
    }

    private void popupThumbnail() {
        imageDialog.setVisible(true);
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

    private void DownloadMazesDialog() {
        // Create outer frame and set size
        outerFrame = new JFrame();
        outerFrame.setSize(new Dimension(900, 500));

        // Set icon
        outerFrame.setIconImage(new ImageIcon(this.getClass().getResource("MazeCo.png")).getImage());

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        FileFilter pngFilter = new FileTypeFilter(".png", "PNG Image");
        fileChooser.addChoosableFileFilter(pngFilter);

        int userSelection = fileChooser.showSaveDialog(outerFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                FileTypeFilter filter = (FileTypeFilter) fileChooser.getFileFilter();
                if (filter.getExtension().equals(".png")) {
                    for (int i = 0; i < mazes.length; i++) {
                        File fileToSave = fileChooser.getSelectedFile();
                        fileToSave = new File(fileToSave + (String) table.getValueAt(table.getSelectedRows()[i], 1) + ".png");
                        ImageIO.write(mazeImages[i], "png", fileToSave);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(outerFrame, "File is not a supported image file or is corrupted", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
        public void actionPerformed(ActionEvent e) {
            ExportCMultipleMazeDialog();
        }
    };

    ActionListener imageButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            popupThumbnail();
        }
    };

    ActionListener downloadListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            DownloadMazesDialog();
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
