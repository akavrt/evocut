package com.akavrt.csp._1d.tester.moea.ui.presets;

import com.akavrt.csp._1d.cutgen.ProblemDescriptors;
import com.akavrt.csp._1d.tester.moea.ui.utils.GBC;
import com.akavrt.csp._1d.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

/**
 * User: akavrt
 * Date: 18.07.13
 * Time: 03:09
 */
public class ProblemPresetsPanel extends BasePresetsPanel {
    private JFormattedTextField problemSizeEdit;
    private JFormattedTextField stockLengthEdit;
    private JFormattedTextField orderLengthLowerBoundEdit;
    private JFormattedTextField orderLengthUpperBoundEdit;
    private JFormattedTextField averageDemandEdit;

    public ProblemPresetsPanel() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(4);

        problemSizeEdit = createIntValueEdit();
        stockLengthEdit = createIntValueEdit();
        orderLengthLowerBoundEdit = createFloatValueEdit(nf);
        orderLengthUpperBoundEdit = createFloatValueEdit(nf);
        averageDemandEdit = createIntValueEdit();

        setLayout(new GridBagLayout());
        add(new JLabel("Problem descriptors"),
            new GBC(0, 0, 2, 1).setFill(GBC.BOTH).setWeight(100, 0).setInsets(10, 10, 10, 10));

        add(new JLabel("size:", SwingConstants.RIGHT),
            new GBC(0, 1).setFill(GBC.BOTH).setWeight(100, 0).setInsets(5, 10, 3, 5));
        add(problemSizeEdit, new GBC(1, 1).setInsets(5, 0, 3, 10));

        add(new JLabel("stock length:", SwingConstants.RIGHT),
            new GBC(0, 2).setFill(GBC.BOTH).setWeight(100, 0).setInsets(3, 10, 3, 5));
        add(stockLengthEdit, new GBC(1, 2).setInsets(3, 0, 3, 10));

        add(new JLabel("order length LB:", SwingConstants.RIGHT),
            new GBC(0, 3).setFill(GBC.BOTH).setWeight(100, 0).setInsets(3, 10, 3, 5));
        add(orderLengthLowerBoundEdit, new GBC(1, 3).setInsets(3, 0, 3, 10));

        add(new JLabel("order length UB:", SwingConstants.RIGHT),
            new GBC(0, 4).setFill(GBC.BOTH).setWeight(100, 0).setInsets(3, 10, 3, 5));
        add(orderLengthUpperBoundEdit, new GBC(1, 4).setInsets(3, 0, 3, 10));

        add(new JLabel("average demand:", SwingConstants.RIGHT),
            new GBC(0, 5).setFill(GBC.BOTH).setWeight(100, 0).setInsets(3, 10, 10, 5));
        add(averageDemandEdit, new GBC(1, 5).setInsets(3, 0, 10, 10));

        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
    }

    public ProblemDescriptors getParameters() {
        if (Utils.isEmpty(problemSizeEdit.getText())
                || Utils.isEmpty(stockLengthEdit.getText())
                || Utils.isEmpty(orderLengthLowerBoundEdit.getText())
                || Utils.isEmpty(orderLengthUpperBoundEdit.getText())
                || Utils.isEmpty(averageDemandEdit.getText())) {
            return null;
        }

        int problemSize = ((Number) problemSizeEdit.getValue()).intValue();
        int stockLength = ((Number) stockLengthEdit.getValue()).intValue();
        double orderLengthLowerBound = ((Number) orderLengthLowerBoundEdit.getValue())
                .doubleValue();
        double orderLengthUpperBound = ((Number) orderLengthUpperBoundEdit.getValue())
                .doubleValue();
        int averageDemand = ((Number) averageDemandEdit.getValue()).intValue();

        ProblemDescriptors descriptors = new ProblemDescriptors(problemSize,
                                                                stockLength,
                                                                orderLengthLowerBound,
                                                                orderLengthUpperBound,
                                                                averageDemand);

        return descriptors;
    }

    public void setParameters(ProblemDescriptors descriptors) {
        if (descriptors == null) {
            problemSizeEdit.setText("");
            stockLengthEdit.setText("");
            orderLengthLowerBoundEdit.setText("");
            orderLengthUpperBoundEdit.setText("");
            averageDemandEdit.setText("");
        } else {
            problemSizeEdit.setValue(descriptors.getSize());
            stockLengthEdit.setValue(descriptors.getStockLength());
            orderLengthLowerBoundEdit.setValue(descriptors.getOrderLengthLowerBound());
            orderLengthUpperBoundEdit.setValue(descriptors.getOrderLengthUpperBound());
            averageDemandEdit.setValue(descriptors.getAverageDemand());
        }
    }

}
