package com.akavrt.csp._1d.results.ui.table;

import com.akavrt.csp._1d.results.CutgenClass;

/**
 * User: akavrt
 * Date: 26.12.13
 * Time: 23:36
 */
public class ClassData {
    private final CutgenClass cutgenClass;
    private final int resultsCount;
    private final int effectiveCount;

    public ClassData(CutgenClass cutgenClass, int resultsCount, int effectiveCount) {
        this.cutgenClass = cutgenClass;
        this.resultsCount = resultsCount;
        this.effectiveCount = effectiveCount;
    }

    public CutgenClass getCutgenClass() {
        return cutgenClass;
    }

    public int getResultsCount() {
        return resultsCount;
    }

    public int getEffectiveCount() {
        return effectiveCount;
    }
}
