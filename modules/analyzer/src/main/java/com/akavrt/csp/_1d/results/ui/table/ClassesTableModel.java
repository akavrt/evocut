package com.akavrt.csp._1d.results.ui.table;

import com.akavrt.csp._1d.results.CutgenClass;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * User: akavrt
 * Date: 26.12.13
 * Time: 21:42
 */
public class ClassesTableModel extends AbstractTableModel {
    private static final int CLASS_INDEX = 0;
    private static final int SIZE_INDEX = 1;
    private static final int DEMAND_INDEX = 2;
    private static final int RESULTS_INDEX = 3;
    private static final int EFFECTIVE_INDEX = 4;
    private static final String[] COLUMN_NAMES = {"class", "m", "d", "results", "pareto"};
    private List<ClassData> classes;

    public void setData(List<ClassData> classes) {
        this.classes = classes;

        fireTableDataChanged();
    }

    public void clearData() {
        classes = null;

        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public int getRowCount() {
        return (classes == null) ? 0 : classes.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object holder = null;

        switch (columnIndex) {
            case CLASS_INDEX:
                holder = classes.get(rowIndex).getCutgenClass().getName();
                break;

            case SIZE_INDEX:
                holder = classes.get(rowIndex).getCutgenClass().getSize();
                break;

            case DEMAND_INDEX:
                holder = classes.get(rowIndex).getCutgenClass().getAverageDemand();
                break;

            case RESULTS_INDEX:
                holder = classes.get(rowIndex).getResultsCount();
                break;

            case EFFECTIVE_INDEX:
                holder = classes.get(rowIndex).getEffectiveCount();
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
        return false;
    }

    public CutgenClass getCutgenClass(int index) {
        if (classes == null || index < 0 || index >= classes.size()) {
            return null;
        }

        return classes.get(index).getCutgenClass();
    }
}
