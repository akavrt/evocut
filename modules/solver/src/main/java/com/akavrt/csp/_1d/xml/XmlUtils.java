package com.akavrt.csp._1d.xml;

import com.akavrt.csp._1d.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Element;

import java.text.*;
import java.util.Date;
import java.util.Locale;

/**
 * User: akavrt
 * Date: 26.06.13
 * Time: 01:38
 */
public class XmlUtils {
    private static final Logger LOGGER = LogManager.getLogger(XmlUtils.class);
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    private static final DecimalFormat DECIMAL_FORMAT;
    private static DateFormat DATE_FORMAT;

    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');

        DECIMAL_FORMAT = new DecimalFormat();
        DECIMAL_FORMAT.setDecimalFormatSymbols(formatSymbols);
        DECIMAL_FORMAT.setGroupingUsed(false);

        DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.ENGLISH);
    }

    public static String formatDouble(double value) {
        return DECIMAL_FORMAT.format(value);
    }

    public static double getDoubleFromText(Element element, double defaultValue) {
        double value = defaultValue;
        String valueString = element.getText();
        if (!Utils.isEmpty(valueString)) {
            ParsePosition pp = new ParsePosition(0);
            Number number = DECIMAL_FORMAT.parse(valueString, pp);
            if (number != null && valueString.length() == pp.getIndex()) {
                value = number.doubleValue();
            }
        }
        return value;
    }

    public static double getDoubleFromText(Element parent, String childName, double defaultValue) {
        double value = defaultValue;
        String valueString = parent.getChildText(childName);
        if (!Utils.isEmpty(valueString)) {
            ParsePosition pp = new ParsePosition(0);
            Number number = DECIMAL_FORMAT.parse(valueString, pp);
            if (number != null && valueString.length() == pp.getIndex()) {
                value = number.doubleValue();
            }
        }

        return value;
    }

    public static int getIntegerFromText(Element element, int defaultValue) {
        int value = defaultValue;
        String valueString = element.getText();
        if (!Utils.isEmpty(valueString)) {
            try {
                value = Integer.parseInt(valueString);
            } catch (NumberFormatException e) {
                LOGGER.catching(e);
            }
        }

        return value;
    }

    public static int getIntegerFromText(Element parent, String childName, int defaultValue) {
        int value = defaultValue;
        String valueString = parent.getChildText(childName);
        if (!Utils.isEmpty(valueString)) {
            try {
                value = Integer.parseInt(valueString);
            } catch (NumberFormatException e) {
                LOGGER.catching(e);
            }
        }

        return value;
    }

    public static int getIntegerFromAttribute(Element element, String attributeName,
                                              int defaultValue) {
        int value = defaultValue;
        String valueString = element.getAttributeValue(attributeName);
        if (!Utils.isEmpty(valueString)) {
            try {
                value = Integer.parseInt(valueString);
            } catch (NumberFormatException e) {
                LOGGER.catching(e);
            }
        }

        return value;
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static Date getDateFromText(Element element) {
        Date date = null;

        String valueString = element.getText();
        if (!Utils.isEmpty(valueString)) {
            ParsePosition pp = new ParsePosition(0);
            date = DATE_FORMAT.parse(valueString, pp);
            if (valueString.length() != pp.getIndex()) {
                date = null;
            }
        }

        return date;
    }

}
