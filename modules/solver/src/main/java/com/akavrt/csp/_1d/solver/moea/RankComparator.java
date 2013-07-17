package com.akavrt.csp._1d.solver.moea;

import java.util.Comparator;

/**
 * User: akavrt
 * Date: 16.07.13
 * Time: 23:19
 */
public class RankComparator implements Comparator<Chromosome> {

    @Override
    public int compare(Chromosome lhs, Chromosome rhs) {
        int result;
        if (lhs.getRank() == rhs.getRank()) {
            // if ranks are equal, crowding distance is taken into account
            // boundary solutions are treated like those having infinite crowding distance
            if (lhs.isBoundary() && rhs.isBoundary()) {
                result = 0;
            } else if (!lhs.isBoundary() && !rhs.isBoundary()) {
                result = lhs.getDistance() > rhs.getDistance() ? -1 : 1;
            } else {
                result = lhs.isBoundary() ? -1 : 1;
            }
        } else {
            // prefer solution with smaller rank
            result = lhs.getRank() < rhs.getRank() ? -1 : 1;
        }

        return result;
    }

}
