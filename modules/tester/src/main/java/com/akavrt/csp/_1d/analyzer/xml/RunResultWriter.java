package com.akavrt.csp._1d.analyzer.xml;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.ProblemClass;
import com.akavrt.csp._1d.utils.ParameterSet;
import com.akavrt.csp._1d.xml.XmlUtils;
import com.akavrt.csp._1d.xml.XmlWriter;
import com.google.common.collect.Lists;
import org.jdom2.Element;

import java.util.Date;
import java.util.List;

/**
 * <p>Results of the test run could be converted to XML and written into stream or file. This
 * utility class helps to accomplish these tasks.</p>
 *
 * @author Victor Balabanov <akavrt@gmail.com>
 */
public class RunResultWriter extends XmlWriter {
    private Problem problem;
    private ProblemClass problemClass;
    private List<Plan> solutions;
    private Algorithm algorithm;
    private int numberOfExecutions;
    private int numberOfProblemsSolved;
    private long totalProcessingTimeInMillis;
    private XmlEnabledCollector collector;

    /**
     * <p>Create empty instance of RunResultWriter. To fill in data fields use setter methods.</p>
     */
    public RunResultWriter() {
        solutions = Lists.newArrayList();
    }

    /**
     * <p>Set problem which will be converted to XML.</p>
     *
     * @param problem The Problem to convert.
     */
    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    /**
     * <p>Set description of the problem class which will be converted to XML.</p>
     *
     * @param problemClass The ProblemClass to convert.
     */
    public void setProblemClass(ProblemClass problemClass) {
        this.problemClass = problemClass;
    }

    /**
     * <p>Set list of solutions which will be converted to XML.</p>
     *
     * @param solutions The list of solutions to convert.
     */
    public void setPlans(List<Plan> solutions) {
        this.solutions.clear();
        this.solutions.addAll(solutions);
    }

    /**
     * <p>Add solution to the list of solutions which will be converted to XML.</p>
     *
     * @param solution The solution to convert.
     */
    public void addPlan(Plan solution) {
        solutions.add(solution);
    }

    /**
     * <p>Set method used to solve problem.</p>
     *
     * @param algorithm Plan method.
     */
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * <p>Set number of times the algorithm was executed in test run.</p>
     *
     * @param numberOfExecutions Number of times the algorithm was executed.
     */
    public void setNumberOfExecutions(int numberOfExecutions) {
        this.numberOfExecutions = numberOfExecutions;
    }

    /**
     * <p>Set number of problems solved in test run.</p>
     *
     * <p>We can run same algorithm to solve different problems (representing a class of problems
     * with similar characteristics, for example). In this case it's useful to store the number of
     * problems solved in test run.</p>
     *
     * @param numberOfProblemsSolved Number of problems solved in test run.
     */
    public void setNumberOfProblemsSolved(int numberOfProblemsSolved) {
        this.numberOfProblemsSolved = numberOfProblemsSolved;
    }

    /**
     * <p>Set total running time. Used in batch processing.</p>
     *
     * <p>We can run same algorithm to solve different problems (representing a class of problems
     * with similar characteristics, for example). In this case it's useful to store running times
     * for later analysis of the method efficiency.</p>
     *
     * @param timeInMillis Total running time in milliseconds.
     */
    public void setTotalProcessingTime(long timeInMillis) {
        this.totalProcessingTimeInMillis = timeInMillis;
    }

    /**
     * <p>Set an instance of collector used to collect and prepare run results.</p>
     *
     * @param collector Collector used to collect and prepare run results.
     */
    public void setCollector(XmlEnabledCollector collector) {
        this.collector = collector;
    }

    /**
     * <p>Convert run results along with problem definition and list of solution into XML
     * represented as an instance of org.jdom2.Document.</p>
     *
     * @return Run results converted to XML.
     */
    public Element convert() {
        Element runElm = new Element(XmlTags.RUN);

        Element dateElm = new Element(XmlTags.DATE);
        dateElm.setText(XmlUtils.formatDate(new Date()));
        runElm.addContent(dateElm);

        if (problemClass != null) {
            Element problemClassElm = problemClass.save();
            runElm.addContent(problemClassElm);
        }

        if (algorithm != null) {
            Element methodElm = new Element(XmlTags.METHOD);
            runElm.addContent(methodElm);

            Element nameElm = new Element(XmlTags.NAME);
            nameElm.setText(algorithm.name());
            methodElm.addContent(nameElm);

            if (algorithm.getParameters().size() > 0) {
                Element parametersElm = new Element(XmlTags.PARAMETERS);
                methodElm.addContent(parametersElm);

                for (ParameterSet parameters : algorithm.getParameters()) {
                    parametersElm.addContent(parameters.save());
                }
            }
        }

        if (numberOfProblemsSolved > 0) {
            Element executionsElm = new Element(XmlTags.PROBLEMS);
            executionsElm.setText(Integer.toString(numberOfProblemsSolved));
            runElm.addContent(executionsElm);
        }

        if (numberOfExecutions > 0) {
            Element executionsElm = new Element(XmlTags.EXECUTIONS);
            executionsElm.setText(Integer.toString(numberOfExecutions));
            runElm.addContent(executionsElm);
        }

        if (totalProcessingTimeInMillis > 0) {
            Element processingTimeElm = new Element(XmlTags.PROCESS_TIME);
            processingTimeElm.setAttribute(XmlTags.UNIT, XmlTags.SECOND);
            processingTimeElm.setText(XmlUtils.formatDouble(0.001 * totalProcessingTimeInMillis));
            runElm.addContent(processingTimeElm);
        }

        if (collector != null) {
            collector.process();
            Element metrics = collector.getResult();

            runElm.addContent(metrics);
        }

        Element rootElm = new Element(XmlTags.RESULTS);
        rootElm.addContent(runElm);

        if (problem != null) {
            /*
            CspWriter cspWriter = new CspWriter();
            cspWriter.setProblem(problem);
            cspWriter.setPlans(solutions);

            Element cspElm = cspWriter.convert();
            rootElm.addContent(cspElm);
            */
        }

        return rootElm;
    }

    /**
     * <p>Clear all data fields.</p>
     */
    public void clear() {
        problem = null;
        solutions.clear();
        algorithm = null;
        numberOfProblemsSolved = 0;
        numberOfExecutions = 0;
        totalProcessingTimeInMillis = 0L;
        collector = null;
    }

    private interface XmlTags {
        String RESULTS = "results";
        String RUN = "run";
        String DATE = "date";
        String METHOD = "method";
        String NAME = "name";
        String PARAMETERS = "parameters";
        String PROBLEMS = "problems";
        String PROCESS_TIME = "process-time";
        String UNIT = "unit";
        String SECOND = "s";
        String EXECUTIONS = "executions";
    }
}
