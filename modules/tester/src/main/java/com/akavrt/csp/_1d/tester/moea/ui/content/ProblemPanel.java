package com.akavrt.csp._1d.tester.moea.ui.content;

import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.solver.moea.Chromosome;
import com.akavrt.csp._1d.tester.moea.ui.content.table.HeaderCellRenderer;
import com.akavrt.csp._1d.tester.moea.ui.content.table.IndexCellRenderer;
import com.akavrt.csp._1d.tester.moea.ui.content.table.ProblemTableModel;
import com.akavrt.csp._1d.tester.moea.ui.content.table.SolutionsTableModel;
import com.akavrt.csp._1d.tester.moea.ui.utils.GBC;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

/**
 * User: akavrt
 * Date: 20.07.13
 * Time: 20:53
 */
public class ProblemPanel extends JPanel {
    private final JTable problemTable;
    private final ProblemTableModel problemTableModel;
    private final JTable solutionsTable;
    private final SolutionsTableModel solutionsTableModel;

    public ProblemPanel() {
        problemTableModel = new ProblemTableModel();
        problemTable = prepareProblemTable(problemTableModel);

        JScrollPane problemTablePane = new JScrollPane(problemTable);
        problemTablePane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));

        solutionsTableModel = new SolutionsTableModel();
        solutionsTable = prepareSolutionsTable(solutionsTableModel);

        JScrollPane solutionsTablePane = new JScrollPane(solutionsTable);
        solutionsTablePane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));

        setLayout(new GridBagLayout());
        add(new JLabel("Problem"),
            new GBC(0, 0).setFill(GBC.BOTH).setWeight(30, 0).setAnchor(GBC.WEST)
                         .setInsets(10, 15, 0, 0));
        add(problemTablePane, new GBC(0, 1).setWeight(20, 100).setFill(GBC.BOTH)
                                           .setInsets(10, 15, 10, 0));
        add(new JLabel("Solutions"),
            new GBC(1, 0).setFill(GBC.BOTH).setWeight(100, 0).setAnchor(GBC.WEST)
                         .setInsets(10, 15, 0, 0));
        add(solutionsTablePane, new GBC(1, 1).setWeight(100, 100).setFill(GBC.BOTH)
                                             .setInsets(10, 15, 10, 15));

        setBackground(Color.white);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
    }

    public void setProblem(Problem problem) {
        problemTableModel.setData(problem);

        if (problem != null) {
            problemTable.setRowSelectionInterval(0, 0);
        }
    }

    public void clearProblem() {
        problemTableModel.clearData();
    }

    public void setSolutions(List<Chromosome> solutions) {
        solutionsTableModel.setData(solutions);

        if (solutions != null && !solutions.isEmpty()) {
            solutionsTable.setRowSelectionInterval(0, 0);
        }
    }

    public void clearSolutions() {
        solutionsTableModel.clearData();
    }

    private JTable prepareProblemTable(AbstractTableModel model) {
        JTable table = new JTable(model);

        TableColumnModel columnModel = table.getColumnModel();

        TableColumn indexColumn = columnModel.getColumn(0);
        indexColumn.setCellRenderer(new IndexCellRenderer());
        indexColumn.setPreferredWidth(40);
        indexColumn.setMinWidth(40);
        indexColumn.setMaxWidth(60);
        indexColumn.setResizable(false);

        TableColumn orderLengthColumn = columnModel.getColumn(1);
        orderLengthColumn.setMinWidth(40);
        orderLengthColumn.setResizable(true);

        TableColumn orderDemandColumn = columnModel.getColumn(2);
        orderDemandColumn.setMinWidth(40);
        orderDemandColumn.setResizable(true);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Font headerFont = table.getTableHeader().getFont();
        headerFont = new Font(headerFont.getName(), Font.BOLD, headerFont.getSize());
        table.getTableHeader().setFont(headerFont);
        table.getTableHeader().setDefaultRenderer(new HeaderCellRenderer(table));

        table.setFillsViewportHeight(true);

        return table;
    }

    private JTable prepareSolutionsTable(AbstractTableModel model) {
        JTable table = new JTable(model);

        TableColumnModel columnModel = table.getColumnModel();

        // {"#", "material", "setups", "feasible", "rank", "age", "hash"}
        TableColumn indexColumn = columnModel.getColumn(0);
        indexColumn.setCellRenderer(new IndexCellRenderer());
        indexColumn.setPreferredWidth(40);
        indexColumn.setMinWidth(40);
        indexColumn.setMaxWidth(60);
        indexColumn.setResizable(false);

        TableColumn materialColumn = columnModel.getColumn(1);
        materialColumn.setMinWidth(40);
        materialColumn.setResizable(true);

        TableColumn setupsColumn = columnModel.getColumn(2);
        setupsColumn.setMinWidth(40);
        setupsColumn.setResizable(true);

        TableColumn feasibilityColumn = columnModel.getColumn(3);
        feasibilityColumn.setPreferredWidth(80);
        feasibilityColumn.setMinWidth(80);
        feasibilityColumn.setMaxWidth(80);
        feasibilityColumn.setResizable(false);

        TableColumn rankColumn = columnModel.getColumn(4);
        rankColumn.setPreferredWidth(80);
        rankColumn.setMinWidth(80);
        rankColumn.setMaxWidth(80);
        rankColumn.setResizable(false);

        TableColumn ageColumn = columnModel.getColumn(5);
        ageColumn.setMinWidth(40);
        ageColumn.setResizable(true);

        TableColumn hashColumn = columnModel.getColumn(6);
        hashColumn.setMinWidth(100);
        hashColumn.setPreferredWidth(120);
        hashColumn.setResizable(true);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Font headerFont = table.getTableHeader().getFont();
        headerFont = new Font(headerFont.getName(), Font.BOLD, headerFont.getSize());
        table.getTableHeader().setFont(headerFont);
        table.getTableHeader().setDefaultRenderer(new HeaderCellRenderer(table));

        table.setFillsViewportHeight(true);

        return table;
    }

}
