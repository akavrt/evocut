package com.akavrt.csp._1d.results;

import com.akavrt.csp._1d.utils.Utils;
import org.jdom2.Element;

/**
 * User: akavrt
 * Date: 26.12.13
 * Time: 02:35
 */
public class PaperResult extends Result {
    private String cutgenClassName;
    private String source;
    private int setup;

    public PaperResult(String source, int setup, boolean isTrim, Element element) {
        this.source = source;
        this.setup = setup;
        this.isTrim = isTrim;

        extract(element);
    }

    public String getSource() {
        return source;
    }

    public int getSetup() {
        return setup;
    }

    public String getCutgenClassName() {
        return cutgenClassName;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && !Utils.isEmpty(cutgenClassName);
    }

    private void extract(Element element) {
        cutgenClassName = parseCutgenClassName(element);
        material = parseMaterial(element);
        patterns = parsePatterns(element);
    }

    private String parseCutgenClassName(Element element) {
        return element.getAttributeValue(XmlTags.NAME);
    }

    private double parseMaterial(Element element) {
        return ResultUtils.parseDoubleFromAttribute(element, XmlTags.Z1, 0);
    }

    private double parsePatterns(Element element) {
        return ResultUtils.parseDoubleFromAttribute(element, XmlTags.Z2, 0);
    }

    private interface XmlTags {
        String NAME = "name";
        String Z1 = "z1";
        String Z2 = "z2";
    }
}
