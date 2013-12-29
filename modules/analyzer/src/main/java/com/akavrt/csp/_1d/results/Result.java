package com.akavrt.csp._1d.results;

/**
 * User: akavrt
 * Date: 29.12.13
 * Time: 15:34
 */
public abstract class Result {
    protected double material;
    protected boolean isTrim;
    protected double patterns;
    protected boolean isDominated;

    public boolean isTrim() {
        return isTrim;
    }

    public double getMaterial() {
        return material;
    }

    public double getPatterns() {
        return patterns;
    }

    public boolean isDominatedBy(Result result) {
        return result.getMaterial() < material && result.getPatterns() <= patterns ||
                result.getMaterial() <= material && result.getPatterns() < patterns;
    }

    public boolean isDominated() {
        return isDominated;
    }

    public void setDominated(boolean isDominated) {
        this.isDominated = isDominated;
    }

    public boolean isValid() {
        return material > 0 && patterns > 0;
    }
}
