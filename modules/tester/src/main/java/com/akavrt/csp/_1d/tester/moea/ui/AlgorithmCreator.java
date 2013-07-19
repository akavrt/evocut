package com.akavrt.csp._1d.tester.moea.ui;

import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.evo.EvolutionaryAlgorithmParameters;
import com.akavrt.csp._1d.solver.evo.EvolutionaryOperator;
import com.akavrt.csp._1d.solver.evo.operators.*;
import com.akavrt.csp._1d.solver.moea.MoeaAlgorithm;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;
import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.solver.pattern.UnconstrainedPatternGenerator;
import com.akavrt.csp._1d.solver.sequential.SimplifiedProcedure;
import com.akavrt.csp._1d.tester.moea.ui.presets.PresetsPanel;

/**
 * User: akavrt
 * Date: 19.07.13
 * Time: 19:44
 */
public class AlgorithmCreator {
    private final PresetsPanel presetsPanel;

    public AlgorithmCreator(PresetsPanel presetsPanel) {
        this.presetsPanel = presetsPanel;
    }

    private PatternGenerator createPatternGenerator() {
        PatternGeneratorParameters generatorParams = presetsPanel.getPatternGeneratorParameters();
        if (generatorParams == null) {
            generatorParams = new PatternGeneratorParameters();
        }

        return new UnconstrainedPatternGenerator(generatorParams);
    }

    private Algorithm createInitAlgorithm(PatternGenerator generator) {
        return new SimplifiedProcedure(generator);
    }

    private EvolutionaryOperator createMutationOperator(PatternGenerator generator) {
        CompositeMutation mutation = new CompositeMutation();

        mutation.addOperator(new IncrementMultiplierMutation(generator));
        mutation.addOperator(new DecrementMultiplierMutation(generator));
        mutation.addOperator(new AdaptPatternMutation(generator));
        mutation.addOperator(new AdaptMultiplierMutation(generator));
        mutation.addOperator(new MergePatternsMutation(generator, 2, 10));

        return mutation;
    }

    public MoeaAlgorithm create() {
        PatternGenerator generator = createPatternGenerator();

        Algorithm initAlgorithm = createInitAlgorithm(generator);

        EvolutionaryOperator mutation = createMutationOperator(generator);

        EvolutionaryAlgorithmParameters moeaParameters = presetsPanel.getMoeaParameters();
        if (moeaParameters == null) {
            moeaParameters = new EvolutionaryAlgorithmParameters();
        }

        return new MoeaAlgorithm(initAlgorithm, mutation, moeaParameters);
    }

}
