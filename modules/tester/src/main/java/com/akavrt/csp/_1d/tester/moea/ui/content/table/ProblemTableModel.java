package com.akavrt.csp._1d.tester.moea.ui.content.table;

import com.akavrt.csp._1d.core.Problem;

import javax.swing.table.AbstractTableModel;

/**
 * User: akavrt
 * Date: 11.04.13
 * Time: 17:52
 */
public class ProblemTableModel extends AbstractTableModel {
    private static final int NUMBER_INDEX = 0;
    private static final int LENGTH_INDEX = 1;
    private static final int DEMAND_INDEX = 2;
    private final String[] columnNames = {"#", "l_i", "d_i"};
    private Problem problem;

    public void setData(Problem problem) {
        this.problem = problem;

        fireTableDataChanged();
    }

    public void clearData() {
        problem = null;

        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return (problem == null) ? 0 : problem.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object holder = null;

        switch (columnIndex) {
            case NUMBER_INDEX:
                holder = rowIndex + 1;
                break;

            case LENGTH_INDEX:
                holder = problem.getOrder(rowIndex).getLength();
                break;

            case DEMAND_INDEX:
                holder = problem.getOrder(rowIndex).getDemand();
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

}
