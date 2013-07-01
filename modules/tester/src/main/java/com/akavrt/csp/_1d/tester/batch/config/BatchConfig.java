package com.akavrt.csp._1d.tester.batch.config;

import com.akavrt.csp._1d.tester.batch.BatchTester;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BatchConfig {
    private static final Logger LOGGER = LogManager.getLogger(BatchConfig.class);
    private String batchFilePath;
    private int numberOfRuns;
    private boolean isLoaded;

    public BatchConfig(String[] args) {
        parseParameters(args);
    }

    protected abstract void parseAlgorithmSpecificParameters(String[] args);

    public abstract void printHelp();

    protected void parseParameters(String[] args) {
        parseBatchParameters(args);
        parseAlgorithmSpecificParameters(args);

        isLoaded = true;
    }

    private void parseBatchParameters(String[] args) {
        batchFilePath = parseBatchFilePath(args);
        numberOfRuns = parseNumberOfRuns(args, BatchTester.DEFAULT_NUMBER_OF_RUNS);
    }

    private String parseBatchFilePath(String[] args) {
        return args.length > 0 ? args[0] : null;
    }

    private int parseNumberOfRuns(String[] args, int defaultValue) {
        int runs = defaultValue;
        try {
            runs = Integer.parseInt(args[1]);
        } catch (Exception e) {
            LOGGER.info("Can't find optional parameter 'number of runs', " +
                                "the default value of {} will be used.",
                        BatchTester.DEFAULT_NUMBER_OF_RUNS);
        }

        return runs;
    }

    public String getBatchFilePath() {
        return batchFilePath;
    }

    public int getNumberOfRuns() {
        return numberOfRuns;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

}
