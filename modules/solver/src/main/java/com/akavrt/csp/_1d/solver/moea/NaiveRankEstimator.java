package com.akavrt.csp._1d.solver.moea;

import java.util.ArrayList;
import java.util.List;

/**
 * User: akavrt
 * Date: 16.07.13
 * Time: 23:16
 */
public class NaiveRankEstimator implements RankEstimator {
    private final DensityEstimator densityEstimator;

    public NaiveRankEstimator() {
        densityEstimator = new DensityEstimator();
    }

    @Override
    public void estimate(List<Chromosome> chromosomes) {
        List<Chromosome> holder = new ArrayList<Chromosome>(chromosomes);

        int rank = 1;
        while (holder.size() > 0) {
            List<Integer> dominance = new ArrayList<Integer>();

            for (int i = 0; i < holder.size(); i++) {
                int dominated = 0;
                Chromosome point = holder.get(i);
                for (int j = 0; j < holder.size(); j++) {
                    dominated += point.isDominated(holder.get(j)) ? 1 : 0;
                }

                dominance.add(dominated);
            }

            // find non-dominated solutions, set rank
            // and remove them from holder list
            int index = 0;
            List<Chromosome> front = new ArrayList<Chromosome>();
            while (index < holder.size()) {
                if (dominance.get(index) == 0) {
                    Chromosome frontPoint = holder.remove(index);

                    frontPoint.setRank(rank);
                    front.add(frontPoint);

                    dominance.remove(index);
                } else {
                    index++;
                }
            }

            estimateDistance(front);

            rank++;
        }
    }

    private void estimateDistance(List<Chromosome> front) {
        densityEstimator.estimate(front);
    }

}
