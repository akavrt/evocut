package com.akavrt.csp._1d.tester.moea.ui;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.solver.ExecutionContext;
import com.akavrt.csp._1d.solver.evo.EvolutionPhase;
import com.akavrt.csp._1d.solver.moea.MoeaAlgorithm;
import com.akavrt.csp._1d.solver.moea.MoeaPopulation;
import com.akavrt.csp._1d.solver.moea.MoeaProgressChangeListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.List;

/**
 * User: akavrt
 * Date: 10.04.13
 * Time: 15:05
 */
public class AsyncSolver extends SwingWorker<List<Plan>, MoeaProgressUpdate> implements
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
    protected List<Plan> doInBackground() {
        return algorithm.execute(this);
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
            List<Plan> solutions = null;
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
        void onProblemSolved(List<Plan> solutions);
    }

}
