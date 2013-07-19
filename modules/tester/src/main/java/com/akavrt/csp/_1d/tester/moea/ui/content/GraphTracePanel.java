package com.akavrt.csp._1d.tester.moea.ui.content;

import com.akavrt.csp._1d.solver.moea.Chromosome;
import com.akavrt.csp._1d.tester.moea.ui.MoeaProgressUpdate;
import com.akavrt.csp._1d.tester.moea.ui.utils.GBC;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * User: akavrt
 * Date: 19.07.13
 * Time: 02:55
 */
public class GraphTracePanel extends JPanel {
    private XYSeries feasibleSeries;
    private XYSeries infeasibleSeries;
    private XYSeries paretoSeries;

    public GraphTracePanel() {
        setupViews();

        setBackground(Color.white);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
    }

    public void clearData() {
        feasibleSeries.clear();
        infeasibleSeries.clear();
        paretoSeries.clear();
    }

    public void updateData(MoeaProgressUpdate data) {
        clearData();

        if (data == null) {
            return;
        }

        for (Chromosome chromosome : data.getParetoFront()) {
            paretoSeries.add(chromosome.getObjective(1), chromosome.getObjective(0));
        }

        for (Chromosome chromosome : data.getInfeasibleSolutions()) {
            infeasibleSeries.add(chromosome.getObjective(1), chromosome.getObjective(0));
        }

        for (Chromosome chromosome : data.getFeasibleSolutions()) {
            feasibleSeries.add(chromosome.getObjective(1), chromosome.getObjective(0));
        }
    }

    private void setupViews() {
        JFreeChart chart = createChart();
        ChartPanel chartPanel = new ChartPanel(chart, true, true, false, true, false);

        setLayout(new GridBagLayout());

        add(chartPanel, new GBC(0, 0).setWeight(100, 100).setFill(GBC.BOTH).setInsets(10, 10, 10,
                                                                                      10));
    }

    private JFreeChart createChart() {
        feasibleSeries = new XYSeries("feasible solutions");
        infeasibleSeries = new XYSeries("infeasible solutions");
        paretoSeries = new XYSeries("pareto front (approximation)");

        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(feasibleSeries);
        dataset.addSeries(infeasibleSeries);
        dataset.addSeries(paretoSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", // title
                                                          "setups", // x axis (domain) label
                                                          "material usage", // y axis (range) label
                                                          dataset, // data
                                                          PlotOrientation.VERTICAL,
                                                          true, // include legend
                                                          true, // tooltips
                                                          false // urls
        );

        chart.setBackgroundPaint(Color.white);


        setupPlot(chart);
        setupAxes(chart);
        setupLegend(chart);

        setupSeriesLines(chart);
        setupSeriesShapes(chart);

        return chart;
    }

    private void setupPlot(JFreeChart chart) {
        XYPlot plot = (XYPlot) chart.getPlot();

        plot.setBackgroundPaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setOutlineVisible(false);
    }

    private void setupAxes(JFreeChart chart) {
        XYPlot plot = (XYPlot) chart.getPlot();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelFont(createAxisFont());
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setLabelFont(createAxisLabelFont());

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setTickLabelFont(createAxisFont());
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis.setLabelFont(createAxisLabelFont());
    }

    private void setupLegend(JFreeChart chart) {
        LegendTitle legend = chart.getLegend();
        legend.setFrame(BlockBorder.NONE);
        legend.setItemFont(createLegendFont());
        legend.setPosition(RectangleEdge.BOTTOM);
        legend.setMargin(10, 0, 10, 0);
    }

    private void setupSeriesLines(JFreeChart chart) {
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

        renderer.setDrawOutlines(true);
        renderer.setUseFillPaint(true);
        renderer.setBaseFillPaint(Color.white);

        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesLinesVisible(2, true);

        renderer.setSeriesOutlineStroke(0, new BasicStroke(1.2f));
        renderer.setSeriesOutlineStroke(1, new BasicStroke(1.2f));
        renderer.setSeriesStroke(2, new BasicStroke(1.2f));


        renderer.setSeriesPaint(0, new Color(153, 204, 0));
        renderer.setSeriesPaint(1, new Color(255, 68, 68));
        renderer.setSeriesPaint(2, new Color(51, 181, 229));
    }

    private void setupSeriesShapes(JFreeChart chart) {
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);

        renderer.setSeriesShape(0, new Ellipse2D.Double(-4, -4, 8, 8));
        renderer.setSeriesShape(1, new Ellipse2D.Double(-4, -4, 8, 8));

        renderer.setSeriesShapesFilled(0, true);
        renderer.setSeriesShapesFilled(1, true);

        renderer.setSeriesFillPaint(0, Color.white);
        renderer.setSeriesFillPaint(1, Color.white);
    }

    private Font createAxisFont() {
        return new Font(Font.SANS_SERIF, Font.PLAIN, 9);
    }

    private Font createLegendFont() {
        return new Font(Font.SANS_SERIF, Font.PLAIN, 11);
    }

    private Font createAxisLabelFont() {
        return new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    }

}
