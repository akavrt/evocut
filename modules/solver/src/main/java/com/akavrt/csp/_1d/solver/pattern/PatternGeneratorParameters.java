package com.akavrt.csp._1d.solver.pattern;

import com.akavrt.csp._1d.utils.BaseParameters;
import com.akavrt.csp._1d.utils.Utils;
import com.akavrt.csp._1d.utils.XmlUtils;
import org.jdom2.Element;

/**
 * <p>Parameters of the randomized multistart pattern generation procedure, see
 * ConstrainedPatternGenerator for more details.</p>
 *
 * <p>An instance of this class can be saved to XML and extracted from it using methods defined in
 * XmlCompatible interface.</p>
 *
 * @author Victor Balabanov <akavrt@gmail.com>
 */
public class PatternGeneratorParameters extends BaseParameters {
    private static final int DEFAULT_GENERATION_TRIALS_LIMIT = 20;
    private int generationTrialsLimit = DEFAULT_GENERATION_TRIALS_LIMIT;

    /**
     * <p>Maximum number of trials can be used by procedure while generating pattern.</p>
     */
    public int getGenerationTrialsLimit() {
        return generationTrialsLimit;
    }

    /**
     * <p>Set the maximum number of trials can be used by procedure while generating pattern.</p>
     *
     * @param generationTrialsLimit Maximum number of generation trials.
     */
    public void setGenerationTrialsLimit(int generationTrialsLimit) {
        this.generationTrialsLimit = generationTrialsLimit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element save() {
        Element paramsElm = new Element(getRootElementName());

        // optional description
        if (!Utils.isEmpty(getDescription())) {
            Element descriptionElm = new Element(XmlTags.DESCRIPTION);
            descriptionElm.setText(getDescription());
            paramsElm.addContent(descriptionElm);
        }

        Element generationTrialsLimitElm = new Element(XmlTags.GENERATION_TRIALS_LIMIT);
        generationTrialsLimitElm.setText(Integer.toString(getGenerationTrialsLimit()));
        paramsElm.addContent(generationTrialsLimitElm);

        return paramsElm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void load(Element rootElm) {
        String description = rootElm.getChildText(XmlTags.DESCRIPTION);
        if (!Utils.isEmpty(description)) {
            setDescription(description);
        }

        Element limitElm = rootElm.getChild(XmlTags.GENERATION_TRIALS_LIMIT);
        if (limitElm != null) {
            int limit = XmlUtils.getIntegerFromText(limitElm, DEFAULT_GENERATION_TRIALS_LIMIT);
            setGenerationTrialsLimit(limit);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRootElementName() {
        return XmlTags.PATTERN;
    }

    private interface XmlTags {
        String PATTERN = "pattern";
        String GENERATION_TRIALS_LIMIT = "generation-trials-limit";
        String DESCRIPTION = "description";
    }

}
