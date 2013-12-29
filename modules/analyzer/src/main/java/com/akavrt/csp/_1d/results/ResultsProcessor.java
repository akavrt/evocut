package com.akavrt.csp._1d.results;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: akavrt
 * Date: 26.12.13
 * Time: 16:01
 */
public class ResultsProcessor {
    private ResultComparator comparator;

    public ResultsProcessor() {
        comparator = new ResultComparator();
    }

    public void process(List<? extends Result> results) {
        for (Result outer : results) {
            outer.setDominated(false);
            for (Result inner : results) {
                if (outer.isDominatedBy(inner)) {
                    outer.setDominated(true);
                    break;
                }
            }
        }

        Collections.sort(results, comparator);
    }

    private static class ResultComparator implements Comparator<Result> {

        @Override
        public int compare(Result lhs, Result rhs) {
            int result;
            if (lhs.isDominated() != rhs.isDominated()) {
                result = lhs.isDominated() ? 1 : -1;
            } else {
                result = lhs.getMaterial() < rhs.getMaterial() ? -1 : 1;
            }

            return result;
        }
    }
}
