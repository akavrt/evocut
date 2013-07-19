package com.akavrt.csp._1d.tester.moea.ui;

import com.akavrt.csp._1d.solver.evo.EvolutionPhase;
import com.akavrt.csp._1d.solver.moea.Chromosome;
import com.akavrt.csp._1d.solver.moea.MoeaPopulation;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: akavrt
 * Date: 10.04.13
 * Time: 18:10
 */
public class MoeaProgressUpdate {
    public final int progress;
    public final EvolutionPhase phase;
    private List<Chromosome> paretoFront;
    private List<Chromosome> feasibleSolutions;
    private List<Chromosome> infeasibleSolutions;

    public MoeaProgressUpdate(int progress, EvolutionPhase phase) {
        this(progress, phase, null);
    }

    public MoeaProgressUpdate(int progress, EvolutionPhase phase, MoeaPopulation population) {
        this.progress = progress;
        this.phase = phase;

        processPopulation(population);
    }

    private void processPopulation(MoeaPopulation population) {
        paretoFront = Lists.newArrayList();
        feasibleSolutions = Lists.newArrayList();
        infeasibleSolutions = Lists.newArrayList();

        if (population == null) {
            return;
        }

        for (Chromosome chromosome : population.getChromosomes()) {
            if (chromosome.getRank() == 1) {
                paretoFront.add(chromosome);
            }

            if (chromosome.isFeasible()) {
                feasibleSolutions.add(chromosome);
            } else {
                infeasibleSolutions.add(chromosome);
            }
        }

        Collections.sort(paretoFront, new Comparator<Chromosome>() {

            @Override
            public int compare(Chromosome lhs, Chromosome rhs) {
                double lhsMaterial = lhs.getObjective(0);
                double lhsSetups = lhs.getObjective(1);

                double rhsMaterial = rhs.getObjective(0);
                double rhsSetups = rhs.getObjective(1);

                int result;
                if (lhsMaterial == rhsMaterial) {
                    result = lhsSetups == rhsSetups ? 0 : (lhsSetups < rhsSetups ? -1 : 1);
                } else {
                    result = lhsMaterial < rhsMaterial ? -1 : 1;
                }

                return result;
            }
        });
    }

    public List<Chromosome> getParetoFront() {
        return paretoFront;
    }

    public List<Chromosome> getFeasibleSolutions() {
        return feasibleSolutions;
    }

    public List<Chromosome> getInfeasibleSolutions() {
        return infeasibleSolutions;
    }

}
