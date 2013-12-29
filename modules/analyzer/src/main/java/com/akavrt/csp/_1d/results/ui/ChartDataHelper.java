package com.akavrt.csp._1d.results.ui;

import com.akavrt.csp._1d.results.EvoResult;
import com.akavrt.csp._1d.results.PaperResult;
import com.akavrt.csp._1d.results.ResultUtils;
import com.akavrt.csp._1d.results.ui.table.EvoData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

/**
 * User: akavrt
 * Date: 27.12.13
 * Time: 00:19
 */
public class ChartDataHelper {
    private static final String TOOL_TIP_FORMAT = "{0}: ({1}, {2})";
    private static final Color[] EVO_COLORS;

    static {
        EVO_COLORS = new Color[8];
        EVO_COLORS[0] = new Color(170, 102, 204);
        EVO_COLORS[1] = new Color(153, 204, 0);
        EVO_COLORS[2] = new Color(255, 187, 51);
        EVO_COLORS[3] = new Color(255, 68, 68);
        EVO_COLORS[4] = new Color(153, 51, 204);
        EVO_COLORS[5] = new Color(102, 153, 0);
        EVO_COLORS[6] = new Color(255, 136, 0);
        EVO_COLORS[7] = new Color(204, 0, 0);
    }

    private final java.util.List<String> resultsTooltips;
    private final java.util.List<String> effectiveTooltips;
    private final Map<Comparable, java.util.List<String>> evoTooltips;
    private final EvoTooltipGenerator evoTooltipGenerator;
    private final Stroke lineSeriesStroke;
    private final Shape evoShape;
    private JFreeChart chart;
    private XYSeriesCollection dataset;
    private XYSeries resultsSeries;
    private XYSeries effectiveSeries;

    public ChartDataHelper() {
        resultsTooltips = Lists.newArrayList();
        effectiveTooltips = Lists.newArrayList();

        evoTooltips = Maps.newHashMap();
        evoTooltipGenerator = new EvoTooltipGenerator();

        lineSeriesStroke = new BasicStroke(1.2f);
        evoShape = new Rectangle2D.Double(-4, -4, 8, 8);
    }

    public void clearPapersData() {
        resultsTooltips.clear();
        effectiveTooltips.clear();

        resultsSeries.clear();
        effectiveSeries.clear();
    }

    public void updatePapersData(java.util.List<PaperResult> classResults) {
        clearPapersData();

        if (classResults != null) {
            for (PaperResult result : classResults) {
                String tooltip = String.format(" %s-%d", result.getSource(), result.getSetup());

                if (result.isDominated()) {
                    resultsSeries.add(result.getMaterial(), result.getPatterns());
                    resultsTooltips.add(tooltip);
                } else {
                    effectiveSeries.add(result.getMaterial(), result.getPatterns());
                    effectiveTooltips.add(tooltip);
                }
            }
        }
    }

    public void addEvoSeries(EvoData data) {
        if (evoTooltips.containsKey(data.getName())) {
            return;
        }

        XYSeries series = new XYSeries(data.getName(), false);
        dataset.addSeries(series);

        java.util.List<String> tooltips = Lists.newArrayList();
        for (EvoResult result : data.getResults()) {
            if (!result.isDominated()) {
                series.add(result.getMaterial(), result.getPatterns());
                String tooltip = String.format(" C1 = %s, C2 = %s",
                                               formatDouble(result.getMaterialWeight()),
                                               formatDouble(result.getPatternsWeight()));
                tooltips.add(tooltip);
            }
        }
        evoTooltips.put(data.getName(), tooltips);

        int index = dataset.getSeriesCount() - 1;
        setupEvoSeries(index);
    }

    public void removeEvoSeries(String modName) {
        if (!evoTooltips.containsKey(modName)) {
            return;
        }

        int index = dataset.getSeriesIndex(modName);
        if (index >= 0) {
            dataset.removeSeries(index);
            evoTooltips.remove(modName);
        }
    }

    public void removeAllEvoSeries() {
        for (Comparable key : evoTooltips.keySet()) {
            int index = dataset.getSeriesIndex(key);
            if (index >= 0) {
                dataset.removeSeries(index);
            }
        }

        evoTooltips.clear();
    }

    private void setupEvoSeries(int index) {
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

        renderer.setSeriesLinesVisible(index, true);

        renderer.setSeriesStroke(index, lineSeriesStroke);
        int colorIndex = (evoTooltips.size() - 1) % EVO_COLORS.length;
        renderer.setSeriesPaint(index, EVO_COLORS[colorIndex]);

        renderer.setSeriesShapesVisible(index, true);
        renderer.setSeriesShape(index, evoShape);
        renderer.setSeriesShapesFilled(index, true);
        renderer.setSeriesFillPaint(index, Color.white);
        renderer.setSeriesToolTipGenerator(index, evoTooltipGenerator);
    }

    public JFreeChart createChart() {
        resultsSeries = new XYSeries("previous results", false);
        effectiveSeries = new XYSeries("best previous results", false);

        dataset = new XYSeriesCollection();

        dataset.addSeries(resultsSeries);
        dataset.addSeries(effectiveSeries);

        chart = ChartFactory.createXYLineChart("", // title
                                               "material usage", // x axis (domain) label
                                               "patterns", // y axis (range) label
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
        setupTooltips(chart);

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
        rangeAxis.setLabelInsets(new RectangleInsets(0, 0, 0, 10));
        setupAxis(rangeAxis);

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setLabelInsets(new RectangleInsets(10, 0, 0, 0));
        setupAxis(domainAxis);
    }

    private void setupAxis(NumberAxis axis) {
        axis.setTickLabelFont(createAxisFont());
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        axis.setLabelFont(createAxisLabelFont());
        axis.setAutoRange(true);
        axis.setAutoRangeIncludesZero(false);
    }

    private void setupLegend(JFreeChart chart) {
        LegendTitle legend = chart.getLegend();
        legend.setFrame(BlockBorder.NONE);
        legend.setItemFont(createLegendFont());
        legend.setPosition(RectangleEdge.TOP);
        legend.setMargin(0, 0, 10, 0);
    }

    private void setupTooltips(JFreeChart chart) {
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

        PapersTooltipGenerator tooltipGenerator = new PapersTooltipGenerator();
        renderer.setSeriesToolTipGenerator(0, tooltipGenerator);
        renderer.setSeriesToolTipGenerator(1, tooltipGenerator);
    }

    private void setupSeriesLines(JFreeChart chart) {
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

        renderer.setDrawOutlines(true);
        renderer.setUseFillPaint(true);
        renderer.setBaseFillPaint(Color.white);

        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesLinesVisible(1, true);

        renderer.setSeriesOutlineStroke(0, lineSeriesStroke);
        renderer.setSeriesStroke(1, lineSeriesStroke);

        renderer.setSeriesPaint(0, new Color(138, 213, 240));
        renderer.setSeriesPaint(1, new Color(0, 153, 204));
    }

    private void setupSeriesShapes(JFreeChart chart) {
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);

        Shape existingDataShape = new Ellipse2D.Double(-4, -4, 8, 8);
        renderer.setSeriesShape(0, existingDataShape);
        renderer.setSeriesShape(1, existingDataShape);

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

    private String formatDouble(double value) {
        return ResultUtils.DECIMAL_FORMAT.format(value);
    }

    private class PapersTooltipGenerator extends StandardXYToolTipGenerator {

        public PapersTooltipGenerator() {
            super(TOOL_TIP_FORMAT, ResultUtils.DECIMAL_FORMAT, ResultUtils.DECIMAL_FORMAT);
        }

        @Override
        public String generateToolTip(XYDataset dataset, int series, int item) {
            return super.generateLabelString(dataset, series, item)
                    + (series == 0 ? resultsTooltips : effectiveTooltips).get(item);
        }
    }

    private class EvoTooltipGenerator extends StandardXYToolTipGenerator {

        public EvoTooltipGenerator() {
            super(TOOL_TIP_FORMAT, ResultUtils.DECIMAL_FORMAT, ResultUtils.DECIMAL_FORMAT);
        }

        @Override
        public String generateToolTip(XYDataset dataset, int series, int item) {
            String tooltip = "";
            Comparable key = dataset.getSeriesKey(series);
            java.util.List<String> tooltips = evoTooltips.get(key);
            if (tooltips != null && item < tooltips.size()) {
                tooltip = tooltips.get(item);
            }

            return super.generateLabelString(dataset, series, item) + tooltip;
        }
    }
}
