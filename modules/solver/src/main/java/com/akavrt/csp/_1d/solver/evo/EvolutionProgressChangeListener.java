package com.akavrt.csp._1d.solver.evo;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 20:07
 */
public interface EvolutionProgressChangeListener {
    void onInitializationProgressChanged(int progress);
    void onGenerationProgressChanged(int progress, Population population);
}
