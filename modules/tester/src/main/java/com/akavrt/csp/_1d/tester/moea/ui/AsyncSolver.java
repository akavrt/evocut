package com.akavrt.csp._1d.tester.moea.ui;

import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.solver.ExecutionContext;
import com.akavrt.csp._1d.solver.evo.EvolutionPhase;
import com.akavrt.csp._1d.solver.moea.Chromosome;
import com.akavrt.csp._1d.solver.moea.MoeaAlgorithm;
import com.akavrt.csp._1d.solver.moea.MoeaPopulation;
import com.akavrt.csp._1d.solver.moea.MoeaProgressChangeListener;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: akavrt
 * Date: 10.04.13
 * Time: 15:05
 */
public class AsyncSolver extends SwingWorker<List<Chromosome>, MoeaProgressUpdate> implements
        ExecutionContext, MoeaProgressChangeListener {
    private final static Logger LOGGER = LogManager.getLogger(AsyncSolver.class);
    private final Problem problem;
    private final MoeaAlgorithm algorithm;
    private final OnProblemSolvedListener listener;

    public AsyncSolver(Problem problem, MoeaAlgorithm algorithm, OnProblemSolvedListener listener) {
        this.problem = problem;
        this.algorithm = algorithm;
        this.listener = listener;

        this.algorithm.setProgressChangeListener(this);
    }

    @Override
    public Problem getProblem() {
        return problem;
    }

    @Override
    protected List<Chromosome> doInBackground() {
        algorithm.execute(this);

        List<Chromosome> chromosomes = null;
        if (algorithm.getPopulation() != null && algorithm.getPopulation().getParents() != null) {
            chromosomes = Lists.newArrayList(algorithm.getPopulation().getParents());

            Collections.sort(chromosomes, new Comparator<Chromosome>() {
                @Override
                public int compare(Chromosome lhs, Chromosome rhs) {
                    int result;

                    if (lhs.getRank() == rhs.getRank()) {
                        if (lhs.getObjective(1) == rhs.getObjective(1)) {
                            result = lhs.getObjective(0) == rhs.getObjective(0)
                                    ? 0
                                    : (lhs.getObjective(0) < rhs.getObjective(0) ? -1 : 1);
                        } else {
                            result = lhs.getObjective(1) < rhs.getObjective(1) ? -1 : 1;
                        }
                    } else {
                        result = lhs.getRank() < rhs.getRank() ? -1 : 1;
                    }

                    return result;
                }
            });

        }

        return chromosomes;
    }

    @Override
    protected void process(List<MoeaProgressUpdate> updates) {
        if (listener != null && !isCancelled() && updates.size() > 0) {
            MoeaProgressUpdate lastUpdate = updates.get(updates.size() - 1);
            listener.onEvolutionProgressChanged(lastUpdate);
        }
    }

    @Override
    public void done() {
        this.algorithm.removeProgressChangeListener();

        if (listener != null && !isCancelled()) {
            List<Chromosome> solutions = null;
            try {
                solutions = get();
            } catch (Exception e) {
                LOGGER.catching(e);
            }

            listener.onProblemSolved(solutions);
        }
    }

    @Override
    public void onInitializationProgressChanged(int progress) {
        publish(new MoeaProgressUpdate(progress, EvolutionPhase.INITIALIZATION));
    }

    @Override
    public void onGenerationProgressChanged(int progress, MoeaPopulation population) {
        publish(new MoeaProgressUpdate(progress, EvolutionPhase.GENERATION, population));
    }

    public interface OnProblemSolvedListener {
        void onEvolutionProgressChanged(MoeaProgressUpdate update);
        void onProblemSolved(List<Chromosome> solutions);
    }

}
