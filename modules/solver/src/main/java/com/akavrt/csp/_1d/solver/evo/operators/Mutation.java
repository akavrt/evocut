package com.akavrt.csp._1d.solver.evo.operators;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.ExecutionContext;
import com.akavrt.csp._1d.solver.evo.EvolutionaryOperator;
import com.akavrt.csp._1d.solver.pattern.PatternGenerator;

import java.util.Random;

/**
 * User: akavrt
 * Date: 01.07.13
 * Time: 23:35
 */
public abstract class Mutation implements EvolutionaryOperator {
    protected final PatternGenerator generator;
    protected final Random rGen;
    private final UpperBoundHelper ubHelper;

    public Mutation(PatternGenerator generator) {
        this.generator = generator;

        rGen = new Random();
        ubHelper = new UpperBoundHelper();
    }

    @Override
    public void initialize(ExecutionContext context) {
        generator.initialize(context.getProblem());
    }

    protected int[] calculateDemand(Plan chromosome, int multiplier) {
        int[] demand = chromosome.getResidualDemand();
        for (int i = 0; i < demand.length; i++) {
            demand[i] = (int) Math.ceil(demand[i] / (double) multiplier);
        }

        return demand;
    }

    protected double calculateLengthToleranceRatio(Plan chromosome) {
        double trimRatio = chromosome.getTrimRatio();
        return Math.min(1.1 * trimRatio, 1);

//        return 0;
    }

    protected int calculateUpperBound(Plan chromosome) {
        int residualDemandLength = chromosome.getResidualDemandLength();
        double stockLength = chromosome.getStockLength();
        int naiveLowerBound = (int) Math.ceil(residualDemandLength / stockLength);

        int upperBound = (int) Math.ceil((11 * naiveLowerBound + 6) / 9.0);

        return upperBound;
    }

    /*
    protected int calculateUpperBound(Plan chromosome) {
        return ubHelper.compute(chromosome);
    }
    */

}
