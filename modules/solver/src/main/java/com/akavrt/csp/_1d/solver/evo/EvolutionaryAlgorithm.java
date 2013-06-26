package com.akavrt.csp._1d.solver.evo;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.ExecutionContext;

import java.util.List;

public abstract class EvolutionaryAlgorithm implements Algorithm {
    private final Algorithm initializationProcedure;
    private final EvolutionaryAlgorithmParameters parameters;
    private EvolutionProgressChangeListener progressChangeListener;

    public EvolutionaryAlgorithm(Algorithm initializationProcedure,
                                 EvolutionaryAlgorithmParameters parameters) {
        this.parameters = parameters;
        this.initializationProcedure = initializationProcedure;
    }

    protected abstract void initializeOperators(ExecutionContext evoContext);

    protected abstract void applyOperators(Population population);

    protected abstract Population createPopulation(ExecutionContext evoContext);

    protected abstract String getShortMethodName();

    @Override
    public List<Plan> execute(ExecutionContext context) {
        if (context.getProblem() == null) {
            return null;
        }

        return search(context);
    }

    protected List<Plan> search(ExecutionContext context) {
        initializeOperators(context);

        Population population = createPopulation(context);

        initializationPhase(population);
        generationalPhase(context, population);

        return population.getSolutions();
    }

    private void initializationPhase(Population population) {
        population.initialize(initializationProcedure, progressChangeListener);
    }

    private void generationalPhase(ExecutionContext context, Population population) {
        while (!context.isCancelled() && population.getAge() < parameters.getRunSteps()) {
            applyOperators(population);

            if (progressChangeListener != null) {
                int progress = 100 * population.getAge() / parameters.getRunSteps();
                progress = Math.min(progress, 100);

                progressChangeListener.onGenerationProgressChanged(progress, population);
            }
        }

        population.sort();
    }

    public void setProgressChangeListener(EvolutionProgressChangeListener listener) {
        this.progressChangeListener = listener;
    }

    public void removeProgressChangeListener() {
        this.progressChangeListener = null;
    }

    protected Algorithm getInitializationProcedure() {
        return initializationProcedure;
    }

}
