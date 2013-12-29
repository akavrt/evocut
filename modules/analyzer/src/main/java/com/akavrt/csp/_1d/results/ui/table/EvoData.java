package com.akavrt.csp._1d.results.ui.table;

import com.akavrt.csp._1d.results.EvoResult;

import java.util.List;

/**
 * User: akavrt
 * Date: 29.12.13
 * Time: 17:01
 */
public class EvoData {
    private final String name;
    private final String description;
    private final List<EvoResult> results;
    private boolean isSelected;

    public EvoData(String name, String description, List<EvoResult> results) {
        this.name = name;
        this.description = description;
        this.results = results;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<EvoResult> getResults() {
        return results;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
