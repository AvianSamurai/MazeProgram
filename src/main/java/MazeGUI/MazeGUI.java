package MazeGUI;


import Program.Maze;
import DB.MazeDB;
import Program.Maze;
import Utils.Debug;
import com.google.gson.Gson;
import Program.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Font.BOLD;

public class MazeGUI extends JFrame implements Runnable {
    public static final int WIDTH = 1440;
    public static final int HEIGHT = 1024;
    private final JPanel mainPanel = new JPanel();
    private static final int DIVIDER_SIZE = 10;
    private static Maze maze = null;
    private static final MazeEditor mazePanel = new MazeEditor();
    private static final MenuJPanel menuPanel = new MenuJPanel();
    GridBagConstraints c = new GridBagConstraints();

    private JTextField deadEndsTextField;
    private JTextField currentlySolvableTextField;
    private JTextField reachOptimalSolutionTextField;

    private BufferedImage mazeImage;
    private BufferedImage solutionImg;
    private JFrame outerExportFrame;
    private JPanel thumbnailPanel;
    private JRadioButton yes;

    public MazeGUI(String title) throws HeadlessException {
        super(title);
    }

    private void createGUI() {
        Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int finalWidth = screenSize.width < WIDTH ? screenSize.width : WIDTH;
        int finalHeight = screenSize.height < HEIGHT ? screenSize.height : HEIGHT;
        setSize(finalWidth, finalHeight);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mazePanel.AddRefrenceToMazeGUI(this);

        // Set icon
        this.setIconImage(new ImageIcon(this.getClass().getResource("MazeCo.png")).getImage());

        // Panel-related code
        mainPanel.setBorder(BorderFactory.createTitledBorder("MazeCo"));
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        menuPanel.CreateMenu("File",
                new String[]{         "New",                "Open",            "Save",           "Export Image"},
                new ActionListener[] {createNewMazeListener, openMazeListener, saveMazeListener, exportMazeListener}, false);
        menuPanel.CreateMenu("Edit",
                new String[]{        "Set Start & End", "Set Image",          "Toggle Image",          "Add Logo",          "Carve",           "Block"},
                new ActionListener[] {startEndListener,  setCellImageListener, toggleCellImageListener, importLogoListener,  carveToolListener, blockToolListener}, true);
        menuPanel.FinalisePanel();
        menuPanel.SetSubmenuIsEnabled(1, false);

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
                if(maze == null) { return; }

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
        JButton stepButton = new JButton("Clear Logo");
        stepButton.setPreferredSize(new Dimension(100, 25));
        JButton runButton = new JButton("Run");
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(maze == null) { return; }
                MazeAlgorithms.GenerateMaze(mazePanel.GetMazeStructure());
                mazePanel.UpdateButtonGrid();
            }
        });
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(maze == null) { return; }
                mazePanel.RemoveAndRegenerateLogo();
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

        JToggleButton showSolutionButton = new JToggleButton("OFF");  // Action a
        showSolutionButton.setMinimumSize(new Dimension(55, 0));
        JLabel showSolutionLabel = new JLabel("Show optimal solution: ");
        ItemListener itemListener = e -> {
            // event is generated in button
            int state = e.getStateChange();
            // if selected print selected in console
            if (state != ItemEvent.SELECTED) {
                showSolutionButton.setText("OFF");
                mazePanel.SetShowSolution(false);
                if(maze != null) {
                    mazePanel.UpdateButtonGrid();
                }
            } else {
                // else print deselected in console
                showSolutionButton.setText("ON");
                mazePanel.SetShowSolution(true);
                if(maze != null) {
                    mazePanel.UpdateButtonGrid();
                }
            }
        };
        showSolutionButton.addItemListener(itemListener);

        JLabel currentlySolvableLabel = new JLabel("Currently solvable: ");
        currentlySolvableTextField = new JTextField(0);
        currentlySolvableTextField.setEditable(false);

        JLabel reachOptimalSolutionLabel = new JLabel("Reached by an optimal solution: ");
        reachOptimalSolutionTextField = new JTextField(0);
        reachOptimalSolutionTextField.setEditable(false);

        JLabel deadEndsLabel = new JLabel("Dead ends: ");
        deadEndsTextField = new JTextField(0);
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

        JButton addLogo = new JButton("Add logo");
        addLogo.addActionListener(importLogoListener);
        logoPanel.add(addLogo);
        propertyPanel.add(logoPanel, c);

        // ----------------------------------------------
        // Panel 4
        JPanel infoPanel = new JPanel();
        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 1;
        // Inside
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Maze Info"), // outer border
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        JButton mazeInfo = new JButton("Show maze info");
        mazeInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(maze != null) {
                    mazePanel.DisplayMazeInfo();
                }
            }
        });
        infoPanel.add(mazeInfo);
        propertyPanel.add(infoPanel, c);

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
        saveButton.addActionListener(saveMazeListener);
        resetButton.setPreferredSize(new Dimension(100, 25));
        JButton exportButton = new JButton("Export");
        exportButton.addActionListener(exportCurrentMazeListener);
        stepButton.setPreferredSize(new Dimension(100, 25));

        saveAndExport.add(saveButton);
        saveAndExport.add(exportButton);

        flowPanel2.add(saveAndExport);
        propertyPanel.add(flowPanel2, c);

        repaint();
        setVisible(true);
    }

    private void NewMaze() {
        // All Frames and Panels
        JFrame NewMazeFrame = new JFrame();
        NewMazeFrame.setSize(700,300);
        NewMazeFrame.setResizable(false);
        JPanel NewMaze = new JPanel();
        NewMaze.setSize(700,300);
        JPanel NewMaze2 = new JPanel();
        NewMaze2.setSize(700,300);

        // First window
        JLabel mazeNameLabel, authNameLabel, mazeTypeLabel;
        JTextField mazeName,authName;
        NewMaze.add(new JLabel("New Maze"));  // Label of the new window

        // Fields for the user to add maze & author name
        String username = System.getProperty("user.name");
        mazeNameLabel = new JLabel("Maze Name: ");
        mazeNameLabel.setBounds(50, 50, 100, 30);
        mazeName=new JTextField();
        mazeName.setText(username + "s maze");
        mazeName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                mazeName.selectAll();
            }
        });
        mazeName.setBounds(150,50, 200,30);  // Set where the fields are placed
        authNameLabel = new JLabel("Author Name: ");
        authNameLabel.setBounds(50, 100, 100, 30);
        authName=new JTextField();
        authName.setText(username);
        authName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                authName.selectAll();
            }
        });
        authName.setBounds(150,100, 200,30);


        // DropMenu for the MazeType
        mazeTypeLabel = new JLabel("Maze Type: ");
        mazeTypeLabel.setBounds(50, 150, 100, 30);

        String[] mazeTypeOptions = {"Standard", "Themed", "Logo"};
        JComboBox<String> jComboBox = new JComboBox<>(mazeTypeOptions);
        jComboBox.setBounds(150, 150, 200, 30);

        // Button to go to next set of options or cancel completely
        JButton jButtonNext = new JButton("Next");
        jButtonNext.setBounds(550, 200, 90, 20);
        jButtonNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewMazeFrame.setContentPane(NewMaze2);
            }
        });
        JButton jButtonCancel = new JButton("Cancel");
        jButtonCancel.setBounds(450, 200, 90, 20);
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

        // Allows user to input custom dimensions for the maze size (width and height)
        mazeSizes = new JLabel("Maze Size: ");
        mazeSizes.setBounds(50, 50, 100, 30);

        JLabel widthLabel = new JLabel("Maze Width:");
        widthLabel.setBounds(50, 100, 80, 30); // Sets where the width label is set on the window
        JLabel heightLabel = new JLabel("Maze Height:");
        heightLabel.setBounds(50, 150, 80, 30); // Sets where the height label is set on the window

        JSpinner widthSpinner = new JSpinner();
        JSpinner heightSpinner = new JSpinner();
        SpinnerNumberModel widthModel = new SpinnerNumberModel();
        widthModel.setMaximum(100);
        widthModel.setMinimum(3);
        widthModel.setStepSize(1);
        widthModel.setValue(15);
        widthSpinner.setModel(widthModel);
        SpinnerNumberModel heightModel = new SpinnerNumberModel();
        heightModel.setMaximum(100);
        heightModel.setMinimum(3);
        heightModel.setStepSize(1);
        heightModel.setValue(15);
        heightSpinner.setModel(heightModel);
        // Maze Width and Height input fields
        widthSpinner.setBounds(130, 100, 100, 30);
        heightSpinner.setBounds(130, 150, 100, 30);

        // Button to go to next set of options or cancel completely
        Object[] options = {"Yes", "No"};
        JButton jButtonCreate2 = new JButton("Create");
        jButtonCreate2.setBounds(550, 200, 90, 20);  // Sets where create button is placed on the window
        jButtonCreate2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int createStatus = JOptionPane.showConfirmDialog(null,
                        "Do you want to automatically generate a maze?",
                        "New Maze", JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                String title = mazeName.getText().trim();
                String author = authName.getText().trim();
                //String size = jComboBoxMazeSize.getItemAt(jComboBoxMazeSize.getSelectedIndex());
                String size = widthSpinner.getValue() + "x" + heightSpinner.getValue();
                String selectedType = jComboBox.getItemAt(jComboBox.getSelectedIndex());
                NewMazeFrame.dispose();
                maze = new Maze(title, author, selectedType, (int)widthSpinner.getValue(), (int)heightSpinner.getValue());
                if (createStatus == JOptionPane.YES_OPTION){
                    MazeAlgorithms.GenerateMaze(maze.getMazeStructure());
                }
                mazePanel.OpenMazeStructure(maze);
                NewMazeFrame.dispose();
            }
        });

        JButton jButtonCancel2 = new JButton("Cancel");
        jButtonCancel2.setBounds(450, 200, 90, 20); // Sets where the cancel button is placed
        jButtonCancel2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewMazeFrame.dispose();
            }
        });

        // Add elements to the GUI
        NewMaze2.add(mazeSizes);
        NewMaze2.add(widthLabel);
        NewMaze2.add(heightLabel);
        NewMaze2.add(widthSpinner);
        NewMaze2.add(heightSpinner);
        NewMaze2.add(jButtonCreate2);
        NewMaze2.add(jButtonCancel2);
        NewMaze2.setLayout(null);

        // Finish frame
        NewMazeFrame.add(NewMaze);
        NewMazeFrame.setLayout(null);
        NewMazeFrame.setVisible(true);
        NewMazeFrame.setContentPane(NewMaze);
        NewMazeFrame.setAlwaysOnTop(true);
    }

    private void ExportCurrentMazeDialog() {
        // Create outer frame and set size
        outerExportFrame = new JFrame("Export current maze");
        outerExportFrame.setSize(new Dimension(700, 400));

        // Set icon
        outerExportFrame.setIconImage(new ImageIcon(this.getClass().getResource("MazeCo.png")).getImage());

        // Creating panels
        thumbnailPanel = new JPanel();
        JPanel exportInfoPanel = new JPanel();
        JPanel resolutionPanel = new JPanel();
        JPanel solutionChoicePanel = new JPanel();
        JPanel btnPanel  = new JPanel();

        // Ask whether exporting with maze solution or not using radio buttons
        JLabel includeSolution = new JLabel("Include a copy of maze with optimal solution");
        solutionChoicePanel.add(includeSolution);
        ButtonGroup G = new ButtonGroup();
        yes = new JRadioButton("Yes");
        yes.setSelected(true);
        JRadioButton no = new JRadioButton("No");
        solutionChoicePanel.add(yes);
        solutionChoicePanel.add(no);
        G.add(yes);
        G.add(no);
        solutionChoicePanel.setLayout(new BoxLayout(solutionChoicePanel, BoxLayout.Y_AXIS));

        // Cancel and export button
        JButton btnCancel = new JButton("Cancel");
        JButton btnExport = new JButton("Export");
        btnExport.addActionListener(downloadListener);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outerExportFrame.dispatchEvent(new WindowEvent(outerExportFrame, WindowEvent.WINDOW_CLOSING));
            }
        });
        btnPanel.add(btnCancel);
        btnPanel.add(btnExport);

        // Set maze resolution, small number of cells with higher resolution and large number of cells with lower resolution
        // minimum cell number: 3 * 3 = 9
        // maximum cell number: 100 * 100 = 10000
        // 1000 - 9 = 9991, 64 - 16 = 48
        // 9991 / 48 = 208
        int start = 9;
        int add = start + 208;
        int res = mazePanel.GetMazeStructure().getWidth() * mazePanel.GetMazeStructure().getHeight();
        for (int i = 64; i >= 16; i--) {
            if (res >= start && res <= add) {
                mazeImage = mazePanel.GetMazeStructure().getMazeImage(i);
                solutionImg = mazePanel.GetMazeStructure().drawSolution(i, maze);
            }
            start = add;
            add += 208;
        }

        // Set size and border for thumbnail panel
        thumbnailPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        thumbnailPanel.setSize(new Dimension(300, 300));
        Debug.LogLn(GetPanelDimension().width + " | " + GetPanelDimension().height);

        // Create a label to hold the maze thumbnail
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(mazeImage.getScaledInstance(GetPanelDimension().width, GetPanelDimension().height, Image.SCALE_SMOOTH)));
        thumbnailPanel.add(imageLabel);

        // Display the resolution of maze
        JLabel resolution = new JLabel("Resolution");
        JTextField textField = new JTextField(mazeImage.getWidth() + "*" + mazeImage.getHeight());
        textField.setEditable(false);
        textField.setMaximumSize(new Dimension(300, 25));
        resolutionPanel.add(resolution);
        resolutionPanel.add(textField);
        resolutionPanel.setLayout(new BoxLayout(resolutionPanel, BoxLayout.Y_AXIS));

        // Add all panels together
        exportInfoPanel.add(resolutionPanel);
        exportInfoPanel.add(solutionChoicePanel);
        exportInfoPanel.add(btnPanel);
        exportInfoPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        exportInfoPanel.setLayout(new GridLayout(3, 0));

        outerExportFrame.add(thumbnailPanel, BorderLayout.CENTER);
        outerExportFrame.add(exportInfoPanel, BorderLayout.EAST);

        // show window
        outerExportFrame.setVisible(true);
    }

    private Dimension GetPanelDimension() {
        float ratio;
        int width;
        int height;
        if (mazeImage.getWidth() >= mazeImage.getHeight()) {
            ratio = mazeImage.getWidth() / ((float)thumbnailPanel.getWidth());
            width = thumbnailPanel.getWidth();
            height = Math.round(mazeImage.getHeight() / ratio);
        }
        else {
            ratio = mazeImage.getHeight() / ((float)thumbnailPanel.getHeight());
            height = thumbnailPanel.getHeight();
            width = Math.round(mazeImage.getWidth() / ratio);
        }
        return new Dimension(width, height);
    }

    private void DownloadMazeDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileFilter fileFilter = new FileTypeFilter(".png", "PNG Image");
        fileChooser.addChoosableFileFilter(fileFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int userSelection = fileChooser.showSaveDialog(outerExportFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                FileTypeFilter fileTypefilter = (FileTypeFilter) fileChooser.getFileFilter();
                if (fileTypefilter.getExtension().equals(".png")) {
                    File mazeFile = fileChooser.getSelectedFile();
                    File solutionFile = fileChooser.getSelectedFile();
                    mazeFile = new File(mazeFile + ".png");
                    solutionFile = new File(solutionFile + "_solution.png");
                    if (yes.isSelected()) {
                        // Export both raw maze image and a copy of maze with solution
                        ImageIO.write(mazeImage, "png", mazeFile);
                        ImageIO.write(solutionImg, "png", solutionFile);
                    }
                    else{
                        // Only export maze image
                        ImageIO.write(mazeImage, "png", mazeFile);
                    }
                }
                JOptionPane.showMessageDialog(null, "Successfully exported mazes");
                outerExportFrame.dispatchEvent(new WindowEvent(outerExportFrame, WindowEvent.WINDOW_CLOSING));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(outerExportFrame, "File is not a supported image file or is corrupted", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void run() {
        createGUI();
    }

    ActionListener downloadListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            DownloadMazeDialog();
        }
    };

    ActionListener exportCurrentMazeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(maze == null) {return;}
            ExportCurrentMazeDialog();
        }
    };

    // Special spot just for menu action listeners
    ActionListener testDialogListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            JOptionPane.showMessageDialog(null, e.getActionCommand());
            maze.getMazeStructure().DebugDisplayMaze(16);
        }
    };

    ActionListener saveMazeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(maze == null) {
                JOptionPane.showMessageDialog(null, "No maze is open");
                return;
            }
            maze.SaveMaze();
            JOptionPane.showMessageDialog(null, "Maze saved to database");
        }

    };

    final private MazeGUI mazeGUI = this;

    ActionListener createNewMazeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            NewMaze();
        }
    };

    ActionListener openMazeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) { OpenMazeDialog.Open(mazeGUI); }
    };

    ActionListener exportMazeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) { ExportMazeDialog.Open(); }
    };

    ActionListener importLogoListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(maze == null) { return; }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(mainPanel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                AddLogoDialogue.OpenAddLogoDialogue(null, mazePanel, selectedFile.getAbsolutePath());
            }
        }
    };

    ActionListener setCellImageListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            mazePanel.SelectTool(ToolsEnum.SET_CELL_IMAGE);
        }
    };

    ActionListener toggleCellImageListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {mazePanel.SelectTool(ToolsEnum.TOGGLE_CELL_IMAGE);}
    };

    ActionListener startEndListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {mazePanel.SelectTool(ToolsEnum.SET_START_END);}
    };

    ActionListener carveToolListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            mazePanel.SelectTool(ToolsEnum.CARVE);
        }
    };

    ActionListener blockToolListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            mazePanel.SelectTool(ToolsEnum.BLOCK);
        }
    };

    /**
     * Sets the dead end label to the given integer
     * @param i the int to set the label to
     */
    public void UpdateDeadEndsLabel(int i) {
        deadEndsTextField.setText(i + "");
    }

    public void UpdateSolutionsLabel(int length) {
        currentlySolvableTextField.setText("");
        currentlySolvableTextField.setText(length > 0 ? "Solvable" : "Not Solvable");
        currentlySolvableTextField.setBackground(length > 0 ? Color.green : Color.pink);
        reachOptimalSolutionTextField.setText(length + "");
    }

    public void OpenMaze(Maze m) {
        maze = m;
        mazePanel.OpenMazeStructure(m);
    }

    public MenuJPanel GetMenuPanel() {
        return menuPanel;
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        // Uncomment this to clear your database and insert fake data
        //MazeDB dbm = new MazeDB();
        //dbm.LoadTestDataIntoDatabase(true);
        //dbm.disconnect();
        SwingUtilities.invokeLater(new MazeGUI(("MazeCo")));
    }
}
