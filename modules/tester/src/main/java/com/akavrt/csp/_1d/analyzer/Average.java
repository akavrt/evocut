package com.akavrt.csp._1d.analyzer;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.metrics.Metric;

import java.util.List;

/**
 * User: akavrt
 * Date: 23.03.13
 * Time: 02:39
 */
public class Average implements Measure {

    @Override
    public String name() {
        return "average";
    }

    @Override
    public double calculate(List<Plan> solutions, Metric metric) {
        if (solutions == null || solutions.size() == 0) {
            return 0;
        }

        double result = 0;
        for (Plan solution : solutions) {
            result += metric.evaluate(solution);
        }

        return result / solutions.size();
    }

    @Override
    public double calculate(List<Long> times) {
        if (times == null || times.size() == 0) {
            return 0;
        }

        double result = 0;
        for (Long time : times) {
            result += time;
        }

        return result / times.size();
    }
}
