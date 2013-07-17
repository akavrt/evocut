package com.akavrt.csp._1d.solver.moea;

import java.util.List;

/**
 * User: akavrt
 * Date: 16.07.13
 * Time: 23:16
 */
public interface RankEstimator {
    public void estimate(List<Chromosome> points);
}
