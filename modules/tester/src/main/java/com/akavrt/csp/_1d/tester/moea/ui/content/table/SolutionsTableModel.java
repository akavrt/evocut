package com.akavrt.csp._1d.tester.moea.ui.content.table;

import com.akavrt.csp._1d.solver.moea.Chromosome;
import com.google.common.collect.Lists;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * User: akavrt
 * Date: 11.04.13
 * Time: 17:52
 */
public class SolutionsTableModel extends AbstractTableModel {
    private static final int NUMBER_INDEX = 0;
    private static final int MATERIAL_INDEX = 1;
    private static final int SETUPS_INDEX = 2;
    private static final int FEASIBILITY_INDEX = 3;
    private static final int RANK_INDEX = 4;
    private static final int AGE_INDEX = 5;
    private static final int HASH_INDEX = 6;
    private final String[] columnNames = {"#", "material", "setups", "feasible", "rank", "age", "hash"};
    private final List<Chromosome> solutions;

    public SolutionsTableModel() {
        solutions = Lists.newArrayList();
    }

    public void setData(List<Chromosome> data) {
        solutions.clear();

        if (data != null) {
            solutions.addAll(data);
        }

        fireTableDataChanged();
    }

    public void clearData() {
        solutions.clear();

        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return solutions.size();
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

            case MATERIAL_INDEX:
                holder = solutions.get(rowIndex).getObjective(0);
                break;

            case SETUPS_INDEX:
                holder = solutions.get(rowIndex).getObjective(1);
                break;

            case FEASIBILITY_INDEX:
                holder = solutions.get(rowIndex).isFeasible();
                break;

            case RANK_INDEX:
                holder = solutions.get(rowIndex).getRank();
                break;

            case AGE_INDEX:
                holder = solutions.get(rowIndex).getAge();
                break;

            case HASH_INDEX:
                holder = solutions.get(rowIndex).getPlan().hashCode();
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
