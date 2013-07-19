package com.akavrt.csp._1d.solver.moea;

/**
 * User: akavrt
 * Date: 19.07.13
 * Time: 18:24
 */
public interface MoeaProgressChangeListener {
    void onInitializationProgressChanged(int progress);
    void onGenerationProgressChanged(int progress, MoeaPopulation population);
}
