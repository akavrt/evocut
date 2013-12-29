package com.akavrt.csp._1d.results.ui.table;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * User: akavrt
 * Date: 27.12.13
 * Time: 17:16
 */
public class ProxyCellRenderer implements TableCellRenderer {

    protected static final Border DEFAULT_BORDER = new EmptyBorder(1, 1, 1, 1);
    private TableCellRenderer renderer;

    public ProxyCellRenderer(TableCellRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row,
                                                   int column) {
        Component comp = renderer.getTableCellRendererComponent(table, value, isSelected,
                                                                hasFocus, row, column);
        if (comp instanceof JComponent) {
            ((JComponent) comp).setBorder(DEFAULT_BORDER);
        }

        return comp;
    }
}
