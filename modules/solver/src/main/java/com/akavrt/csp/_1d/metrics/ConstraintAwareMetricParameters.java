package com.akavrt.csp._1d.metrics;

import com.akavrt.csp._1d.utils.BaseParameters;
import com.akavrt.csp._1d.utils.Utils;
import com.akavrt.csp._1d.utils.XmlUtils;
import org.jdom2.Element;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 01:32
 */
public class ConstraintAwareMetricParameters extends BaseParameters {
    private static final double DEFAULT_AGGREGATED_TRIM_FACTOR = 0.5;
    private static final double DEFAULT_PATTERNS_FACTOR = 0.5;
    private double aggregatedTrimFactor = DEFAULT_AGGREGATED_TRIM_FACTOR;
    private double patternsFactor = DEFAULT_PATTERNS_FACTOR;


    public double getAggregatedTrimFactor() {
        return aggregatedTrimFactor;
    }


    public void setAggregatedTrimFactor(double aggregatedTrimFactor) {
        this.aggregatedTrimFactor = aggregatedTrimFactor;
    }


    public double getPatternsFactor() {
        return patternsFactor;
    }


    public void setPatternsFactor(double patternsFactor) {
        this.patternsFactor = patternsFactor;
    }


    @Override
    public Element save() {
        Element constraintScalarElm = new Element(XmlTags.CONSTRAINT_SCALAR);

        // optional description
        if (!Utils.isEmpty(getDescription())) {
            Element descriptionElm = new Element(XmlTags.DESCRIPTION);
            descriptionElm.setText(getDescription());
            constraintScalarElm.addContent(descriptionElm);
        }

        Element aggregatedTrimWeightElm = new Element(XmlTags.AGGREGATED_TRIM_WEIGHT);
        aggregatedTrimWeightElm.setText(XmlUtils.formatDouble(getAggregatedTrimFactor()));
        constraintScalarElm.addContent(aggregatedTrimWeightElm);

        Element patternWeightElm = new Element(XmlTags.PATTERN_WEIGHT);
        patternWeightElm.setText(XmlUtils.formatDouble(getPatternsFactor()));
        constraintScalarElm.addContent(patternWeightElm);

        return constraintScalarElm;
    }

    @Override
    public void load(Element rootElm) {
        String description = rootElm.getChildText(XmlTags.DESCRIPTION);
        if (!Utils.isEmpty(description)) {
            setDescription(description);
        }

        double aggregatedTrimWeight = XmlUtils.getDoubleFromText(rootElm,
                                                                 XmlTags.AGGREGATED_TRIM_WEIGHT,
                                                                 DEFAULT_AGGREGATED_TRIM_FACTOR);
        setAggregatedTrimFactor(aggregatedTrimWeight);

        double patternWeight = XmlUtils.getDoubleFromText(rootElm, XmlTags.PATTERN_WEIGHT,
                                                          DEFAULT_PATTERNS_FACTOR);
        setPatternsFactor(patternWeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRootElementName() {
        return XmlTags.CONSTRAINT_SCALAR;
    }

    private interface XmlTags {
        String CONSTRAINT_SCALAR = "constraint-scalar";
        String AGGREGATED_TRIM_WEIGHT = "aggregated-trim-weight";
        String PATTERN_WEIGHT = "pattern-weight";
        String DESCRIPTION = "description";
    }

}
