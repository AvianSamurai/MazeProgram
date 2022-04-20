package MazeGUI;

import Program.Maze;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import java.io.File;
import javax.swing.JOptionPane;

public class MazeGUI extends JFrame implements Runnable {
    public static final int WIDTH = 1440;
    public static final int HEIGHT = 1024;
    private final JPanel mainPanel = new JPanel();
    private static final int DIVIDER_SIZE = 10;

    private static Maze temp_Maze;

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

        JPanel menuPanel = new JPanel();
        menuPanel.add(new JLabel("MENU"));  // tempory
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

    private void NewMaze(){
        // All Frames and Panels
        JFrame NewMazeFrame = new JFrame();
        NewMazeFrame.setSize(800,450);
        JPanel NewMaze = new JPanel();
        NewMaze.setSize(800,450);
        JPanel NewMaze2 = new JPanel();
        NewMaze2.setSize(800,450);

        // First window
        JLabel mazeNameLabel, authNameLabel, mazeTypeLabel;
        JTextField mazeName,authName;
        NewMaze.add(new JLabel("New Maze"));  // Label of the new window

        // Fields for the user to add maze & author name
        mazeNameLabel = new JLabel("Maze Name: ");
        mazeNameLabel.setBounds(50, 100, 100, 30);
        mazeName=new JTextField();
        mazeName.setBounds(150,100, 200,30);  // Set where the fields are placed
        authNameLabel = new JLabel("Author Name: ");
        authNameLabel.setBounds(50, 150, 100, 30);
        authName=new JTextField();
        authName.setBounds(150,150, 200,30);


        // DropMenu for the MazeType
        mazeTypeLabel = new JLabel("Maze Type: ");
        mazeTypeLabel.setBounds(50, 250, 100, 30);

        String[] mazeTypeOptions = {"General", "Themed", "Others"};
        JComboBox<String> jComboBox = new JComboBox<>(mazeTypeOptions);
        jComboBox.setBounds(150, 250, 200, 30);

        // Button to go to next set of options or cancel completely
        JButton jButtonNext = new JButton("Next");
        jButtonNext.setBounds(550, 350, 90, 20);
        jButtonNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewMazeFrame.setContentPane(NewMaze2);
            }
        });
        JButton jButtonCancel = new JButton("Cancel");
        jButtonCancel.setBounds(450, 350, 90, 20);
        jButtonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewMazeFrame.dispose();
            }
        });

        // Add elements to the NewMaze Panel
        NewMaze.add(mazeNameLabel);
        NewMaze.add(mazeName);
        NewMaze.add(authNameLabel);
        NewMaze.add(authName);
        NewMaze.add(mazeTypeLabel);
        NewMaze.add(jComboBox);
        NewMaze.add(jButtonNext);
        NewMaze.add(jButtonCancel);
        NewMaze.setLayout(null);

        // Second Form
        JLabel mazeSizes;
        NewMaze2.add(new JLabel("New Maze"));  // Label of the new window

        // DropMenu for the MazeSize
        mazeSizes = new JLabel("Maze Size: ");
        mazeSizes.setBounds(50, 100, 100, 30);

        String[] mazeSizeOptions = {"100x100", "385x356", "1600x1600"};
        JComboBox<String> jComboBoxMazeSize = new JComboBox<>(mazeSizeOptions);
        jComboBoxMazeSize.setBounds(150, 100, 200, 30);

        /*// Button for choosing a logo
        JFileChooser logoChooser = new JFileChooser();
        logoChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = logoChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            // user selects a file
        }
        File selectedFile = logoChooser.getSelectedFile();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }*/

        // Button to go to next set of options or cancel completely
        Object[] options = {"Yes", "No"};
        JButton jButtonCreate2 = new JButton("Create");
        jButtonCreate2.setBounds(550, 350, 90, 20);
        jButtonCreate2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int createStatus = JOptionPane.showConfirmDialog(null,
                        "Do you want?", "New Maze", JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (createStatus == JOptionPane.YES_OPTION){
                    NewMazeFrame.dispose();
                }
                else
                    NewMazeFrame.dispose();
            }
        });
        JButton jButtonCancel2 = new JButton("Cancel");
        jButtonCancel2.setBounds(450, 350, 90, 20);
        jButtonCancel2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewMazeFrame.dispose();
            }
        });

        // Add elements to the GUI
        NewMaze2.add(mazeSizes);
        NewMaze2.add(jComboBoxMazeSize);
        NewMaze2.add(jButtonCreate2);
        NewMaze2.add(jButtonCancel2);
        NewMaze2.setLayout(null);

        // Finish frame
        NewMazeFrame.add(NewMaze);
        NewMazeFrame.setLayout(null);
        NewMazeFrame.setVisible(true);
        NewMazeFrame.setContentPane(NewMaze);
    }

    @Override
    public void run() {
        createGUI();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new MazeGUI("MazeCo"));
    }
}
