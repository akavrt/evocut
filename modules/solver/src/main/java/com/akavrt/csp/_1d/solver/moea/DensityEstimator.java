package com.akavrt.csp._1d.solver.moea;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: akavrt
 * Date: 16.07.13
 * Time: 23:14
 */
public class DensityEstimator {

    public void estimate(List<Chromosome> points) {
        if (points == null || points.isEmpty()) {
            return;
        }

        for (int objIdx = 0, dimen = points.get(0).dimen(); objIdx < dimen; objIdx++) {
            // sort points along objective's axis
            Collections.sort(points, new ObjectiveComparator(objIdx));

            Chromosome left = points.get(0);
            Chromosome right = points.get(points.size() - 1);

            // infinity distance for boundary points is assumed
            left.setBoundary(true);
            right.setBoundary(true);

            double interval = right.getObjective(objIdx) - left.getObjective(objIdx);
            for (int i = 1; i < points.size() - 1; i++) {
                Chromosome previous = points.get(i - 1);
                Chromosome next = points.get(i + 1);

                double distance = 0;
                if (interval > 0) {
                    distance = (next.getObjective(objIdx) - previous.getObjective(objIdx)) / interval;
                }

                points.get(i).setDistance(objIdx, distance);
            }
        }
    }

    private static class ObjectiveComparator implements Comparator<Chromosome> {
        private final int objective;

        public ObjectiveComparator(int objective) {
            this.objective = objective;
        }

        @Override
        public int compare(Chromosome lhs, Chromosome rhs) {
            double lhsObjective = lhs.getObjective(objective);
            double rhsObjective = rhs.getObjective(objective);

            return lhsObjective == rhsObjective ? 0 : (lhsObjective < rhsObjective ? -1 : 1);
        }

    }

}
