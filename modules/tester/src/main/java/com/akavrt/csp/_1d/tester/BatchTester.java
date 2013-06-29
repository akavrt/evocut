package com.akavrt.csp._1d.tester;

import com.akavrt.csp._1d.analyzer.Average;
import com.akavrt.csp._1d.analyzer.xml.XmlEnabledCollector;
import com.akavrt.csp._1d.metrics.MaterialUsage;
import com.akavrt.csp._1d.metrics.SetupCounter;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.MultistartSolver;
import com.akavrt.csp._1d.solver.ProblemClass;
import com.akavrt.csp._1d.solver.ProblemClassProcessor;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: akavrt
 * Date: 29.06.13
 * Time: 00:03
 */
public abstract class BatchTester {
    public static final int DEFAULT_NUMBER_OF_RUNS = 10;
    private final String batchFilePath;
    private final int numberOfRuns;

    protected abstract Algorithm createAlgorithm();

    protected abstract Logger getLogger();

    public BatchTester(String batchFilePath, int numberOfRuns) {
        this.batchFilePath = batchFilePath;
        this.numberOfRuns = numberOfRuns;
    }

    public void process() {
        File batchFile = new File(batchFilePath);
        if (!batchFile.exists() || !batchFile.isFile()) {
            getLogger().info("Batch file '{}' wasn't found.", batchFilePath);
            return;
        }

        List<ProblemClass> batch = read(batchFile);
        if (batch.size() == 0) {
            getLogger().info("Batch is empty.");
        }

        Algorithm method = createAlgorithm();
        MultistartSolver solver = new MultistartSolver(method, numberOfRuns);

        XmlEnabledCollector globalCollector = createGlobalCollector();
        String parentPath = batchFile.getParent();
        for (ProblemClass descriptors : batch) {
            ProblemClassProcessor processor = new ProblemClassProcessor(solver, descriptors);
            processor.process(globalCollector, parentPath);
        }
    }

    private List<ProblemClass> read(File file) {
        List<ProblemClass> batch = Lists.newArrayList();
        try {
            SAXBuilder sax = new SAXBuilder();
            Document document = sax.build(file);
            if (document.hasRootElement()) {
                Element root = document.getRootElement();

                for (Element classElm : root.getChildren(XmlTags.CLASS)) {
                    ProblemClass descriptors = new ProblemClass(classElm);
                    batch.add(descriptors);
                }
            }
        } catch (JDOMException e) {
            getLogger().catching(e);
        } catch (IOException e) {
            getLogger().catching(e);
        }

        return batch;
    }

    private XmlEnabledCollector createGlobalCollector() {
        XmlEnabledCollector collector = new XmlEnabledCollector();
        collector.addMeasure(new Average());

        /*
        collector.addMeasure(new StandardDeviation());
        collector.addMeasure(new MinValue());
        collector.addMeasure(new MaxValue());
        */

        collector.addMetric(new MaterialUsage());
        collector.addMetric(new SetupCounter());

        return collector;
    }

    private interface XmlTags {
        String CLASS = "class";
    }

}
