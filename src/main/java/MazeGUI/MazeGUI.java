package MazeGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MazeGUI extends JFrame implements Runnable {
    public static final int WIDTH = 1440;
    public static final int HEIGHT = 1024;
    private final JPanel mainPanel = new JPanel();
    private static final int DIVIDER_SIZE = 10;
    GridBagConstraints c = new GridBagConstraints();

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
                new String[]{"New", "Open", "Save", "Export Image"},
                new ActionListener[] {testDialogListener, testDialogListener, testDialogListener, testDialogListener});
        menuPanel.CreateMenu("Edit",
                new String[]{"Set Start/End", "Add (logo, image)", "Maze Type", "Draw"},
                new ActionListener[] {testDialogListener, testDialogListener, testDialogListener, testDialogListener});
        menuPanel.FinalisePanel();

        JPanel mazePanel = new JPanel();
        mazePanel.add(new JLabel("MAZE CANVAS"));
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
        resetButton.setPreferredSize(new Dimension(100, 25));
        JButton stepButton = new JButton("Step");
        stepButton.setPreferredSize(new Dimension(100, 25));
        JButton runButton = new JButton("Run");
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
        JLabel showSolutionLabel = new JLabel("Show optimal solution: ");
        ItemListener itemListener = e -> {
            // event is generated in button
            int state = e.getStateChange();
            // if selected print selected in console
            if (state == ItemEvent.SELECTED) {
                showSolutionButton.setText("OFF");
            } else {
                // else print deselected in console
                showSolutionButton.setText("ON");
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
    public static void main(String[] args){
        SwingUtilities.invokeLater(new MazeGUI(("MazeCo")));
    }
}
