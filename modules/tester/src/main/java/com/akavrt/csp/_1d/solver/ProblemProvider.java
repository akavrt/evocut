package com.akavrt.csp._1d.solver;

import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.cutgen.ProblemGenerator;
import com.akavrt.csp._1d.cutgen.PseudoRandom;

import java.util.Iterator;

/**
 * User: akavrt
 * Date: 28.06.13
 * Time: 21:18
 */
public class ProblemProvider implements Iterator<Problem>, Iterable<Problem> {
    private final ProblemClass classDescriptors;
    private ProblemGenerator generator;
    private int generated;

    public ProblemProvider(ProblemClass classDescriptors) {
        this.classDescriptors = classDescriptors;
        initGenerator();
    }

    private void initGenerator() {
        PseudoRandom rGen = new PseudoRandom(classDescriptors.getSeed());
        generator = new ProblemGenerator(rGen, classDescriptors.getProblemDescriptors());
    }

    public void reset() {
        generated = 0;
        initGenerator();
    }

    @Override
    public boolean hasNext() {
        return generated < classDescriptors.getSize();
    }

    @Override
    public Problem next() {
        generated++;

        return generator.nextProblem();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Removal doesn't supported.");
    }

    @Override
    public Iterator<Problem> iterator() {
        return this;
    }

}
