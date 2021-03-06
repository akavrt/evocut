package com.akavrt.csp._1d.tester.batch.config;

import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.solver.sequential.VahrenkampProcedureParameters;
import com.akavrt.csp._1d.tester.batch.BatchTester;
import com.akavrt.csp._1d.tester.params.PatternGeneratorParametersReader;
import com.akavrt.csp._1d.tester.params.VahrenkampProcedureParametersReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * User: akavrt
 * Date: 21.05.13
 * Time: 20:38
 */
public class VahrenkampBatchConfig extends BatchConfig {
    private static final Logger LOGGER = LogManager.getLogger(VahrenkampBatchConfig.class);
    private VahrenkampProcedureParameters procedureParameters;
    private PatternGeneratorParameters patternParameters;

    public VahrenkampBatchConfig(String[] args) {
        super(args);
    }

    @Override
    public void printHelp() {
        System.out.println("You can run batch using this command:");
        System.out.println("    batch_file [runs] [shp_params] [pgp_params]");
        System.out.println("\n  Where");
        System.out.println("    batch_file - absolute path to the file with batch description.");
        System.out.println("    runs       - the number to run algorithm for each problem; optional");
        System.out.println("                 parameter, if not specified the default value of " + BatchTester.DEFAULT_NUMBER_OF_RUNS + " is");
        System.out.println("                 used;");
        System.out.println("    shp_params - absolute path to the XML file with parameters of");
        System.out.println("                 sequential heuristic procedure; optional parameter, if");
        System.out.println("                 not specified the default set of parameters is used;");
        System.out.println("    pgp_params - absolute path to the XML file with parameters of");
        System.out.println("                 pattern generation procedure; optional parameter,");
        System.out.println("                 if not specified the default set of parameters is used.");
    }

    @Override
    protected void parseAlgorithmSpecificParameters(String[] args) {
        parseSequentialProcedureParameters(args);
        parsePatternGeneratorParameters(args);
    }

    private void parseSequentialProcedureParameters(String[] args) {
        try {
            String path = args[2];
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                procedureParameters = new VahrenkampProcedureParametersReader().read(file);
            }
        } catch (Exception e) {
            LOGGER.info("Can't find parameters of sequential heuristic procedure, " +
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

    public VahrenkampProcedureParameters getAlgorithmParameters() {
        return procedureParameters;
    }

    public PatternGeneratorParameters getPatternParameters() {
        return patternParameters;
    }

}
