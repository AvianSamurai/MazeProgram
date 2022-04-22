package DB;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class JSelectionTable extends JTable {

    // Constants
    private final Dimension INTERNAL_SPACING = new Dimension(10,10);
    private static final int CELL_HEIGHT = 10;

    private DefaultTableModel dtm;

    public JSelectionTable(String[] header) {
        super(TableMod(header));
        dtm = (DefaultTableModel) this.getModel();
        this.setIntercellSpacing(INTERNAL_SPACING);
        this.setRowHeight(CELL_HEIGHT + (INTERNAL_SPACING.height * 2));
        this.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
    }

    public void setNewData(String[][] data){
        dtm.setNumRows(0);
        this.resizeAndRepaint();
        for(String[] d : data) {
            dtm.addRow(d);
        }
    }

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
