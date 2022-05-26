package MazeGUI;

import Program.*;
import Utils.Debug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class MazeEditor extends JPanel {

    private JButton[][] buttonGrid;
    private MazeStructure mazeStruct;
    private JPanel mazeCanvas;
    private SpringLayout outerAreaLayout;
    private ToolsEnum selectedTool = ToolsEnum.NONE;
    private final int BORDER_THICKNESS = 1;
    private LogoCell[][] logoCells = null;
    private Stack<int[]> cellsToUpdate = new Stack<>();
    private MazeGUI mazeGUI;

    public MazeEditor() {
        this.setLayout((outerAreaLayout = new SpringLayout()));

        mazeCanvas = new JPanel();
        mazeCanvas.setMinimumSize(GetPanelDimension());
        this.add(mazeCanvas);
        outerAreaLayout.putConstraint(SpringLayout.VERTICAL_CENTER, mazeCanvas, 0, SpringLayout.VERTICAL_CENTER, this);
        outerAreaLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, mazeCanvas, 0, SpringLayout.HORIZONTAL_CENTER, this);
    }

    public void OpenMazeStructure(MazeStructure m) {
        mazeStruct = m;
        mazeCanvas.removeAll();
        CreateButtonGrid();
        UpdateButtonGrid();
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

        int[] pos = null;
        while(!cellsToUpdate.empty()) {
            pos = cellsToUpdate.pop();
            if(pos[0] >= 0 && pos[0] < mazeStruct.getWidth() && pos[1] >= 0 && pos[1] < mazeStruct.getHeight()) {
                UpdateButton(pos[0], pos[1]);
            }
        }

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
    }

    private void ResetTools() {
        if(lastSelectedCell != null) {
            buttonGrid[lastSelectedCell[0]][lastSelectedCell[1]].setBackground(Color.WHITE);
            lastSelectedCell = null;
        }
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

            case NONE:
                break;

            default:
                Debug.LogLn("User attempted to use " + selectedTool + " but tool is not defined");
                break;
        }
    }

    private void PlaceLogo(int x, int y) {
        LogoCell.ClearLogosInMaze(mazeStruct);
        mazeStruct.InsertCellGroup(x, y, logoCells, true);
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
}
