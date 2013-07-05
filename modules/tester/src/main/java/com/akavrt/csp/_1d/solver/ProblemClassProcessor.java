package com.akavrt.csp._1d.solver;

import com.akavrt.csp._1d.analyzer.xml.RunResultWriter;
import com.akavrt.csp._1d.analyzer.xml.XmlEnabledCollector;
import com.akavrt.csp._1d.core.Problem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * User: akavrt
 * Date: 03.04.13
 * Time: 16:24
 */
public class ProblemClassProcessor {
    private static final Logger LOGGER = LogManager.getLogger(ProblemClassProcessor.class);
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd_HH-mm";
    private static final String FILE_NAME_FORMAT = "csp-1d-class_%s_%s.xml";
    private final MultistartSolver solver;
    private final ProblemClass problemClass;

    public ProblemClassProcessor(MultistartSolver solver, ProblemClass problemClass) {
        this.solver = solver;
        this.problemClass = problemClass;
    }

    public void process(XmlEnabledCollector globalCollector, String parentPath) {
        if (globalCollector == null) {
            return;
        }

        long start = System.currentTimeMillis();
        solver.clearCollectors();

        // reset global collector
        globalCollector.clear();
        solver.addCollector(globalCollector);

        ProblemProvider problemProvider = new ProblemProvider(problemClass);
        int i = 0;
        for (Problem problem : problemProvider) {
            LOGGER.info("Solving problem #{} from class {}", ++i, problemClass.getName());

            solver.setProblem(problem);
            solver.solve();
        }

        long end = System.currentTimeMillis();

        writeResults(globalCollector, parentPath, end - start);
    }

    private boolean writeResults(XmlEnabledCollector globalCollector, String parentPath,
                                 long totalProcessTimeInMillis) {
        File resultsFile = createResultsFile(parentPath);

        try {
            LOGGER.info("Writing run results into '{}'", resultsFile.getPath());

            RunResultWriter writer = new RunResultWriter();

            writer.setProblemClass(problemClass);
            writer.setAlgorithm(solver.getAlgorithm());
            writer.setNumberOfProblemsSolved(problemClass.getSize());
            writer.setNumberOfExecutions(solver.getNumberOfRuns());
            writer.setTotalProcessingTime(totalProcessTimeInMillis);
            writer.setCollector(globalCollector);

            writer.write(resultsFile, true);
        } catch (IOException e) {
            LOGGER.catching(e);
            return false;
        }

        return true;
    }

    private File createResultsFile(String parentPath) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.ENGLISH);
        String fileName = String.format(FILE_NAME_FORMAT,
                                        problemClass.getName(), dateFormat.format(new Date()));

        return new File(parentPath, fileName);
    }

}