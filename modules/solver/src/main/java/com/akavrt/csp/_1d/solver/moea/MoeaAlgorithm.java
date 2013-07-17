package com.akavrt.csp._1d.solver.moea;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.ExecutionContext;
import com.akavrt.csp._1d.solver.evo.EvolutionaryAlgorithmParameters;
import com.akavrt.csp._1d.solver.evo.EvolutionaryOperator;
import com.akavrt.csp._1d.utils.ParameterSet;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * User: akavrt
 * Date: 16.07.13
 * Time: 22:11
 */
public class MoeaAlgorithm implements Algorithm {
    private final Algorithm initializationProcedure;
    private final EvolutionaryOperator mutation;
    private final EvolutionaryAlgorithmParameters parameters;

    public MoeaAlgorithm(
            Algorithm initializationProcedure,
            EvolutionaryOperator mutation,
            EvolutionaryAlgorithmParameters parameters) {
        this.initializationProcedure = initializationProcedure;
        this.mutation = mutation;
        this.parameters = parameters;
    }

    @Override
    public String name() {
        return "NSGA-II";
    }

    @Override
    public List<ParameterSet> getParameters() {
        List<ParameterSet> params = Lists.newArrayList();

        parameters.setDescription("Parameters of the master method.");
        params.add(parameters);

        for (ParameterSet parameterSet : initializationProcedure.getParameters()) {
            parameterSet.setDescription("Parameters of the initialization procedure.");
            params.add(parameterSet);
        }

        return params;
    }

    @Override
    public List<Plan> execute(ExecutionContext context) {
        if (context.getProblem() == null) {
            return null;
        }

        return search(context);
    }

    private List<Plan> search(ExecutionContext context) {
        // initializing mutation operator
        mutation.initialize(context);

        MoeaPopulation population = new MoeaPopulation(context, parameters);

        initializationPhase(population);
        generationalPhase(context, population);

        return population.getSolutions();
    }

    private void initializationPhase(MoeaPopulation population) {
        population.initialize(initializationProcedure);
    }

    private void generationalPhase(ExecutionContext context, MoeaPopulation population) {
        while (!context.isCancelled() && population.getAge() < parameters.getRunSteps()) {
            population.generation(mutation);
        }
    }

}
