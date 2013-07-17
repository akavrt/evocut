package com.akavrt.csp._1d.solver.evo.es;

import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.evo.EvolutionaryComponentsFactory;
import com.akavrt.csp._1d.solver.evo.EvolutionaryOperator;
import com.akavrt.csp._1d.solver.evo.operators.*;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.solver.sequential.SimplifiedProcedure;

/**
 * User: akavrt
 * Date: 30.04.13
 * Time: 13:40
 */
public class BaseStrategyComponentsFactory implements EvolutionaryComponentsFactory {
    protected final PatternGenerator patternGenerator;

    public BaseStrategyComponentsFactory(PatternGenerator generator) {
        this.patternGenerator = generator;
    }

    @Override
    public EvolutionaryOperator createMutation() {
        CompositeMutation mutation = new CompositeMutation();

        // add operators
        prepareMutationOperator(mutation);

        return mutation;
    }

    protected void prepareMutationOperator(CompositeMutation mutation) {
        mutation.addOperator(new IncrementMultiplierMutation(patternGenerator));
        mutation.addOperator(new DecrementMultiplierMutation(patternGenerator));
        mutation.addOperator(new AdaptPatternMutation(patternGenerator));
        mutation.addOperator(new AdaptMultiplierMutation(patternGenerator));
        mutation.addOperator(new MergePatternsMutation(patternGenerator, 2, 10));
    }

    @Override
    public Algorithm createInitializationProcedure() {
        return new SimplifiedProcedure(patternGenerator);
    }

}

