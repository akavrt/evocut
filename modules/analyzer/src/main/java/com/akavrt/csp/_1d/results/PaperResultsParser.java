package com.akavrt.csp._1d.results;

import com.akavrt.csp._1d.utils.Utils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * User: akavrt
 * Date: 26.12.13
 * Time: 02:41
 */
public class PaperResultsParser {
    private static final Logger LOGGER = LogManager.getLogger(PaperResultsParser.class);
    private Map<String, CutgenClass> cutgenClasses;
    private Map<CutgenClass, List<PaperResult>> quantityResults;
    private Map<CutgenClass, List<PaperResult>> trimResults;

    public PaperResultsParser() {
        quantityResults = Maps.newTreeMap();
        trimResults = Maps.newTreeMap();
    }

    public Map<CutgenClass, List<PaperResult>> getQuantityResults() {
        return quantityResults;
    }

    public Map<CutgenClass, List<PaperResult>> getTrimResults() {
        return trimResults;
    }

    public void parse(String path) {
        File file = new File(path);
        parse(file);
    }

    public void parse(File file) {
        quantityResults.clear();
        trimResults.clear();

        try {
            if (file.exists() && file.isFile()) {
                readFile(file);
            }
        } catch (Exception e) {
            LOGGER.info("Error occurred while parsing data.");
        }
    }

    private void readFile(File file) {
        try {
            SAXBuilder sax = new SAXBuilder();
            Document document = sax.build(file);
            if (document.hasRootElement()) {
                Element root = document.getRootElement();

                Element classesElement = root.getChild(XmlTags.CLASSES);
                cutgenClasses = parseClasses(classesElement);

                Element sourcesElement = root.getChild(XmlTags.SOURCES);
                parseSources(sourcesElement);
            }
        } catch (JDOMException e) {
            LOGGER.catching(e);
        } catch (IOException e) {
            LOGGER.catching(e);
        }
    }

    private Map<String, CutgenClass> parseClasses(Element classesElement) {
        Map<String, CutgenClass> cutgenClasses = Maps.newHashMap();
        if (classesElement == null) {
            return cutgenClasses;
        }

        List<Element> classElements = classesElement.getChildren(XmlTags.CLASS);
        for (Element classElement : classElements) {
            String name = classElement.getChildText(XmlTags.NAME);
            Element descriptorsElement = classElement.getChild(XmlTags.DESCRIPTORS);
            if (!Utils.isEmpty(name) && descriptorsElement != null) {
                CutgenClass cutgenClass = new CutgenClass(name, descriptorsElement);
                cutgenClasses.put(name, cutgenClass);
            }
        }

        return cutgenClasses;
    }

    private void parseSources(Element sourcesElement) {
        if (sourcesElement == null) {
            return;
        }

        List<Element> sourceElements = sourcesElement.getChildren(XmlTags.SOURCE);
        for (Element sourceElement : sourceElements) {
            parseSource(sourceElement);
        }
    }

    private void parseSource(Element source) {
        String cite = source.getAttributeValue(XmlTags.CITE_ATT);
        if (!Utils.isEmpty(cite)) {
            List<Element> setups = source.getChildren(XmlTags.SETUP);
            int index = 0;
            for (Element setup : setups) {
                parseSetup(cite, ++index, setup);
            }
        }
    }

    private void parseSetup(String cite, int index, Element setup) {
        String trim = setup.getAttributeValue(XmlTags.Z1_ATT);
        boolean isTrim = !Utils.isEmpty(trim) && trim.equalsIgnoreCase(XmlTags.Z1_ATT_VALUE_TRIM);
        List<Element> classes = setup.getChildren(XmlTags.CLASS);
        for (Element classElm : classes) {
            PaperResult result = new PaperResult(cite, index, isTrim, classElm);

            if (result.isValid()) {
                addResult(result);
            }
        }
    }

    private void addResult(PaperResult result) {
        // find CutgenClass by name
        CutgenClass cutgenClass = cutgenClasses.get(result.getCutgenClassName());
        if (cutgenClass == null) {
            return;
        }

        Map<CutgenClass, List<PaperResult>> holder = result.isTrim()
                ? trimResults
                : quantityResults;

        List<PaperResult> results = holder.get(cutgenClass);
        if (results == null) {
            results = Lists.newArrayList();
        }

        results.add(result);
        holder.put(cutgenClass, results);
    }

    private interface XmlTags {
        String NAME = "name";
        String DESCRIPTORS = "descriptors";
        String CLASSES = "classes";
        String SOURCES = "sources";
        String SOURCE = "source";
        String CITE_ATT = "cite";
        String SETUP = "setup";
        String Z1_ATT = "z1";
        String Z1_ATT_VALUE_TRIM = "trim";
        String CLASS = "class";
    }
}
