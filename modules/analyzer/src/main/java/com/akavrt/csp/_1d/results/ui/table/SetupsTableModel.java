package com.akavrt.csp._1d.results.ui.table;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * User: akavrt
 * Date: 26.12.13
 * Time: 22:02
 */
public class SetupsTableModel extends AbstractTableModel {
    private static final int SELECTION_INDEX = 0;
    private static final int VERSION_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final String[] COLUMN_NAMES = {"", "#", "description"};
    private List<EvoData> mods;

    public void setData(List<EvoData> mods) {
        this.mods = mods;
        fireTableDataChanged();
    }

    public void clearData() {
        mods = null;
        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public int getRowCount() {
        return (mods == null) ? 0 : mods.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object holder = null;

        switch (columnIndex) {
            case SELECTION_INDEX:
                holder = mods.get(rowIndex).isSelected();
                break;

            case VERSION_INDEX:
                holder = mods.get(rowIndex).getName();
                break;

            case DESCRIPTION_INDEX:
                holder = mods.get(rowIndex).getDescription();
                break;
        }

        return holder;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == SELECTION_INDEX;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex == SELECTION_INDEX) {
            mods.get(rowIndex).setSelected((Boolean) value);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    public EvoData getEvoData(int index) {
        if (mods == null || index < 0 || index >= mods.size()) {
            return null;
        }

        return mods.get(index);
    }
}

