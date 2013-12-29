package com.akavrt.csp._1d.tester.batch.config;

import com.akavrt.csp._1d.metrics.ConstraintAwareMetricParameters;
import com.akavrt.csp._1d.solver.evo.es.EvolutionStrategyParameters;
import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.tester.batch.BatchTester;
import com.akavrt.csp._1d.tester.params.ConstraintAwareMetricParametersReader;
import com.akavrt.csp._1d.tester.params.EvolutionStrategyParametersReader;
import com.akavrt.csp._1d.tester.params.PatternGeneratorParametersReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * User: akavrt
 * Date: 21.05.13
 * Time: 21:16
 */
public class StrategyBatchConfig extends BatchConfig {
    private static final Logger LOGGER = LogManager.getLogger(StrategyBatchConfig.class);
    private EvolutionStrategyParameters strategyParameters;
    private PatternGeneratorParameters patternParameters;
    private ConstraintAwareMetricParameters constrainedMetricParameters;

    public StrategyBatchConfig(String[] args) {
        super(args);
    }

    @Override
    public void printHelp() {
        System.out.println("You can run batch using this command:");
        System.out.println("    batch_file [runs] [es_params] [pgp_params] [of_params]");
        System.out.println("\n  Where");
        System.out.println("    batch_file - absolute path to the file with batch description.");
        System.out.println("    runs       - the number to run algorithm for each problem, optional");
        System.out.println("                 parameter, if not specified the default value of " + BatchTester.DEFAULT_NUMBER_OF_RUNS + " is");
        System.out.println("                 used;");
        System.out.println("    es_params  - absolute path to the XML file with parameters of");
        System.out.println("                 evolution strategy, optional parameter, if not");
        System.out.println("                 specified the default set of parameters is used;");
        System.out.println("    pgp_params - absolute path to the XML file with parameters of");
        System.out.println("                 pattern generation procedure, optional parameter,");
        System.out.println("                 if not specified the default set of parameters is used;");
        System.out.println("    of_params  - absolute path to the XML file with parameters of");
        System.out.println("                 objective function (weights used in linear scalar),");
        System.out.println("                 optional parameter, if not specified the default set");
        System.out.println("                 of parameters is used.");
    }

    @Override
    protected void parseAlgorithmSpecificParameters(String[] args) {
        parseEvolutionStrategyParameters(args);
        parsePatternGeneratorParameters(args);
        parseObjectiveFunctionParameters(args);
    }

    private void parseEvolutionStrategyParameters(String[] args) {
        try {
            String path = args[2];
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                strategyParameters = new EvolutionStrategyParametersReader().read(file);
            }
        } catch (Exception e) {
            LOGGER.info("Can't find parameters of evolution strategy, " +
                                "the default set of parameters will be used.");
        }
    }

    private void parsePatternGeneratorParameters(String[] args) {
        try {
            String path = args[3];
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                patternParameters = new PatternGeneratorParametersReader().read(file);
            }
        } catch (Exception e) {
            LOGGER.info("Can't find parameters of pattern generator procedure, " +
                                "the default set of parameters will be used.");
        }
    }

    private void parseObjectiveFunctionParameters(String[] args) {
        try {
            String path = args[4];
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                constrainedMetricParameters = new ConstraintAwareMetricParametersReader().read(file);
            }
        } catch (Exception e) {
            LOGGER.info("Can't find parameters of objective function, the default set of parameters will be used.");
        }
    }

    public EvolutionStrategyParameters getAlgorithmParameters() {
        return strategyParameters;
    }

    public PatternGeneratorParameters getPatternParameters() {
        return patternParameters;
    }

    public ConstraintAwareMetricParameters getObjectiveFunctionParameters() {
        return constrainedMetricParameters;
    }

}
