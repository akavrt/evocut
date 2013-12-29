package com.akavrt.csp._1d.results;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: akavrt
 * Date: 27.12.13
 * Time: 19:40
 */
public class EvoResultsParser {
    private static final Logger LOGGER = LogManager.getLogger(EvoResultsParser.class);
    private List<MethodMod> mods;

    public EvoResultsParser() {
        mods = Lists.newArrayList();
    }

    public List<MethodMod> getMods() {
        return mods;
    }

    public void parse(String path) {
        File file = new File(path);
        parse(file);
    }

    public void parse(File file) {
        mods.clear();

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
                parseMods(root);
            }
        } catch (JDOMException e) {
            LOGGER.catching(e);
        } catch (IOException e) {
            LOGGER.catching(e);
        }
    }

    private void parseMods(Element root) {
        List<Element> elements = root.getChildren(XmlTags.MOD);
        for (Element element : elements) {
            mods.add(new MethodMod(element));
        }
    }

    private interface XmlTags {
        String MOD = "mod";
    }
}
