package com.akavrt.csp._1d.solver.evo.es;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.metrics.Metric;
import com.akavrt.csp._1d.solver.ExecutionContext;
import com.akavrt.csp._1d.solver.evo.DiversePopulation;
import com.akavrt.csp._1d.solver.evo.DiversityManager;
import com.akavrt.csp._1d.solver.evo.EvolutionaryOperator;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * User: akavrt
 * Date: 26.04.13
 * Time: 22:03
 */
public class ModStrategyPopulation extends DiversePopulation {
    private static final Logger LOGGER = LogManager.getLogger(ModStrategyPopulation.class);
    private final EvolutionStrategyParameters parameters;

    public ModStrategyPopulation(ExecutionContext context,
                                 EvolutionStrategyParameters parameters, Metric objectiveFunction) {
        super(context, parameters, objectiveFunction);

        this.parameters = parameters;
    }

    @Override
    public void generation(EvolutionaryOperator... operators) {
        EvolutionaryOperator mutation = operators[0];

        DiversityManager dm = getDiversityManager();

        incAge();

        LOGGER.debug("*** GENERATION {} ***", getAge());

        sort();

        // pick the first 'EvolutionStrategyParameters.getOffspringCount()' chromosomes (lambda,
        // the most fitted part of the population) and use them to produce new chromosomes
        List<Plan> exchangeList = Lists.newArrayList();
        for (int i = 0; i < parameters.getOffspringCount(); i++) {
            exchangeList.add(chromosomes.get(i));
        }

        dm.reset();
        for (int i = 0; i < chromosomes.size() - exchangeList.size(); i++) {
            Plan chromosome = chromosomes.get(i);
            dm.add(chromosome);
        }

        // replace content of the exchange list with new chromosomes
        prepareExchange(exchangeList, mutation, dm);

        // exchange the last 'EvolutionStrategyParameters.getOffspringCount()' chromosomes
        // (the worst fitted part of the population) with new chromosomes
        int offset = chromosomes.size() - exchangeList.size();
        for (int i = 0; i < exchangeList.size(); i++) {
            Plan chromosome = exchangeList.get(i);
            chromosomes.set(offset + i, chromosome);
        }

        LOGGER.debug("Generation #{}, diversity measure: {} unique of {} total solutions.",
                     getAge(), dm.getMeasure(), chromosomes.size());
        LOGGER.debug("Generation #{}, {} retries were done.", getAge(), dm.getRetryCount());

        if (LOGGER.isDebugEnabled()) {
            double groupLength = 0;
            for (Plan chromosome : chromosomes) {
                groupLength += chromosome.size() / (double) chromosome.getSetups();
            }

            String formatted = String.format("%.2f", groupLength / chromosomes.size());
            LOGGER.debug("Generation #{}, average group length is {}.", getAge(), formatted);
        }
    }

    private List<Plan> prepareExchange(List<Plan> exchangeList,
                                             EvolutionaryOperator mutation,
                                             DiversityManager dm) {
        for (int i = 0; i < exchangeList.size(); i++) {
            // pick chromosome from the exchange list and apply mutation to it,
            // then replace original chromosome with mutated one in the exchange list
            Plan original = exchangeList.get(i);
            Plan mutated = applyMutation(mutation, dm, original);
            exchangeList.set(i, mutated);
            dm.add(mutated);
        }

        return exchangeList;
    }

    private Plan applyMutation(EvolutionaryOperator mutation, DiversityManager dm, Plan original) {
        int stuck = 0;
        Plan mutated = null;
        while (mutated == null) {
            Plan candidate = mutation.apply(original);
            if (dm.isAdded(candidate) && stuck < getRetryBound()) {
                dm.incRetryCount();
                stuck++;
            } else {
                mutated = candidate;
            }
        }

        return mutated;
    }

}
