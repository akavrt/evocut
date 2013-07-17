package com.akavrt.csp._1d.solver.moea;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.ExecutionContext;
import com.akavrt.csp._1d.solver.evo.EvolutionaryAlgorithmParameters;
import com.akavrt.csp._1d.solver.evo.EvolutionaryOperator;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * User: akavrt
 * Date: 16.07.13
 * Time: 22:57
 */
public class MoeaPopulation {
    private static final Logger LOGGER = LogManager.getLogger(MoeaPopulation.class);
    private final ExecutionContext context;
    private final EvolutionaryAlgorithmParameters parameters;
    private final List<Chromosome> parents;
    private final RankEstimator estimator;
    private final Comparator<Chromosome> comparator;
    private final Random generator;
    private int age;

    public MoeaPopulation(ExecutionContext context,
                          EvolutionaryAlgorithmParameters parameters) {
        this.context = context;
        this.parameters = parameters;

        parents = Lists.newArrayList();

        estimator = new NaiveRankEstimator();
        comparator = new RankComparator();
        generator = new Random();
    }

    public int getAge() {
        return age;
    }

    public List<Plan> getSolutions() {
        List<Plan> plans = Lists.newArrayList();
        for (Chromosome chromosome : parents) {
            plans.add(chromosome.getPlan());
        }

        return plans;
    }

    public void initialize(Algorithm initProcedure) {
        parents.clear();

        age = 0;

        // fill population with solutions generated using auxiliary algorithm
        while (!context.isCancelled() && parents.size() < parameters.getPopulationSize()) {
            // run auxiliary algorithm
            List<Plan> solutions = initProcedure.execute(context);

            if (solutions != null && solutions.size() > 0 && solutions.get(0) != null) {
                Plan plan = solutions.get(0);
                parents.add(new Chromosome(plan));

                LOGGER.debug("Adding #{} chromosome to the population.", parents.size());
            } else {
                LOGGER.debug("No solution was found by constructive algorithm.");
            }
        }

        estimator.estimate(parents);
    }

    public void generation(EvolutionaryOperator mutation) {
        age++;

        LOGGER.debug("*** GENERATION {} ***", age);

        mutate(mutation);
        truncate();
    }

    private void mutate(EvolutionaryOperator mutation) {
        while (parents.size() < 2 * parameters.getPopulationSize()) {
            Chromosome parent = pick();
            Plan mutated = mutation.apply(parent.getPlan());

            parents.add(new Chromosome(mutated));
        }
    }

    private Chromosome pick() {
        // binary tournament selection is used to select parent chromosome

        int index = generator.nextInt(parameters.getPopulationSize());
        Chromosome first = parents.get(index);

        // TODO implement retry strategy to guarantee that distinct indices were selected
        index = generator.nextInt(parameters.getPopulationSize());
        Chromosome second = parents.get(index);

        return comparator.compare(first, second) < 0 ? first : second;
    }

    private void truncate() {
        // reset previously calculated ranks and densities
        for (Chromosome parent : parents) {
            parent.reset();
        }

        // update ranks and densities
        estimator.estimate(parents);

        // sort population using crowded-comparison operator
        // after sorting the best chromosome will be the first one,
        // the worst chromosome - the last one
        Collections.sort(parents, comparator);

        // discard the second half of the intermediate population
        while (parents.size() > parameters.getPopulationSize()) {
            parents.remove(parents.size() - 1);
        }
    }

}
