package com.akavrt.csp._1d.solver.moea;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.solver.Algorithm;
import com.akavrt.csp._1d.solver.ExecutionContext;
import com.akavrt.csp._1d.solver.evo.EvolutionaryAlgorithmParameters;
import com.akavrt.csp._1d.solver.evo.EvolutionaryOperator;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

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
    private final List<Chromosome> rejects;
    private final Set<Integer> hashes;
    private final List<Chromosome> buffer;
    private final RankEstimator estimator;
    private final Comparator<Chromosome> comparator;
    private final Random generator;
    private int age;

    public MoeaPopulation(ExecutionContext context,
                          EvolutionaryAlgorithmParameters parameters) {
        this.context = context;
        this.parameters = parameters;

        parents = Lists.newArrayList();
        rejects = Lists.newArrayList();

        hashes = Sets.newHashSet();
        buffer = Lists.newArrayList();

        estimator = new NaiveRankEstimator();
        comparator = new RankComparator();
        generator = new Random();
    }

    public int getAge() {
        return age;
    }

    public List<Chromosome> getParents() {
        return parents;
    }

    public List<Chromosome> getRejects() {
        return rejects;
    }

    public List<Plan> getSolutions() {
        List<Plan> plans = Lists.newArrayList();
        for (Chromosome chromosome : parents) {
            plans.add(chromosome.getPlan());
        }

        return plans;
    }

    public void initialize(Algorithm initProcedure) {
        initialize(initProcedure, null);
    }

    public void initialize(Algorithm initProcedure, MoeaProgressChangeListener listener) {
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

                if (listener != null) {
                    int progress = 100 * parents.size() / parameters.getPopulationSize();
                    progress = Math.min(progress, 100);

                    listener.onInitializationProgressChanged(progress);
                }
            } else {
                LOGGER.debug("No solution was found by constructive algorithm.");
            }
        }

        estimator.estimate(parents);
    }

    public void generation(EvolutionaryOperator mutation) {
        incAge();
        mutate(mutation);
        truncate();
    }

    private void incAge() {
        age++;

        LOGGER.debug("*** GENERATION {} ***", age);

        for (Chromosome parent : parents) {
            parent.incAge();
        }
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

        diversityAwareTruncation();
    }

    private void naiveTruncation() {
        // discard the second half of the intermediate population
        rejects.clear();
        while (parents.size() > parameters.getPopulationSize()) {
            Chromosome rejected = parents.remove(parents.size() - 1);
            rejects.add(rejected);
        }
    }

    private void diversityAwareTruncation() {
        // simple strategy employed to filter out duplicate solutions
        hashes.clear();
        buffer.clear();
        int index = 0;
        while (buffer.size() < parameters.getPopulationSize() && index < parents.size()) {
            Chromosome candidate = parents.get(index);
            if (hashes.add(candidate.getPlan().hashCode())) {
                buffer.add(candidate);
                parents.remove(index);
            } else {
                index++;
            }
        }

        // TODO we need to consider the following approach:
        // duplicates replaced with newly constructed solutions
        // or additional mutation is applied to them

        // if there is not enough unique solutions
        // to fill in parent population, use duplicates
        while (buffer.size() < parameters.getPopulationSize()) {
            // previously we used: buffer.add(parents.remove(0));

            int pick = generator.nextInt(parents.size());
            buffer.add(parents.remove(pick));
        }

        rejects.clear();
        rejects.addAll(parents);

        parents.clear();
        parents.addAll(buffer);
    }

}
