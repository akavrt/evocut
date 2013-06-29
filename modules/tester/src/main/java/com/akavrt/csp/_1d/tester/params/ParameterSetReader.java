package com.akavrt.csp._1d.tester.params;

import com.akavrt.csp._1d.utils.ParameterSet;

import java.io.File;

/**
 * User: akavrt
 * Date: 13.04.13
 * Time: 18:37
 */
public interface ParameterSetReader<T extends ParameterSet> {
    T read(File file);
}
