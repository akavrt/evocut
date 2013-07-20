package com.akavrt.csp._1d.tester.moea.ui.content;

import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.solver.moea.Chromosome;
import com.akavrt.csp._1d.tester.moea.ui.MoeaProgressUpdate;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.util.List;

/**
 * User: akavrt
 * Date: 09.04.13
 * Time: 15:01
 */
public class ContentPanel extends JTabbedPane {
    private final TextTracePanel textTracePanel;
    private final GraphTracePanel graphTracePanel;
    private final ProblemPanel problemPanel;

    public ContentPanel() {
        textTracePanel = new TextTracePanel();
        graphTracePanel = new GraphTracePanel();
        problemPanel = new ProblemPanel();

        addTab("text trace", textTracePanel);
        addTab("graph trace", graphTracePanel);
        addTab("problem", problemPanel);

        setTabPlacement(JTabbedPane.BOTTOM);
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        setUI(new BasicTabbedPaneUI() {
            @Override
            protected Insets getContentBorderInsets(int tabPlacement) {
                return new Insets(0, 0, 0, 0);
            }

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            }

            @Override
            protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects,
                                               int tabIndex, Rectangle iconRect,
                                               Rectangle textRect, boolean isSelected) {
            }
        });

        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
    }

    public void clearTextArea() {
        textTracePanel.clearTextArea();
    }

    public void appendText(String text) {
        textTracePanel.appendText(text);
    }

    public void clearGraph() {
        graphTracePanel.clearData();
    }

    public void updateGraph(MoeaProgressUpdate update) {
        graphTracePanel.updateData(update);
    }

    public void setProblem(Problem problem) {
        problemPanel.setProblem(problem);
    }

    public void clearProblem() {
        problemPanel.clearProblem();
    }

    public void setSolutions(List<Chromosome> solutions) {
        problemPanel.setSolutions(solutions);
    }

    public void clearSolutions() {
        problemPanel.clearSolutions();
    }

    public void setFocus() {
        textTracePanel.setFocus();
    }

}
