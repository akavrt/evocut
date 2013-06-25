package com.akavrt.csp._1d.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Element;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 01:35
 */
public abstract class BaseParameters implements ParameterSet {
    private static final Logger LOGGER = LogManager.getLogger(BaseParameters.class);
    private String description;

    protected abstract String getRootElementName();

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void loadFromChildElement(Element element) {
        Element parameterSetElm = element.getChild(getRootElementName());
        if (parameterSetElm != null) {
            load(parameterSetElm);
        } else {
            LOGGER.warn("<{}> element wasn't found.", getRootElementName());
        }
    }
}
