package com.akavrt.csp._1d.solver;

import com.akavrt.csp._1d.cutgen.ProblemDescriptors;
import com.akavrt.csp._1d.cutgen.PseudoRandom;
import com.akavrt.csp._1d.utils.Utils;
import com.akavrt.csp._1d.xml.XmlCompatible;
import com.akavrt.csp._1d.xml.XmlUtils;
import org.jdom2.Element;

/**
 * User: akavrt
 * Date: 28.06.13
 * Time: 01:36
 */
public class ProblemClass implements XmlCompatible {
    private ProblemDescriptors problemDescriptors;
    private int size;
    private int seed;
    private String name;
    private String description;

    public ProblemClass(Element element) {
        load(element);
    }

    public ProblemClass(int size, ProblemDescriptors problemDescriptors) {
        this(size, problemDescriptors, PseudoRandom.DEFAULT_SEED, null, null);
    }

    public ProblemClass(int size, ProblemDescriptors problemDescriptors, int seed) {
        this(size, problemDescriptors, seed, null, null);
    }

    public ProblemClass(int size, ProblemDescriptors problemDescriptors, int seed, String name) {
        this(size, problemDescriptors, seed, name, null);
    }

    public ProblemClass(int size, ProblemDescriptors problemDescriptors, int seed, String name,
                        String description) {
        this.size = size;
        this.problemDescriptors = problemDescriptors;
        this.seed = seed;
        this.name = name;
        this.description = description;
    }

    public ProblemDescriptors getProblemDescriptors() {
        return problemDescriptors;
    }

    /**
     * <p>Returns number of problems in a specific class defined by an instance of
     * ProblemDescriptors.</p>
     */
    public int getSize() {
        return size;
    }

    public int getSeed() {
        return seed;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Element save() {
        Element classElm = new Element(XmlTags.CLASS);

        Element sizeElm = new Element(XmlTags.CLASS_SIZE);
        sizeElm.setText(Integer.toString(size));
        classElm.addContent(sizeElm);

        Element seedElm = new Element(XmlTags.SEED);
        seedElm.setText(Integer.toString(seed));
        classElm.addContent(seedElm);

        if (problemDescriptors != null) {
            Element descriptorsElm = problemDescriptors.save();
            classElm.addContent(descriptorsElm);
        }

        // optional name
        if (!Utils.isEmpty(name)) {
            Element nameElm = new Element(XmlTags.NAME);
            classElm.addContent(nameElm);
            nameElm.setText(name);
        }

        // optional description
        if (!Utils.isEmpty(description)) {
            Element descriptionElm = new Element(XmlTags.DESCRIPTION);
            descriptionElm.setText(description);
            classElm.addContent(descriptionElm);
        }

        return classElm;
    }

    @Override
    public void load(Element rootElm) {
        Element classSizeElm = rootElm.getChild(XmlTags.CLASS_SIZE);
        if (classSizeElm != null) {
            size = XmlUtils.getIntegerFromText(classSizeElm, 1);
        }

        Element seedElm = rootElm.getChild(XmlTags.SEED);
        if (seedElm != null) {
            seed = XmlUtils.getIntegerFromText(seedElm, PseudoRandom.DEFAULT_SEED);
        }

        name = rootElm.getChildText(XmlTags.NAME);
        description = rootElm.getChildText(XmlTags.DESCRIPTION);

        Element descriptorsElm = rootElm.getChild(XmlTags.DESCRIPTORS);
        if (descriptorsElm != null) {
            problemDescriptors = new ProblemDescriptors(descriptorsElm);
        }
    }

    private interface XmlTags {
        String CLASS = "class";
        String CLASS_SIZE = "class-size";
        String SEED = "seed";
        String NAME = "name";
        String DESCRIPTION = "description";
        String DESCRIPTORS = "descriptors";
    }

}
