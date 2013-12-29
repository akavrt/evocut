package com.akavrt.csp._1d.results;

import org.jdom2.Element;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;
import java.util.Locale;

/**
 * User: akavrt
 * Date: 27.12.13
 * Time: 22:41
 */
public class ResultUtils {
    public static final DecimalFormat DECIMAL_FORMAT;

    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');

        DECIMAL_FORMAT = new DecimalFormat();
        DECIMAL_FORMAT.setDecimalFormatSymbols(formatSymbols);
        DECIMAL_FORMAT.setGroupingUsed(false);
    }

    public static double parseDoubleFromAttribute(Element element, String attributeName,
                                                  double defaultValue) {
        double value = defaultValue;
        String valueString = element.getAttributeValue(attributeName);
        if (valueString != null && (valueString = valueString.trim()).length() > 0) {
            ParsePosition pp = new ParsePosition(0);
            Number number = DECIMAL_FORMAT.parse(valueString, pp);
            if (number != null && valueString.length() == pp.getIndex()) {
                value = number.doubleValue();
            }
        }

        return value;
    }
}
