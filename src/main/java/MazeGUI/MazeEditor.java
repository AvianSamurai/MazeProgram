package MazeGUI;

import Program.BasicCell;
import Program.Direction;
import Program.MazeStructure;
import Utils.Debug;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeEditor extends JPanel {

    private JButton[][] buttonGrid;
    private MazeStructure mazeStruct;
    private JPanel mazeCanvas;
    private SpringLayout outerAreaLayout;
    private ToolsEnum selectedTool = ToolsEnum.NONE;
    private final int BORDER_THICKNESS = 2;

    public MazeEditor() {
        this.setLayout((outerAreaLayout = new SpringLayout()));

        mazeCanvas = new JPanel();
        mazeCanvas.setBackground(Color.green);
        mazeCanvas.setMinimumSize(GetPanelDimension());
        this.add(mazeCanvas);
        outerAreaLayout.putConstraint(SpringLayout.VERTICAL_CENTER, mazeCanvas, 0, SpringLayout.VERTICAL_CENTER, this);
        outerAreaLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, mazeCanvas, 0, SpringLayout.HORIZONTAL_CENTER, this);

    }

    public void OpenMazeStructure(MazeStructure m) {
        mazeStruct = m;
        CreateButtonGrid();
        UpdateButtonGrid();
    }

    private void UpdateButtonGrid() {
        int xCount = mazeStruct.getWidth();
        int yCount = mazeStruct.getHeight();

        for(int y = 0; y < yCount; y++) {
            for (int x = 0; x < xCount; x++) {
                UpdateButton(x, y);
            }
        }
    }

    private void UpdateButton(int x, int y) {
        BasicCell cell = mazeStruct.GetBasicCell(x, y);
        if(cell == null) {
            return;
        }
        boolean[] borders = cell.GetBorders();
        int north = borders[0] ? BORDER_THICKNESS : 0;
        int east = borders[1] ? BORDER_THICKNESS : 0;
        int south = borders[2] ? BORDER_THICKNESS : 0;
        int west = borders[3] ? BORDER_THICKNESS : 0;
        buttonGrid[x][y].setBorder(BorderFactory.createMatteBorder(north, west, south, east, Color.black));
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
                final int thisx = x; final int thisy = y;
                buttonGrid[x][y].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        MazeButtonClicked(thisx, thisy);
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
    }

    private void CarveTool(int x, int y) {
        if(lastSelectedCell == null) {
            lastSelectedCell = new int[] {x, y};
            buttonGrid[x][y].setBackground(Color.GREEN);
            return;
        }

        int offsetX = x - lastSelectedCell[0];
        int offsetY = y - lastSelectedCell[1];
        if((Math.abs(offsetX) + Math.abs(offsetY) == 1) && (Math.abs(offsetX) != Math.abs(offsetY))) {
            mazeStruct.CarveInDirection(lastSelectedCell[0], lastSelectedCell[1], Direction.OffsetToDirection(offsetX, offsetY));
            buttonGrid[lastSelectedCell[0]][lastSelectedCell[1]].setBackground(Color.WHITE);
            UpdateButton(x, y);
            UpdateButton(lastSelectedCell[0], lastSelectedCell[1]);
            lastSelectedCell = null;
        }
    }

    private void BlockTool(int x, int y) {
        if(lastSelectedCell == null) {
            lastSelectedCell = new int[] {x, y};
            buttonGrid[x][y].setBackground(Color.RED);
            return;
        }

        int offsetX = x - lastSelectedCell[0];
        int offsetY = y - lastSelectedCell[1];
        if((Math.abs(offsetX) + Math.abs(offsetY) == 1) && (Math.abs(offsetX) != Math.abs(offsetY))) {
            mazeStruct.BlockInDirection(lastSelectedCell[0], lastSelectedCell[1], Direction.OffsetToDirection(offsetX, offsetY));
            buttonGrid[lastSelectedCell[0]][lastSelectedCell[1]].setBackground(Color.WHITE);
            UpdateButton(x, y);
            UpdateButton(lastSelectedCell[0], lastSelectedCell[1]);
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

            default:
                Debug.LogLn("User attempted to use " + selectedTool + "but tool is not defined");
                break;
        }
    }
}
