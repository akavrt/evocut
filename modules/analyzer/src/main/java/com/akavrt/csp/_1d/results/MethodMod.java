package com.akavrt.csp._1d.results;

import com.akavrt.csp._1d.utils.Utils;
import com.akavrt.csp._1d.xml.XmlUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jdom2.Element;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: akavrt
 * Date: 27.12.13
 * Time: 17:24
 */
public class MethodMod {
    private String name;
    private String description;
    private Date date;
    private Map<String, List<EvoResult>> data;

    public MethodMod(Element element) {
        data = Maps.newHashMap();
        extractMetadata(element);
        extractClasses(element);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public Map<String, List<EvoResult>> getData() {
        return data;
    }

    public List<EvoResult> getClassResults(String className) {
        return data.get(className);
    }

    private void extractMetadata(Element element) {
        name = element.getAttributeValue(XmlTags.NAME);

        Element descriptionElm = element.getChild(XmlTags.DESCRIPTION);
        if (descriptionElm != null) {
            description = descriptionElm.getText();
        }

        Element dateElm = element.getChild(XmlTags.DATE);
        if (dateElm != null) {
            date = XmlUtils.getDateFromText(dateElm);
        }
    }

    private void extractClasses(Element element) {
        List<Element> classes = element.getChildren(XmlTags.CLASS);
        for (Element cutgenClass : classes) {
            parseClass(cutgenClass);
        }
    }

    private void parseClass(Element cutgenClass) {
        String className = cutgenClass.getAttributeValue(XmlTags.NAME);

        String trim = cutgenClass.getAttributeValue(XmlTags.Z1_ATT);
        boolean isTrim = !Utils.isEmpty(trim) && trim.equalsIgnoreCase(XmlTags.Z1_ATT_VALUE_TRIM);

        List<Element> results = cutgenClass.getChildren(XmlTags.RUN);
        List<EvoResult> extracted = Lists.newArrayList();
        for (Element element : results) {
            EvoResult result = new EvoResult(isTrim, element);
            if (result.isValid()) {
                extracted.add(result);
            }
        }

        List<EvoResult> previous;
        if ((previous = data.get(className)) != null) {
            previous.addAll(extracted);
        } else if (!extracted.isEmpty()) {
            data.put(className, extracted);
        }
    }

    private interface XmlTags {
        String CLASS = "class";
        String NAME = "name";
        String DATE = "date";
        String DESCRIPTION = "description";
        String Z1_ATT = "z1";
        String Z1_ATT_VALUE_TRIM = "trim";
        String RUN = "run";
    }
}
