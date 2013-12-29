package com.akavrt.csp._1d.results;

import com.akavrt.csp._1d.cutgen.ProblemDescriptors;
import org.jdom2.Element;

/**
 * User: akavrt
 * Date: 29.12.13
 * Time: 22:58
 */
public class CutgenClass extends ProblemDescriptors implements Comparable<CutgenClass> {
    private final String name;

    public CutgenClass(String name, Element element) {
        super(element);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CutgenClass)) {
            return false;
        }

        CutgenClass rhs = (CutgenClass) o;

        return hashCode() == rhs.hashCode();
    }

    @Override
    public int compareTo(CutgenClass cutgenClass) {
        return (name == null || cutgenClass.name == null) ? 0 : name.compareTo(cutgenClass.name);
    }
}
