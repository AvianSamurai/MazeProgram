package MazeGUI;

import Program.*;
import Utils.Debug;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class MazeEditor extends JPanel {

    private JButton[][] buttonGrid;
    private MazeStructure mazeStruct;
    private Maze maze;
    private JPanel mazeCanvas;
    private SpringLayout outerAreaLayout;
    private ToolsEnum selectedTool = ToolsEnum.NONE;
    private final int BORDER_THICKNESS = 1;
    private LogoCell[][] logoCells = null;
    private Stack<int[]> cellsToUpdate = new Stack<>();
    private int[][] solutionPositions = null;
    private MazeGUI mazeGUI;
    private boolean showSolution = false;

    public MazeEditor() {
        this.setLayout((outerAreaLayout = new SpringLayout()));

        mazeCanvas = new JPanel();
        mazeCanvas.setMinimumSize(GetPanelDimension());
        this.add(mazeCanvas);
        outerAreaLayout.putConstraint(SpringLayout.VERTICAL_CENTER, mazeCanvas, 0, SpringLayout.VERTICAL_CENTER, this);
        outerAreaLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, mazeCanvas, 0, SpringLayout.HORIZONTAL_CENTER, this);

        // Add startup text to maze canvas
        String username = System.getProperty("user.name");
        JLabel welcomeLabel = new JLabel("Welcome " + username + ", \n" +
                "Click 'open' to show a saved maze, or click 'new' to create a new one");
        mazeCanvas.add(welcomeLabel);
    }

    public void OpenMazeStructure(Maze m) {
        maze = m;
        mazeStruct = m.getMazeStructure();
        mazeCanvas.removeAll();
        CreateButtonGrid();
        UpdateButtonGrid();
        mazeGUI.GetMenuPanel().SetSubmenuIsEnabled(1, true);
    }

    private void UpdateSolution() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                solutionPositions = MazeAlgorithms.GenerateSolution(mazeStruct, maze.GetStartPos()[0], maze.GetStartPos()[1], maze.GetEndPos()[0], maze.GetEndPos()[1]);
                if(showSolution) {
                    if(solutionPositions != null) {
                        for (int[] solpos : solutionPositions) {
                            buttonGrid[solpos[0]][solpos[1]].setBackground(Color.CYAN);
                        }
                        repaint();
                    }
                }
                mazeGUI.UpdateSolutionsLabel(solutionPositions == null ? 0 : solutionPositions.length);
            }
        });
        thread.start();
    }

    public void AddRefrenceToMazeGUI(MazeGUI mazeGUI) {
        this.mazeGUI = mazeGUI;
        mazeGUI.getRootPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if(mazeStruct != null) {
                    mazeCanvas.setMinimumSize(GetPanelDimension());
                    mazeCanvas.removeAll();
                    CreateButtonGrid();
                    UpdateButtonGrid();
                }
            }
        });
    }

    public MazeStructure GetMazeStructure() {
        return mazeStruct;
    }

    public void SetShowSolution(boolean showSolution) {
        this.showSolution = showSolution;
    }

    public void UpdateButtonGrid() {
        // Update dead ends in a different thread so we dont bog down the program
        Thread DeadEndsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                UpdateDeadEnds();
            }
        });
        DeadEndsThread.run();

        int xCount = mazeStruct.getWidth();
        int yCount = mazeStruct.getHeight();

        for(int y = 0; y < yCount; y++) {
            for (int x = 0; x < xCount; x++) {
                UpdateButton(x, y);
            }
        }
        cellsToUpdate.clear();

        UpdateSolution();

        repaint();
    }

    /**
     * Adds button to the list of cells to update when UpdateEditedButtons() is called
     *
     * @param x cell x position
     * @param y cell y position
     */
    public void AddEditedButton(int x, int y) {
        cellsToUpdate.add(new int[]{x, y});
    }

    /**
     * Updates only the buttons that have been edited reciently, to add a button to the reciently edited list, call AddEditedButton(x, y)
     */
    public void UpdateEditedButtons() {
        // Update dead ends in a different thread so we dont bog down the program
        Thread DeadEndsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                UpdateDeadEnds();
            }
        });
        DeadEndsThread.run();

        if(solutionPositions != null) {
            for (int[] pos : solutionPositions) {
                cellsToUpdate.add(pos);
            }
        }

        int[] pos = null;
        while(!cellsToUpdate.empty()) {
            pos = cellsToUpdate.pop();
            if(pos[0] >= 0 && pos[0] < mazeStruct.getWidth() && pos[1] >= 0 && pos[1] < mazeStruct.getHeight()) {
                UpdateButton(pos[0], pos[1]);
            }
        }

        UpdateSolution();

        repaint();
    }

    private void UpdateButton(int x, int y) {
        I_Cell cell = mazeStruct.GetCell(x, y);

        // Reformat button
        buttonGrid[x][y].setBackground(Color.white);
        buttonGrid[x][y].setIcon(null);

        if(cell instanceof LogoCell) {
            LogoCell logoCell = (LogoCell) cell;
            buttonGrid[x][y].setIcon(new ImageIcon(logoCell.GetCellImage().getScaledInstance(GetButtonDimension().width,
                    GetButtonDimension().height, Image.SCALE_SMOOTH)));
        } else if (cell instanceof ImageCell) {
            ImageCell imageCell = (ImageCell) cell;
            buttonGrid[x][y].setIcon(new ImageIcon(imageCell.GetCellImage().getScaledInstance(GetButtonDimension().width,
                    GetButtonDimension().height, Image.SCALE_SMOOTH)));
        }

        if(cell instanceof BorderedCell) {
            BorderedCell borderedCell = (BorderedCell) cell;
            boolean[] borders = borderedCell.GetBorders();
            int north = borders[0] ? BORDER_THICKNESS : 0;
            int east = borders[1] ? BORDER_THICKNESS : 0;
            int south = borders[2] ? BORDER_THICKNESS : 0;
            int west = borders[3] ? BORDER_THICKNESS : 0;
            buttonGrid[x][y].setBorder(BorderFactory.createMatteBorder(north, west, south, east, Color.black));
        }
    }

    private void UpdateDeadEnds() {
        int deadEndCount = 0;
        for(int y = 0; y < mazeStruct.getHeight(); y++) {
            for(int x = 0; x < mazeStruct.getWidth(); x++) {
                I_Cell cell = mazeStruct.GetCell(x, y);
                if(cell instanceof BorderedCell) {
                    if(((BorderedCell)cell).GetBorderCount() == 3) {
                        deadEndCount++;
                    }
                }
            }
        }
        mazeGUI.UpdateDeadEndsLabel(deadEndCount);
    }

    private void CreateButtonGrid() {
        int xCount = mazeStruct.getWidth();
        int yCount = mazeStruct.getHeight();

        mazeCanvas.setLayout(new GridLayout(yCount, xCount));

        buttonGrid = new JButton[xCount][yCount];
        for(int y = 0; y < yCount; y++) {
            for(int x = 0; x < xCount; x++) {
                buttonGrid[x][y] = new JButton();
                buttonGrid[x][y].setPreferredSize(GetButtonDimension());
                buttonGrid[x][y].setBackground(Color.WHITE);
                buttonGrid[x][y].setMargin(new Insets(0, 0, 0, 0));
                final int thisx = x; final int thisy = y;
                buttonGrid[x][y].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        MazeButtonClicked(thisx, thisy);
                    }
                });
                buttonGrid[x][y].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        HoverEnter(thisx, thisy);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        HoverExit(thisx, thisy);
                    }
                });
                mazeCanvas.add(buttonGrid[x][y]);
            }
        }

        UpdateButtonGrid();
    }

    private Dimension GetPanelDimension() {
        int dim = this.getWidth() > this.getHeight() ? this.getHeight() : this.getWidth();
        return new Dimension(dim, dim);
    }

    private Dimension GetButtonDimension() {
        int mazeRatio = mazeStruct.getWidth() / mazeStruct.getHeight();
        int windowRatio = this.getWidth() / this.getHeight();
        int dim = mazeRatio > windowRatio ? this.getWidth() / mazeStruct.getWidth() : this.getHeight() / mazeStruct.getHeight();
        return new Dimension(dim, dim);
    }

    /* ======================= TOOLS ======================= */

    int[] lastSelectedCell = null;

    public void SelectTool(ToolsEnum tool) {
        Debug.LogLn("User selected " + tool.name() + " tool");
        selectedTool = tool;
        ResetTools();
        if(tool == ToolsEnum.NONE) {
            mazeGUI.GetMenuPanel().ClearHighlighting();
        }
    }

    private void ResetTools() {
        if(lastSelectedCell != null) {
            buttonGrid[lastSelectedCell[0]][lastSelectedCell[1]].setBackground(Color.WHITE);
            lastSelectedCell = null;
        }
        settingStart = true;
        UpdateButtonGrid();
    }

    private void CarveTool(int x, int y) {
        if(lastSelectedCell == null) {
            lastSelectedCell = new int[] {x, y};
            buttonGrid[x][y].setBackground(Color.GREEN);
            cellsToUpdate.add(new int[]{x, y});
            return;
        }

        int offsetX = x - lastSelectedCell[0];
        int offsetY = y - lastSelectedCell[1];
        if((Math.abs(offsetX) + Math.abs(offsetY) == 1) && (Math.abs(offsetX) != Math.abs(offsetY))) {
            mazeStruct.CarveInDirection(lastSelectedCell[0], lastSelectedCell[1], Direction.OffsetToDirection(offsetX, offsetY));
            buttonGrid[lastSelectedCell[0]][lastSelectedCell[1]].setBackground(Color.WHITE);
            AddEditedButton(x, y);
            UpdateEditedButtons();
            lastSelectedCell = null;
        }
    }

    private void BlockTool(int x, int y) {
        if(lastSelectedCell == null) {
            lastSelectedCell = new int[] {x, y};
            buttonGrid[x][y].setBackground(Color.RED);
            AddEditedButton(x, y);
            return;
        }

        int offsetX = x - lastSelectedCell[0];
        int offsetY = y - lastSelectedCell[1];
        if((Math.abs(offsetX) + Math.abs(offsetY) == 1) && (Math.abs(offsetX) != Math.abs(offsetY))) {
            mazeStruct.BlockInDirection(lastSelectedCell[0], lastSelectedCell[1], Direction.OffsetToDirection(offsetX, offsetY));
            buttonGrid[lastSelectedCell[0]][lastSelectedCell[1]].setBackground(Color.WHITE);
            AddEditedButton(x, y);
            UpdateEditedButtons();
            lastSelectedCell = null;
        }
    }

    private boolean settingStart = true;
    private int[] startPos;

    private void SetStartEnd(int x, int y) {
        if(!mazeStruct.GetCell(x, y).isPartOfMaze()) { return; }

        cellsToUpdate.add(maze.GetStartPos());
        cellsToUpdate.add(maze.GetEndPos());

        if(settingStart) {
            buttonGrid[x][y].setBackground(Color.GREEN);
            startPos = new int[]{x, y};
            cellsToUpdate.add(startPos);
            settingStart = false;
            return;
        }

        ClearPreviousStartAndEnd();
        buttonGrid[x][y].setBackground(Color.RED);
        maze.SetStartPos(startPos);
        maze.SetEndPos(new int[] {x, y});
        cellsToUpdate.add(new int[] {x, y});

        SetStartEndType();
        SelectTool(ToolsEnum.NONE);
    }

    private void SetCellImage(int x, int y) {
        if(!(mazeStruct.GetCell(x, y) instanceof ImageCell)) { return; }
        ImageCell cell = (ImageCell) mazeStruct.GetCell(x, y);

        JFileChooser fileChooser = new JFileChooser();
        FileFilter imageFilter = new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(imageFilter);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            SelectTool(ToolsEnum.NONE);
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        BufferedImage im;
        try {
            im = ImageIO.read(selectedFile);
        } catch (IOException e) {
            Debug.LogLn("Failed to open image when adding image to new cell");
            e.printStackTrace();
            SelectTool(ToolsEnum.NONE);
            return;
        }

        cell.SetCellImage(im);
        cellsToUpdate.add(new int[]{x, y});
        ResetTools();
    }

    private void ToggleCellImageType(int x, int y) {
        I_Cell cell = mazeStruct.GetCell(x, y);
        if(!(cell instanceof BasicCell)) { return; }

        if(cell instanceof ImageCell) {
            boolean borders[] = ((ImageCell)cell).GetBorders();
            BasicCell newBasicCell = new BasicCell();
            newBasicCell.SetBorders(borders);
            mazeStruct.InsertCell(x, y, newBasicCell);
        } else {
            mazeStruct.InsertCell(x, y, ((BasicCell)cell).ConvertToImageCell());
        }
        UpdateButton(x, y);
    }

    private void SetStartEndType() {
                String str = (String)JOptionPane.showInputDialog(null, "Select start and end type",
                "Selection", JOptionPane.QUESTION_MESSAGE, null, new String[] {"Classic", "Arrow", "Image", "None"}, "Classic");

        boolean success = true;
        switch (str) {
            default:
            case "None":
                ConvertToNoneStartEndType(maze.GetStartPos()[0], maze.GetStartPos()[1]);
                ConvertToNoneStartEndType(maze.GetEndPos()[0], maze.GetEndPos()[1]);
                break;

            case "Classic":
                success = ConvertToClassicStartEndType(maze.GetStartPos()[0], maze.GetStartPos()[1]) && success;
                success = ConvertToClassicStartEndType(maze.GetEndPos()[0], maze.GetEndPos()[1]) && success;

                 if(!success) {
                    JOptionPane.showMessageDialog(this,
                            "Classic start and end only works on non logo cells on the edge of the maze. Converting to none instead",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "Arrow":
                success = ConvertToArrowStartEndType(maze.GetStartPos()[0], maze.GetStartPos()[1], true) && success;
                success = ConvertToArrowStartEndType(maze.GetEndPos()[0], maze.GetEndPos()[1], false) && success;

                if(!success) {
                    JOptionPane.showMessageDialog(this,
                            "Arrow start and end only works on non logo cells on the edge of the maze. Converting to none instead",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "Image":
                success = ConvertToImageStartEndType(maze.GetStartPos()[0], maze.GetStartPos()[1]) && success;
                success = ConvertToImageStartEndType(maze.GetEndPos()[0], maze.GetEndPos()[1]) && success;

                if(!success) {
                    JOptionPane.showMessageDialog(this,
                            "Could not convert cell, are you trying to convert an logo cell?",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                break;
        }

        SelectTool(ToolsEnum.NONE);
    }

    private void ClearPreviousStartAndEnd() {
        int[] start = maze.GetStartPos();
        int[] end = maze.GetEndPos();

        if(start == null || end == null) { return; }

        if(mazeStruct.GetCell(start[0], start[1]) instanceof BasicCell) {
            boolean[] borders = mazeStruct.GetBasicCell(start[0], start[1]).GetBorders();
            Direction[] dirs = mazeStruct.GetDirectionsToValidCells(start[0], start[1], true);
            BasicCell newBasicCell = new BasicCell();
            for(Direction d : dirs) {
                newBasicCell.SetBorder(d, borders[d.GetIntValue()]);
            }
            mazeStruct.InsertCell(start[0], start[1], newBasicCell);
        }

        if(mazeStruct.GetCell(end[0], end[1]) instanceof BasicCell) {
            boolean[] borders = mazeStruct.GetBasicCell(end[0], end[1]).GetBorders();
            Direction[] dirs = mazeStruct.GetDirectionsToValidCells(end[0], end[1], true);
            BasicCell newBasicCell = new BasicCell();
            for(Direction d : dirs) {
                newBasicCell.SetBorder(d, borders[d.GetIntValue()]);
            }
            mazeStruct.InsertCell(end[0], end[1], newBasicCell);
        }
    }

    private boolean ConvertToImageStartEndType(int x, int y) {
        I_Cell cell = mazeStruct.GetCell(x, y);
        if(!(cell instanceof BasicCell)) { return false; }
        mazeStruct.InsertCell(x, y, ((BasicCell)cell).ConvertToImageCell());
        return true;
    }

    private boolean ConvertToArrowStartEndType(int x, int y, boolean isStart) {
        BasicCell basicCell = mazeStruct.GetBasicCell(x, y);
        if(basicCell == null) {
            ConvertToNoneStartEndType(x, y);
            return false;
        }

        if(!(basicCell instanceof ImageCell)) {
            mazeStruct.InsertCell(x, y, basicCell.ConvertToImageCell());
        }
        ImageCell imCell = (ImageCell) mazeStruct.GetCell(x, y);

        if(y == 0) {
            imCell.SetCellArrow(Direction.NORTH, isStart); return true;
        } else if (y == mazeStruct.getHeight() - 1) {
            imCell.SetCellArrow(Direction.SOUTH, isStart); return true;
        } else if (x == 0) {
            imCell.SetCellArrow(Direction.WEST, isStart); return true;
        } else if (x == mazeStruct.getWidth() - 1) {
            imCell.SetCellArrow(Direction.EAST, isStart); return true;
        }
        ConvertToNoneStartEndType(x, y);
        return false;
    }

    private boolean ConvertToClassicStartEndType(int x, int y) {
        BasicCell basicCell = mazeStruct.GetBasicCell(x, y);
        if(basicCell == null) {
            ConvertToNoneStartEndType(x, y);
            return false;
        }

        if(y == 0) {
            basicCell.SetBorder(Direction.NORTH, false); return true;
        } else if (y == mazeStruct.getHeight() - 1) {
            basicCell.SetBorder(Direction.SOUTH, false); return true;
        } else if (x == 0) {
            basicCell.SetBorder(Direction.WEST, false); return true;
        } else if (x == mazeStruct.getWidth() - 1) {
            basicCell.SetBorder(Direction.EAST, false); return true;
        }
        ConvertToNoneStartEndType(x, y);
        return false;
    }

    private void ConvertToNoneStartEndType(int x, int y) {
        I_Cell cell = mazeStruct.GetCell(x ,y);
        if(!(cell instanceof BorderedCell)) { return;}

        boolean[] borders = ((BorderedCell)cell).GetBorders();
        BasicCell newCell = new BasicCell();

        for(Direction dir : mazeStruct.GetDirectionsToValidCells(x, y, true)) {
            newCell.SetBorder(dir, borders[dir.GetIntValue()]);
        }

        mazeStruct.InsertCell(x, y, newCell);
    }

    public void MazeButtonClicked(int x, int y) {
        switch (selectedTool) {
            case CARVE:
                CarveTool(x, y);
                break;

            case BLOCK:
                BlockTool(x, y);
                break;

            case PLACE_LOGO:
                PlaceLogo(x, y);
                break;

            case SET_START_END:
                SetStartEnd(x, y);
                break;

            case SET_CELL_IMAGE:
                SetCellImage(x, y);
                break;

            case TOGGLE_CELL_IMAGE:
                ToggleCellImageType(x, y);
                break;

            default:
            case NONE:
                Debug.LogLn("No on cell click definition of tool type of " + selectedTool.name());
                break;
        }
    }

    private void PlaceLogo(int x, int y) {
        LogoCell.ClearLogosInMaze(mazeStruct);
        mazeStruct.InsertLogoCellGroup(x, y, logoCells, true);
        logoCells = null;
        SelectTool(ToolsEnum.NONE);
    }

    public void SetLogoToPlace(BufferedImage logo, int width, boolean shapeToLogoShape) {
        logoCells = LogoCell.CreateLogoCellGroup(logo, width, shapeToLogoShape);
    }

    private void HoverEnter(int x, int y) {
        if(selectedTool == ToolsEnum.PLACE_LOGO) {
            for(int xOffset = 0; xOffset < logoCells.length; xOffset++) {
                for(int yOffset = 0; yOffset < logoCells[0].length; yOffset++) {
                    if(logoCells[xOffset][yOffset] != null && x + logoCells.length <= mazeStruct.getWidth() && y + logoCells[0].length <= mazeStruct.getHeight()) {
                        buttonGrid[x + xOffset][y + yOffset].setBackground(Color.green);
                        AddEditedButton(x + xOffset, y + yOffset);
                    }
                }
            }
            repaint();
        }
    }

    private void HoverExit(int x, int y) {
        if(selectedTool == ToolsEnum.PLACE_LOGO) {
            UpdateEditedButtons();
        }
    }

    public void RemoveAndRegenerateLogo() {

        for(int y = 0; y < mazeStruct.getHeight(); y++) {
            for (int x = 0; x < mazeStruct.getWidth(); x++) {
                I_Cell cell = mazeStruct.GetCell(x, y);
                if(cell instanceof LogoCell) {
                    BasicCell newCell = new BasicCell();
                    mazeStruct.InsertCell(x, y, newCell);
                    cellsToUpdate.add(new int[]{x, y});
                }
            }
        }

        MazeAlgorithms.GenerateMaze(mazeStruct);
        UpdateButtonGrid();
    }

    public void DisplayMazeInfo() {
        JOptionPane.showMessageDialog(mazeGUI,
                "Title: \t" + maze.GetTitle() +
                "\nAuthor: \t" + maze.GetAuthor() +
                "\nDatabase id: \t" + maze.GetID(),
                "Maze Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
