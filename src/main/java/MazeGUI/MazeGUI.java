package MazeGUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeGUI extends JFrame implements Runnable {
    public static final int WIDTH = 1440;
    public static final int HEIGHT = 1024;
    private final JPanel mainPanel = new JPanel();
    private static final int DIVIDER_SIZE = 10;

    public MazeGUI(String title) throws HeadlessException {
        super(title);
    }

    private void createGUI() {
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel-related code
        mainPanel.setBorder(BorderFactory.createTitledBorder("MazeCo"));
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        MenuJPanel menuPanel = new MenuJPanel();
        menuPanel.CreateMenu("File",
                new String[]{"New", "Open", "Import", "Export"},
                new ActionListener[] {testDialogListener, testDialogListener, testDialogListener, testDialogListener});
        menuPanel.CreateMenu("Edit",
                new String[]{"Set Start/End", "Add (logo, image)", "Maze Type", "Draw"},
                new ActionListener[] {testDialogListener, testDialogListener, testDialogListener, testDialogListener});
        menuPanel.FinalisePanel();

        JPanel mazePanel = new JPanel();
        mazePanel.add(new JLabel("MAZE CANVAS"));   // tempory
        JPanel propertyPanel = new JPanel();
        propertyPanel.add(new JLabel("PROPERTY"));  // tempory

        // Split the mainPanel into three panels
        JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mazePanel, propertyPanel);
        splitPane1.setDividerSize(DIVIDER_SIZE);
        splitPane1.setDividerLocation(400); // the value is not fixed
                                            // once buttons and other widgets are added
                                            // the panel may resize to fit them
                                            // thus can be deleted later
        splitPane1.setOneTouchExpandable(true);
        // Make the size of property panel remain fixed
        splitPane1.setResizeWeight(1.0);

        JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, splitPane1);
        splitPane2.setDividerSize(DIVIDER_SIZE);
        splitPane2.setDividerLocation(200); // same as for splitPanel1
        splitPane2.setOneTouchExpandable(true);
        // Make the size of menu panel remain fixed
        splitPane2.setResizeWeight(0.0);

        mainPanel.add(splitPane2, BorderLayout.CENTER);

        // pack();
        repaint();
        setVisible(true);
    }

    @Override
    public void run() {
        createGUI();
    }

    // Special spot just for menu action listeners
    ActionListener testDialogListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, e.getActionCommand());
        }
    };
}
