package com.akavrt.csp._1d.solver;

import com.akavrt.csp._1d.core.Problem;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 18:47
 */
public interface ExecutionContext {
    Problem getProblem();
    boolean isCancelled();
}
