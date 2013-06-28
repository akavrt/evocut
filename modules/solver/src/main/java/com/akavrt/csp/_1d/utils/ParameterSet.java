package com.akavrt.csp._1d.utils;

import com.akavrt.csp._1d.xml.XmlCompatible;
import org.jdom2.Element;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 01:34
 */
public interface ParameterSet extends XmlCompatible {
    String getDescription();
    void setDescription(String description);
    void loadFromChildElement(Element element);
}
