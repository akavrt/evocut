package com.akavrt.csp._1d.solver.evo;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.metrics.ContextMetricProvider;
import com.akavrt.csp._1d.metrics.Metric;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.ExecutionContext;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: akavrt
 * Date: 27.04.13
 * Time: 01:09
 */
public abstract class DiversePopulation implements Population, ContextMetricProvider {
    private static final Logger LOGGER = LogManager.getLogger(DiversePopulation.class);
    protected final List<Plan> chromosomes;
    private final ExecutionContext context;
    private final DiversityManager dm;
    private final EvolutionaryAlgorithmParameters parameters;
    private final Comparator<Plan> comparator;
    private int age;

    public DiversePopulation(ExecutionContext context,
                             EvolutionaryAlgorithmParameters parameters, Metric objectiveFunction) {
        this.context = context;
        this.parameters = parameters;

        chromosomes = Lists.newArrayList();
        dm = new DiversityManager();

        objectiveFunction.setContextMetricProvider(this);
        comparator = objectiveFunction.getReverseComparator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAge() {
        return age;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Plan> getChromosomes() {
        return chromosomes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Plan> getSolutions() {
        return chromosomes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(Algorithm initProcedure) {
        initialize(initProcedure, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(Algorithm initProcedure, EvolutionProgressChangeListener listener) {
        chromosomes.clear();
        dm.reset();

        age = 0;

        // fill population with solutions generated using auxiliary algorithm
        int stuck = 0;
        while (!context.isCancelled() && chromosomes.size() < parameters.getPopulationSize()) {
            // run auxiliary algorithm
            List<Plan> solutions = initProcedure.execute(context);

            if (solutions != null && solutions.size() > 0 && solutions.get(0) != null) {
                Plan chromosome = solutions.get(0);
                if (dm.isAdded(chromosome) && stuck < getRetryBound()) {
                    stuck++;
                } else {
                    chromosomes.add(chromosome);
                    dm.add(chromosome);

                    LOGGER.debug("Adding #{} chromosome to the population.", chromosomes.size());

                    stuck = 0;

                    if (listener != null) {
                        int progress = 100 * chromosomes.size() / parameters.getPopulationSize();
                        progress = Math.min(progress, 100);

                        listener.onInitializationProgressChanged(progress);
                    }
                }
            } else {
                LOGGER.debug("No solution was found by constructive algorithm.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sort() {
        sort(chromosomes);
    }

    protected void sort(List<Plan> chromosomes) {
        Collections.sort(chromosomes, comparator);
    }

    protected void incAge() {
        age++;
    }

    protected ExecutionContext getContext() {
        return context;
    }

    protected DiversityManager getDiversityManager() {
        return dm;
    }

    protected int getRetryBound() {
        return dm.getRetryBound();
    }

    @Override
    public int getMaterialUpperBound() {
        return dm.getMaterialUpperBound();
    }
}
