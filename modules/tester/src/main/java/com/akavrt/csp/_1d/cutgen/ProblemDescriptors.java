package com.akavrt.csp._1d.cutgen;

import com.akavrt.csp._1d.xml.XmlCompatible;
import com.akavrt.csp._1d.xml.XmlUtils;
import org.jdom2.Element;

/**
 * <p>Gau and Wascher (1995) introduced five descriptors which could be used to define specific,
 * homogeneous classes of problem instances for 1D SSSCSP.</p>
 *
 * <p>For more detailed explanation refer to the original EJOR paper:</p>
 *
 * <p>Gau, T., and Wascher, G., 1995, CUTGEN1 - a problem generator for the one-dimensional
 * cutting stock problem. European Journal of Operational Research, 84, 572-579.</p>
 *
 * @author Victor Balabanov <akavrt@gmail.com>
 */
public class ProblemDescriptors implements XmlCompatible {
    private final static int DEFAULT_PROBLEM_SIZE = 10;
    private final static int DEFAULT_STOCK_LENGTH = 100;
    private final static double DEFAULT_ORDER_LENGTH_LOWER_BOUND = 0.1;
    private final static double DEFAULT_ORDER_LENGTH_UPPER_BOUND = 0.4;
    private final static int DEFAULT_AVERAGE_DEMAND = 10;
    private int size;
    private int stockLength;
    private double orderLengthLowerBound;
    private double orderLengthUpperBound;
    private int averageDemand;

    public ProblemDescriptors(Element element) {
        load(element);
    }

    public ProblemDescriptors(int size, int stockLength,
                              double orderLengthLowerBound, double orderLengthUpperBound,
                              int averageDemand) {
        this.size = size;
        this.stockLength = stockLength;
        this.orderLengthLowerBound = orderLengthLowerBound;
        this.orderLengthUpperBound = orderLengthUpperBound;
        this.averageDemand = averageDemand;
    }

    /**
     * <p>Size of the given instance of 1D SSSCSP is defined as the number of different order
     * lengths.</p>
     *
     * <p>In notation taken by Gau and Wascher (1995): m.</p>
     *
     * @return Size of the problem.
     */
    public int getSize() {
        return size;
    }

    /**
     * <p>Standard size of the stock items available in an unlimited supply.</p>
     *
     * <p>In notation taken by Gau and Wascher (1995): L.</p>
     *
     * @return Size of the stock items, specified by an integer number.
     */
    public int getStockLength() {
        return stockLength;
    }

    /**
     * <p>Lower bound for the relative size of order lengths in relation to the standard size of
     * the stock items.</p>
     *
     * <p>In notation taken by Gau and Wascher (1995): v_1.</p>
     *
     * <p>l_i >= v_1 * L for all i = 1, ..., m, where l_i - length of the i-th order.</p>
     *
     * @return Lower bound for the relative size of order lengths specified as a fractional ratio.
     */
    public double getOrderLengthLowerBound() {
        return orderLengthLowerBound;
    }

    /**
     * <p>Upper bound for the relative size of order lengths in relation to the standard size of
     * the stock items.</p>
     *
     * <p>In notation taken by Gau and Wascher (1995): v_2.</p>
     *
     * <p>l_i <= v_2 * L for all i = 1, ..., m, where l_i - length of the i-th order.</p>
     *
     * <p>0 < v_1 < v_2 <= 1.</p>
     *
     * @return Upper bound for the relative size of order lengths specified as a fractional ratio.
     */
    public double getOrderLengthUpperBound() {
        return orderLengthUpperBound;
    }

    /**
     * <p>Average demand per order length.</p>
     *
     * <p>In notation taken by Gau and Wascher (1995): d.</p>
     *
     * <p>Total demand distributed between the different orders is D = m * d.</p>
     *
     * @return Average demand per order length.
     */
    public int getAverageDemand() {
        return averageDemand;
    }

    @Override
    public Element save() {
        Element descriptorsElm = new Element(XmlTags.DESCRIPTORS);

        Element problemSizeElm = new Element(XmlTags.PROBLEM_SIZE);
        problemSizeElm.setText(Integer.toString(size));
        descriptorsElm.addContent(problemSizeElm);

        Element stockLengthElm = new Element(XmlTags.STOCK_LENGTH);
        stockLengthElm.setText(Integer.toString(stockLength));
        descriptorsElm.addContent(stockLengthElm);

        Element orderLengthLowerBoundElm = new Element(XmlTags.ORDER_LENGTH_LOWER_BOUND);
        orderLengthLowerBoundElm.setText(XmlUtils.formatDouble(orderLengthLowerBound));
        descriptorsElm.addContent(orderLengthLowerBoundElm);

        Element orderLengthUpperBoundElm = new Element(XmlTags.ORDER_LENGTH_UPPER_BOUND);
        orderLengthUpperBoundElm.setText(XmlUtils.formatDouble(orderLengthUpperBound));
        descriptorsElm.addContent(orderLengthUpperBoundElm);

        Element averageDemandElm = new Element(XmlTags.AVERAGE_DEMAND);
        averageDemandElm.setText(Integer.toString(averageDemand));
        descriptorsElm.addContent(averageDemandElm);

        return descriptorsElm;
    }

    @Override
    public void load(Element rootElm) {
        Element problemSizeElm = rootElm.getChild(XmlTags.PROBLEM_SIZE);
        size = extractInteger(problemSizeElm, DEFAULT_PROBLEM_SIZE);

        Element stockLengthElm = rootElm.getChild(XmlTags.STOCK_LENGTH);
        stockLength = extractInteger(stockLengthElm, DEFAULT_STOCK_LENGTH);

        Element orderLengthLowerBoundElm = rootElm.getChild(XmlTags.ORDER_LENGTH_LOWER_BOUND);
        orderLengthLowerBound = extractDouble(orderLengthLowerBoundElm,
                                              DEFAULT_ORDER_LENGTH_LOWER_BOUND);

        Element orderLengthUpperBoundElm = rootElm.getChild(XmlTags.ORDER_LENGTH_UPPER_BOUND);
        orderLengthUpperBound = extractDouble(orderLengthUpperBoundElm, DEFAULT_ORDER_LENGTH_UPPER_BOUND);

        Element averageDemandElm = rootElm.getChild(XmlTags.AVERAGE_DEMAND);
        averageDemand = extractInteger(averageDemandElm, DEFAULT_AVERAGE_DEMAND);
    }

    private int extractInteger(Element element, int defaultValue) {
        if (element == null) {
            return defaultValue;
        }

        return XmlUtils.getIntegerFromText(element, defaultValue);
    }

    private double extractDouble(Element element, double defaultValue) {
        if (element == null) {
            return defaultValue;
        }

        return XmlUtils.getDoubleFromText(element, defaultValue);
    }

    private interface XmlTags {
        String DESCRIPTORS = "descriptors";
        String PROBLEM_SIZE = "problem-size";
        String STOCK_LENGTH = "stock-length";
        String ORDER_LENGTH_LOWER_BOUND = "order-length-lb";
        String ORDER_LENGTH_UPPER_BOUND = "order-length-ub";
        String AVERAGE_DEMAND = "average-demand";
    }

}
