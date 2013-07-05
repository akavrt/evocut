package com.akavrt.csp._1d.tester;

import com.akavrt.csp._1d.metrics.Metric;
import com.akavrt.csp._1d.solver.evo.EvolutionaryOperator;
import com.akavrt.csp._1d.solver.evo.es.BaseStrategyComponentsFactory;
import com.akavrt.csp._1d.solver.evo.operators.CompositeMutation;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;

/**
 * User: akavrt
 * Date: 02.07.13
 * Time: 13:48
 */
public class TraceableStrategyComponentsFactory extends BaseStrategyComponentsFactory {
    private final Metric objectiveFunction;
    private CompositeMutation mutation;

    public TraceableStrategyComponentsFactory(PatternGenerator generator,
                                              Metric objectiveFunction) {
        super(generator);
        this.objectiveFunction = objectiveFunction;
    }

    @Override
    public EvolutionaryOperator createMutation() {
        mutation = new CompositeMutation(objectiveFunction);

        // add operators
        prepareMutationOperator(mutation);

        return mutation;
    }

    public void traceMutation() {
        if (mutation != null) {
            mutation.traceResults();
        }
    }

}
