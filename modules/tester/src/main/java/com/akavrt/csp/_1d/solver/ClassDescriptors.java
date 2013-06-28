package com.akavrt.csp._1d.solver;

import com.akavrt.csp._1d.cutgen.ProblemDescriptors;
import com.akavrt.csp._1d.utils.Utils;
import com.akavrt.csp._1d.xml.XmlCompatible;
import com.akavrt.csp._1d.xml.XmlUtils;
import org.jdom2.Element;

/**
 * User: akavrt
 * Date: 28.06.13
 * Time: 01:36
 */
public class ClassDescriptors implements XmlCompatible {
    private ProblemDescriptors problemDescriptors;
    private int size;
    private String name;
    private String description;

    public ClassDescriptors(ProblemDescriptors problemDescriptors, int size) {
        this(problemDescriptors, size, null, null);
    }

    public ClassDescriptors(ProblemDescriptors problemDescriptors, int size, String name) {
        this(problemDescriptors, size, name, null);
    }

    public ClassDescriptors(ProblemDescriptors problemDescriptors, int size,
                            String name, String description) {
        this.problemDescriptors = problemDescriptors;
        this.size = size;
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
            size = XmlUtils.getIntegerFromText(classSizeElm, 0);
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
        String NAME = "name";
        String DESCRIPTION = "description";
        String DESCRIPTORS = "descriptors";
    }

}
