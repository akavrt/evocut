package com.akavrt.csp._1d.analyzer;

import com.akavrt.csp._1d.core.Plan;
import com.akavrt.csp._1d.metrics.Metric;

import java.util.List;

/**
 * User: akavrt
 * Date: 23.03.13
 * Time: 02:44
 */
public class StandardDeviation implements Measure {
    private final Measure average;

    public StandardDeviation() {
        average = new Average();
    }

    @Override
    public String name() {
        return "sigma";
    }

    @Override
    public double calculate(List<Plan> solutions, Metric metric) {
        if (solutions == null || solutions.size() == 0) {
            return 0;
        }

        double mean = average.calculate(solutions, metric);
        double result = 0;
        for (Plan solution : solutions) {
            double evaluated = metric.evaluate(solution);
            result += (evaluated - mean) * (evaluated - mean);
        }

        return Math.sqrt(result / solutions.size());
    }

    @Override
    public double calculate(List<Long> times) {
        if (times == null || times.size() == 0) {
            return 0;
        }

        double mean = average.calculate(times);
        double result = 0;
        for (Long time : times) {
            result += (time - mean) * (time - mean);
        }

        return Math.sqrt(result / times.size());
    }
}
