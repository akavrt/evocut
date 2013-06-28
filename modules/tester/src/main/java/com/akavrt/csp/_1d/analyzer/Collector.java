package com.akavrt.csp._1d.analyzer;

import com.akavrt.csp._1d.core.Plan;

/**
 * User: akavrt
 * Date: 23.03.13
 * Time: 00:35
 */
public interface Collector {
    void collect(Plan solution);
    void collect(Plan solution, long millis);
    void clear();
    void process();
}
