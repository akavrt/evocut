package com.akavrt.csp._1d.solver.x;

import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.evo.es.BaseStrategyComponentsFactory;
import com.akavrt.csp._1d.solver.evo.operators.AdaptMultiplierMutation;
import com.akavrt.csp._1d.solver.evo.operators.AdaptPatternMutation;
import com.akavrt.csp._1d.solver.evo.operators.CompositeMutation;
import com.akavrt.csp._1d.solver.evo.operators.DecrementMultiplierMutation;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;

/**
 * User: akavrt
 * Date: 07.07.13
 * Time: 18:56
 */
public class TunableStrategyComponentsFactory extends BaseStrategyComponentsFactory {
    private final Algorithm initAlgorithm;

    public TunableStrategyComponentsFactory(PatternGenerator generator, Algorithm initAlgorithm) {
        super(generator);
        this.initAlgorithm = initAlgorithm;
    }

    @Override
    protected void prepareMutationOperator(CompositeMutation mutation) {
//        mutation.addOperator(new IncrementMultiplierMutation(patternGenerator));
        mutation.addOperator(new DecrementMultiplierMutation(patternGenerator));
        mutation.addOperator(new AdaptPatternMutation(patternGenerator));
        mutation.addOperator(new AdaptMultiplierMutation(patternGenerator));
//        mutation.addOperator(new MergePatternsMutation(patternGenerator, 2, 3));
    }

    @Override
    public Algorithm createInitializationProcedure() {
        return initAlgorithm;
    }

}
