package MazeGUI;


import DB.MazeDB;
import Program.Maze;
import com.google.gson.Gson;
import Program.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MazeGUI extends JFrame implements Runnable {
    public static final int WIDTH = 1440;
    public static final int HEIGHT = 1024;
    private final JPanel mainPanel = new JPanel();
    private static final int DIVIDER_SIZE = 10;
    private static Maze temp_Maze;
    private static final MazeEditor mazePanel = new MazeEditor();
    GridBagConstraints c = new GridBagConstraints();

    public MazeGUI(String title) throws HeadlessException {
        super(title);
    }

    private void createGUI() {
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set icon
        this.setIconImage(new ImageIcon(this.getClass().getResource("MazeCo.png")).getImage());

        // Panel-related code
        mainPanel.setBorder(BorderFactory.createTitledBorder("MazeCo"));
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        MenuJPanel menuPanel = new MenuJPanel();
        menuPanel.CreateMenu("File",
                new String[]{"New", "Open", "Save", "Export Image"},
                new ActionListener[] {createNewMazeListener, openMazeListener, testDialogListener, exportMazeListener});
        menuPanel.CreateMenu("Edit",
                new String[]{"Set Start/End", "Add (logo, image)", "Maze Type", "Draw"},
                new ActionListener[] {testDialogListener, importLogoListener, testDialogListener, testDialogListener});
        menuPanel.FinalisePanel();

        JPanel propertyPanel = new JPanel();
        //propertyPanel.add(new JLabel("PROPERTY"));

        // Split the mainPanel into three panels
        JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mazePanel, propertyPanel);
        splitPane1.setDividerSize(DIVIDER_SIZE);
        //splitPane1.setDividerLocation(400); // the value is not fixed
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

        // Property panel
        propertyPanel.setLayout(new GridBagLayout());
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        // ----------------------------------------------
        // Panel 1
        JPanel flowPanel = new JPanel();
        JPanel mazeGenerationPanel = new JPanel();
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        // Inside
        flowPanel.setLayout(new FlowLayout());
        flowPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Maze generation"), // outer border
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        mazeGenerationPanel.setLayout(new GridLayout(3, 1, 15, 15));

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null,"Are you sure you want to reset the maze?", "Reset Maze",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if(result == JOptionPane.YES_OPTION){
                    mazePanel.GetMazeStructure().ResetBasicCells();
                    mazePanel.UpdateButtonGrid();
                }
            }
        });
        resetButton.setPreferredSize(new Dimension(100, 25));
        JButton stepButton = new JButton("Step");
        stepButton.setPreferredSize(new Dimension(100, 25));
        JButton runButton = new JButton("Run");
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MazeAlgorithms.GenerateMaze(mazePanel.GetMazeStructure());
                mazePanel.UpdateButtonGrid();
            }
        });
        runButton.setPreferredSize(new Dimension(100, 25));

        mazeGenerationPanel.add(resetButton);
        mazeGenerationPanel.add(stepButton);
        mazeGenerationPanel.add(runButton);

        flowPanel.add(mazeGenerationPanel);
        propertyPanel.add(flowPanel, c);

        // ----------------------------------------------
        // Panel 2
        JPanel propertiesPanel = new JPanel();
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 2;
        // Inside
        propertiesPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Properties"), // outer border
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        GroupLayout groupLayout = new GroupLayout(propertiesPanel);
        propertiesPanel.setLayout(groupLayout);

        JToggleButton showSolutionButton = new JToggleButton("ON");  // Action a
        showSolutionButton.setMinimumSize(new Dimension(55, 0));
        JLabel showSolutionLabel = new JLabel("Show optimal solution: ");
        ItemListener itemListener = e -> {
            // event is generated in button
            int state = e.getStateChange();
            // if selected print selected in console
            if (state == ItemEvent.SELECTED) {
                showSolutionButton.setText("OFF");
                mazePanel.SetShowSolution(false);
                mazePanel.UpdateButtonGrid();
            } else {
                // else print deselected in console
                showSolutionButton.setText("ON");
                mazePanel.SetShowSolution(true);
                mazePanel.UpdateButtonGrid();
            }
        };
        showSolutionButton.addItemListener(itemListener);

        JLabel currentlySolvableLabel = new JLabel("Currently solvable: ");
        JTextField currentlySolvableTextField = new JTextField(0);
        currentlySolvableTextField.setEditable(false);

        JLabel reachOptimalSolutionLabel = new JLabel("Reached by an optimal solution: ");
        JTextField reachOptimalSolutionTextField = new JTextField(0);
        reachOptimalSolutionTextField.setEditable(false);

        JLabel deadEndsLabel = new JLabel("Dead ends: ");
        JTextField deadEndsTextField = new JTextField(0);
        deadEndsTextField.setEditable(false);

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(showSolutionLabel)
                        .addComponent(currentlySolvableLabel)
                        .addComponent(reachOptimalSolutionLabel)
                        .addComponent(deadEndsLabel)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(showSolutionButton)
                        .addComponent(currentlySolvableTextField)
                        .addComponent(reachOptimalSolutionTextField)
                        .addComponent(deadEndsTextField)
                )
        );
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(showSolutionLabel)
                        .addComponent(showSolutionButton)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(currentlySolvableLabel)
                        .addComponent(currentlySolvableTextField)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(reachOptimalSolutionLabel)
                        .addComponent(reachOptimalSolutionTextField)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(deadEndsLabel)
                        .addComponent(deadEndsTextField)
                )
        );

        propertyPanel.add(propertiesPanel, c);

        // ----------------------------------------------
        // Panel 3
        JPanel logoPanel = new JPanel();
        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 1;
        // Inside
        logoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Logo"), // outer border
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JButton addLogo = new JButton("Add/Change logo");
        logoPanel.add(addLogo);
        propertyPanel.add(logoPanel, c);

        // ----------------------------------------------
        // Panel 4
        JPanel imagePanel = new JPanel();
        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 1;
        // Inside
        imagePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Image"), // outer border
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        JButton addImage = new JButton("Add/Change image");
        imagePanel.add(addImage);
        propertyPanel.add(imagePanel, c);

        // ----------------------------------------------
        // Panel 5
        JPanel saveAndExport = new JPanel();
        JPanel flowPanel2 =  new JPanel();
        c.gridx = 3;
        c.gridy = 0;
        c.gridheight = 2;
        // Inside
        flowPanel2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Save & Export"), // outer border
                BorderFactory.createEmptyBorder(20, 15, 10, 15)));
        saveAndExport.setLayout(new GridLayout(2, 1, 15, 30));

        JButton saveButton = new JButton("Save");
        resetButton.setPreferredSize(new Dimension(100, 25));
        JButton exportButton = new JButton("Export");
        stepButton.setPreferredSize(new Dimension(100, 25));

        saveAndExport.add(saveButton);
        saveAndExport.add(exportButton);

        flowPanel2.add(saveAndExport);
        propertyPanel.add(flowPanel2, c);

        //pack();
        repaint();
        setVisible(true);

        MazeStructure m = MazeFactory.CreateBasicMaze(15, 15);// TODO temp, pls remove
        MazeAlgorithms.GenerateMaze(m); // TODO temp pls remove
        mazePanel.OpenMazeStructure(m); // TODO temp, pls remove
    }

    private void NewMaze() {
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
                        "Do you want to automatically generate a maze?",
                        "New Maze", JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (createStatus == JOptionPane.YES_OPTION){
                    String title = mazeName.getText().trim();
                    String author = authName.getText().trim();
                    String size = jComboBoxMazeSize.getItemAt(jComboBoxMazeSize.getSelectedIndex());
                    String selectedType = jComboBox.getItemAt(jComboBox.getSelectedIndex());
                    NewMazeFrame.dispose();
                    Maze newMaze = new Maze(title, author, size, selectedType);
                    String date = newMaze.GetDateTime();
                    Map<String, String> jsonData = new HashMap<>();
                    jsonData.put("title",title);
                    jsonData.put("author", author);
                    jsonData.put("size", size);
                    jsonData.put("type", selectedType);
                    Gson gson = new Gson();
                    String output = gson.toJson(jsonData);
                    MazeDB ndm = null;
                    try {
                        ndm = new MazeDB();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        ndm.CreateUpdateDelete("INSERT INTO saved_mazes (name, author_name, json_data, creation_date) VALUES ('"+ title +"','" + author +"','"+ output +"','"+ date +"');");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

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

    // Special spot just for menu action listeners
    ActionListener testDialogListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, e.getActionCommand());
        }
    };

    ActionListener createNewMazeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            NewMaze();
        }
    };

    ActionListener openMazeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) { new OpenMazeDialog(); }
    };

    ActionListener exportMazeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) { new ExportMazeDialog(); }
    };

    ActionListener importLogoListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(mainPanel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }
        }
    };

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        // Uncomment this to clear your database and insert fake data
        MazeDB dbm = new MazeDB();
        dbm.LoadTestDataIntoDatabase(true);
        dbm.disconnect();
        SwingUtilities.invokeLater(new MazeGUI(("MazeCo")));
    }
}
