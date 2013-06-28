package com.akavrt.csp._1d.analyzer.xml;

import com.akavrt.csp._1d.analyzer.Measure;
import com.akavrt.csp._1d.analyzer.SimpleCollector;
import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.metrics.Metric;
import com.akavrt.csp._1d.xml.XmlUtils;
import org.jdom2.Element;

import java.util.List;

/**
 * <p>This implementation of the Collector interface should be used to collect data in test runs,
 * prepare measures and convert calculated values into XML.</p>
 */
public class XmlEnabledCollector extends SimpleCollector {
    private Element result;

    @Override
    public void process() {
        if (solutions.size() == 0) {
            return;
        }

        Element rootElm = new Element(XmlTags.METRICS);

        // process solution-specific metrics
        for (Metric metric : metrics) {
            Element metricElm = new Element(XmlTags.METRIC);
            rootElm.addContent(metricElm);

            Element nameElm = new Element(XmlTags.NAME);
            nameElm.setText(metric.name());
            metricElm.addContent(nameElm);

            for (Measure measure : measures) {
                double value = measure.calculate(solutions, metric);

                Element measureElm = new Element(measure.name());
                measureElm.setText(XmlUtils.formatDouble(value));
                metricElm.addContent(measureElm);
            }
        }

        Element timeMetricElm = processExecutionTime(measures, executionTimeInMillis);
        rootElm.addContent(timeMetricElm);

        Element feasibilityRatioElm = calculateFeasibilityRatio(solutions);
        rootElm.addContent(feasibilityRatioElm);

        result = rootElm;
    }

    private Element processExecutionTime(List<Measure> measures, List<Long> executionTimeInMillis) {
        Element metricElm = new Element(XmlTags.METRIC);

        Element nameElm = new Element(XmlTags.NAME);
        nameElm.setText("Execution time in milliseconds");
        metricElm.addContent(nameElm);
        for (Measure measure : measures) {
            double value = measure.calculate(executionTimeInMillis);

            Element measureElm = new Element(measure.name());
            measureElm.setText(XmlUtils.formatDouble(value));
            metricElm.addContent(measureElm);
        }

        return metricElm;
    }

    private Element calculateFeasibilityRatio(List<Plan> solutions) {
        int feasible = 0;
        for (Plan solution : solutions) {
            if (solution.isFeasible()) {
                feasible++;
            }
        }
        double feasibilityRatio = 100 * feasible / (double) solutions.size();

        Element metricElm = new Element(XmlTags.METRIC);

        Element nameElm = new Element(XmlTags.NAME);
        nameElm.setText("Feasibility ratio");
        metricElm.addContent(nameElm);

        Element valueElm = new Element(XmlTags.VALUE);
        valueElm.setAttribute(XmlTags.UNIT, XmlTags.PERCENTS);
        valueElm.setText(XmlUtils.formatDouble(feasibilityRatio));
        metricElm.addContent(valueElm);

        return metricElm;
    }

    public Element getResult() {
        return result;
    }

    private interface XmlTags {
        String METRICS = "metrics";
        String METRIC = "metric";
        String NAME = "name";
        String UNIT = "unit";
        String PERCENTS = "%";
        String VALUE = "value";
    }

}
