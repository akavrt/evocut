package com.akavrt.csp._1d.solver.pattern;

import com.akavrt.csp._1d.core.Problem;

public interface PatternGenerator {
    int[] generate(int[] demand, double allowedTrimRatio);
    PatternGeneratorParameters getParameters();
    public void initialize(Problem problem);
}
