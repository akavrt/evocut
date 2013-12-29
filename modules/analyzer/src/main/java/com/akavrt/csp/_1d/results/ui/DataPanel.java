package com.akavrt.csp._1d.results.ui;

import com.akavrt.csp._1d.results.CutgenClass;
import com.akavrt.csp._1d.results.EvoResult;
import com.akavrt.csp._1d.results.MethodMod;
import com.akavrt.csp._1d.results.PaperResult;
import com.akavrt.csp._1d.results.ui.table.ClassesTableModel;
import com.akavrt.csp._1d.results.ui.table.EvoData;
import com.akavrt.csp._1d.results.ui.table.ProxyCellRenderer;
import com.akavrt.csp._1d.results.ui.table.SetupsTableModel;
import com.akavrt.csp._1d.tester.moea.ui.content.table.HeaderCellRenderer;
import com.akavrt.csp._1d.tester.moea.ui.utils.GBC;
import com.google.common.collect.Lists;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * User: akavrt
 * Date: 26.12.13
 * Time: 20:43
 */
public class DataPanel extends JPanel implements TableModelListener {
    private final JTable classesTable;
    private final ClassesTableModel classesTableModel;
    private final SetupsTableModel setupsTableModel;
    private final ChartDataHelper chartHelper;
    private final ChartPanel chartPanel;
    private final AnalyzerFrame parent;
    private CutgenClass currentClass;
    private java.util.List<MethodMod> evoData;
    private java.util.List<EvoData> filteredEvoData;

    public DataPanel(AnalyzerFrame parent) {
        this.parent = parent;

        classesTableModel = new ClassesTableModel();
        classesTable = prepareClassesTable(classesTableModel);

        JScrollPane classesTablePane = new JScrollPane(classesTable);
        classesTablePane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));

        setupsTableModel = new SetupsTableModel();
        JTable setupsTable = prepareSetupsTable(setupsTableModel);

        JScrollPane setupsTablePane = new JScrollPane(setupsTable);
        setupsTablePane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));

        chartHelper = new ChartDataHelper();
        JFreeChart chart = chartHelper.createChart();
        chartPanel = new ChartPanel(chart, true, true, false, true, true);
        chartPanel.setMinimumDrawHeight(200);
        chartPanel.setMinimumDrawWidth(200);
        chartPanel.setMaximumDrawWidth(2000);
        chartPanel.setMaximumDrawHeight(2000);

        JPanel leftPane = new JPanel();
        leftPane.setLayout(new GridBagLayout());
        leftPane.add(new JLabel("CUTGEN classes"),
                     new GBC(0, 0).setFill(GBC.BOTH).setWeight(35, 0).setAnchor(GBC.WEST)
                                  .setInsets(10, 15, 0, 0));
        leftPane.add(classesTablePane,
                     new GBC(0, 1).setWeight(35, 100).setFill(GBC.BOTH).setInsets(10, 15, 10, 0));

        leftPane.setPreferredSize(new Dimension(260, 0));
        leftPane.setBackground(Color.white);

        JPanel centerPane = new JPanel();
        centerPane.setLayout(new GridBagLayout());
        centerPane.add(chartPanel,
                       new GBC(0, 0, 1, 2).setWeight(100, 100).setFill(GBC.BOTH)
                                          .setInsets(10, 15, 10, 0));
        centerPane.add(new JLabel("EvoCut setups"),
                       new GBC(1, 0).setFill(GBC.BOTH).setWeight(20, 0).setAnchor(GBC.WEST)
                                    .setInsets(10, 15, 0, 0));
        centerPane.add(setupsTablePane,
                       new GBC(1, 1).setWeight(20, 100).setFill(GBC.BOTH)
                                    .setInsets(10, 15, 10, 15));

        centerPane.setBackground(Color.white);

        setLayout(new BorderLayout());
        add(leftPane, BorderLayout.LINE_START);
        add(centerPane, BorderLayout.CENTER);

        setBackground(Color.white);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
    }

    public void setPapersData(java.util.List<com.akavrt.csp._1d.results.ui.table.ClassData> classes) {
        classesTableModel.setData(classes);

        if (classes != null && !classes.isEmpty()) {
            classesTable.setRowSelectionInterval(0, 0);
        }
    }

    public void clearPapersData() {
        currentClass = null;
        classesTableModel.clearData();
        chartHelper.clearPapersData();
    }

    public void setEvoCutData(java.util.List<MethodMod> data) {
        evoData = data;
        filteredEvoData = applyFilter(currentClass, evoData);
        setupsTableModel.setData(filteredEvoData);
    }

    private java.util.List<EvoData> applyFilter(CutgenClass cutgenClass,
                                                java.util.List<MethodMod> data) {
        if (cutgenClass == null || data == null) {
            return null;
        }

        java.util.List<EvoData> filtered = Lists.newArrayList();
        for (MethodMod mod : data) {
            java.util.List<EvoResult> results = mod.getClassResults(cutgenClass.getName());
            if (results != null && !results.isEmpty()) {
                EvoData evoData = new EvoData(mod.getName(), mod.getDescription(), results);
                filtered.add(evoData);
            }
        }

        return filtered;
    }

    public void clearEvoCutData() {
        evoData = null;
        filteredEvoData = null;

        setupsTableModel.clearData();
        chartHelper.removeAllEvoSeries();
    }

    private JTable prepareClassesTable(AbstractTableModel model) {
        JTable table = new JTable(model);

        TableColumnModel columnModel = table.getColumnModel();

        TableColumn classColumn = columnModel.getColumn(0);
        classColumn.setResizable(true);

        TableColumn sizeColumn = columnModel.getColumn(1);
        sizeColumn.setPreferredWidth(35);
        sizeColumn.setMinWidth(35);
        sizeColumn.setMaxWidth(35);
        sizeColumn.setResizable(false);

        TableColumn demandColumn = columnModel.getColumn(2);
        demandColumn.setPreferredWidth(35);
        demandColumn.setMinWidth(35);
        demandColumn.setMaxWidth(35);
        demandColumn.setResizable(false);

        TableColumn resultsColumn = columnModel.getColumn(3);
        resultsColumn.setPreferredWidth(55);
        resultsColumn.setMinWidth(55);
        resultsColumn.setMaxWidth(55);
        resultsColumn.setResizable(false);

        TableColumn effectiveColumn = columnModel.getColumn(4);
        effectiveColumn.setPreferredWidth(55);
        effectiveColumn.setMinWidth(55);
        effectiveColumn.setMaxWidth(55);
        effectiveColumn.setResizable(false);

        table.getSelectionModel().addListSelectionListener(new ClassesTableSelectionHandler());

        tableBaseSetup(table);

        return table;
    }

    private JTable prepareSetupsTable(AbstractTableModel model) {
        JTable table = new JTable(model);

        TableColumnModel columnModel = table.getColumnModel();

        TableColumn selectionColumn = columnModel.getColumn(0);
        selectionColumn.setPreferredWidth(40);
        selectionColumn.setMinWidth(40);
        selectionColumn.setMaxWidth(60);
        selectionColumn.setResizable(false);

        TableColumn versionColumn = columnModel.getColumn(1);
        versionColumn.setPreferredWidth(40);
        versionColumn.setMinWidth(40);
        versionColumn.setMaxWidth(60);
        versionColumn.setResizable(false);

        TableColumn descriptionColumn = columnModel.getColumn(2);
        descriptionColumn.setResizable(true);

        model.addTableModelListener(this);

        tableBaseSetup(table);

        return table;
    }

    private void tableBaseSetup(JTable table) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Font headerFont = table.getTableHeader().getFont();
        headerFont = new Font(headerFont.getName(), Font.BOLD, headerFont.getSize());
        table.getTableHeader().setFont(headerFont);
        table.getTableHeader().setDefaultRenderer(new HeaderCellRenderer(table));

        table.setDefaultRenderer(String.class,
                                 new ProxyCellRenderer(table.getDefaultRenderer(String.class)));
        table.setDefaultRenderer(Integer.class,
                                 new ProxyCellRenderer(table.getDefaultRenderer(Integer.class)));
        table.setDefaultRenderer(Boolean.class,
                                 new ProxyCellRenderer(table.getDefaultRenderer(Boolean.class)));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.setFillsViewportHeight(true);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int column = e.getColumn();
        int row = e.getFirstRow();
        EvoData selectedEvoData = setupsTableModel.getEvoData(row);

        if (column == 0 && selectedEvoData != null) {
            TableModel model = (TableModel) e.getSource();

            boolean isChecked = (Boolean) model.getValueAt(row, column);
            if (isChecked) {
                chartHelper.addEvoSeries(selectedEvoData);
            } else {
                chartHelper.removeEvoSeries(selectedEvoData.getName());
            }
            chartPanel.restoreAutoBounds();
        }
    }

    private class ClassesTableSelectionHandler implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();

            if (!e.getValueIsAdjusting() && !lsm.isSelectionEmpty()) {
                int selection = lsm.getMinSelectionIndex();

                currentClass = classesTableModel.getCutgenClass(selection);
                java.util.List<PaperResult> classResults;
                if (parent.getPapersData() != null
                        && (classResults = parent.getPapersData().get(currentClass)) != null) {
                    chartHelper.updatePapersData(classResults);
                    chartHelper.removeAllEvoSeries();

                    filteredEvoData = applyFilter(currentClass, evoData);
                    setupsTableModel.setData(filteredEvoData);

                    chartPanel.restoreAutoBounds();
                }
            }
        }
    }
}
