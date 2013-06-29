package com.akavrt.csp._1d.solver;

import com.akavrt.csp._1d.cutgen.ProblemDescriptors;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: akavrt
 * Date: 28.06.13
 * Time: 21:50
 */
public class ProblemProviderTest {
    private ProblemClass classDescriptors;
    private ProblemProvider provider;

    @Before
    public void setupProvider() {
        ProblemDescriptors problemDescriptors = new ProblemDescriptors(10, 100, 0.2, 0.4, 40);
        classDescriptors = new ProblemClass(100, problemDescriptors);
        provider = new ProblemProvider(classDescriptors);
    }

    @Test
    public void iterator() {
        int actualBatchSize = 0;
        while (provider.hasNext()) {
            provider.next();
            actualBatchSize++;
        }

        assertEquals(classDescriptors.getSize(), actualBatchSize);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removal() {
        provider.remove();
    }
}
