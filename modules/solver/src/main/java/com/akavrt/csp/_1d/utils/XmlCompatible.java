package com.akavrt.csp._1d.utils;

import org.jdom2.Element;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 01:33
 */
public interface XmlCompatible {
    public Element save();
    public void load(Element element);
}
