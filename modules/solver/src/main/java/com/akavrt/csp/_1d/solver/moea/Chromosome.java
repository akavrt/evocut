package com.akavrt.csp._1d.solver.moea;

import com.akavrt.csp._1d.core.Plan;

/**
 * User: akavrt
 * Date: 16.07.13
 * Time: 22:59
 */
public class Chromosome {
    private final Plan plan;
    private final double[] distances;
    private int rank;
    private boolean isBoundary;
    private int age;

    public Chromosome(Plan plan) {
        this.plan = plan;
        distances = new double[2];
        age = 1;
    }

    public int dimen() {
        return 2;
    }

    public void reset() {
        rank = 0;
        isBoundary = false;

        distances[0] = 0;
        distances[1] = 0;
    }

    public Plan getPlan() {
        return plan;
    }

    public boolean isFeasible() {
        return plan.isFeasible();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isDominated(Chromosome rhs) {
        Plan rhsPlan = rhs.getPlan();

        boolean isDominated;
        if (isFeasible() && rhs.isFeasible()) {
            isDominated = feasiblyDominated(rhsPlan);
        } else if (!isFeasible() && !rhs.isFeasible()) {
            isDominated = infeasiblyDominated(rhsPlan);
        } else {
            isDominated = rhs.isFeasible();
        }

        return isDominated;
    }

    private boolean feasiblyDominated(Plan rhsPlan) {
        double x1 = plan.getMaterialUsage();
        double y1 = plan.getSetups();

        double x2 = rhsPlan.getMaterialUsage();
        double y2 = rhsPlan.getSetups();

        return x1 >= x2 && y1 > y2 || x1 > x2 && y1 >= y2;
    }

    private boolean infeasiblyDominated(Plan rhsPlan) {
        return plan.getTotalResidualDemand() > rhsPlan.getTotalResidualDemand();
    }

    public boolean isDominate(Chromosome rhs) {
        Plan rhsPlan = rhs.getPlan();

        boolean isDominate;
        if (isFeasible() && rhs.isFeasible()) {
            isDominate = feasiblyDominate(rhsPlan);
        } else if (!isFeasible() && !rhs.isFeasible()) {
            isDominate = infeasiblyDominate(rhsPlan);
        } else {
            isDominate = isFeasible();
        }

        return isDominate;
    }

    private boolean feasiblyDominate(Plan rhsPlan) {
        double x1 = plan.getMaterialUsage();
        double y1 = plan.getSetups();

        double x2 = rhsPlan.getMaterialUsage();
        double y2 = rhsPlan.getSetups();

        return x1 <= x2 && y1 < y2 || x1 < x2 && y1 <= y2;
    }

    private boolean infeasiblyDominate(Plan rhsPlan) {
        return plan.getTotalResidualDemand() < rhsPlan.getTotalResidualDemand();
    }

    public double getObjective(int index) {
        return index == 0 ? plan.getMaterialUsage() : plan.getSetups();
    }

    public double getDistance() {
        return distances[0] + distances[1];
    }

    public void setDistance(int index, double distance) {
        distances[index] = distance;
    }

    public boolean isBoundary() {
        return isBoundary;
    }

    public void setBoundary(boolean boundary) {
        this.isBoundary = boundary;
    }

    public int getAge() {
        return age;
    }

    public void incAge() {
        age++;
    }

}
