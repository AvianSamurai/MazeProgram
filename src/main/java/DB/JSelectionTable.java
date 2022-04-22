package DB;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class JSelectionTable extends JTable {

    // Constants
    private final Dimension INTERNAL_SPACING = new Dimension(10,10);
    private static final int CELL_HEIGHT = 10;

    private DefaultTableModel dtm;

    /**
     * Creates a new JSelectionTable
     * JSelectionTable is like a normal JTable except it is not editable and
     * implements some useful methods for quickly updating the table
     *
     * @param header An array of strings for the headers
     */
    public JSelectionTable(String[] header) {
        super(TableMod(header));
        dtm = (DefaultTableModel) this.getModel();
        this.setIntercellSpacing(INTERNAL_SPACING);
        this.setRowHeight(CELL_HEIGHT + (INTERNAL_SPACING.height * 2));
        this.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
    }

    /**
     * Wipes the table clean and adds new data to it
     * where data[x][y], y must be equal to number of columns
     *
     * @param data Data to be placed in the table
     */
    public void setNewData(String[][] data){
        dtm.setNumRows(0);
        this.resizeAndRepaint();
        for(String[] d : data) {
            dtm.addRow(d);
        }
    }

    // Creates a custom table model for this type of table
    private static TableModel TableMod(String[] header) {
        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dtm.setColumnCount(header.length);
        dtm.setColumnIdentifiers(header);
        return dtm;
    }
}
