package com.akavrt.csp._1d.results;

import org.jdom2.Element;

/**
 * User: akavrt
 * Date: 27.12.13
 * Time: 17:50
 */
public class EvoResult extends Result {
    private double materialWeight;
    private double patternsWeight;

    public EvoResult(boolean isTrim, Element element) {
        this.isTrim = isTrim;

        extract(element);
    }

    public double getMaterialWeight() {
        return materialWeight;
    }

    public double getPatternsWeight() {
        return patternsWeight;
    }

    private void extract(Element element) {
        material = ResultUtils.parseDoubleFromAttribute(element, XmlTags.Z1, 0);
        patterns = ResultUtils.parseDoubleFromAttribute(element, XmlTags.Z2, 0);

        materialWeight = ResultUtils.parseDoubleFromAttribute(element, XmlTags.C1, 0);
        patternsWeight = ResultUtils.parseDoubleFromAttribute(element, XmlTags.C2, 0);
    }

    private interface XmlTags {
        String C1 = "c1";
        String C2 = "c2";
        String Z1 = "z1";
        String Z2 = "z2";
    }
}
