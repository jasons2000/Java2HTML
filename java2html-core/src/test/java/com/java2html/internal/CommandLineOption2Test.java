package com.java2html.internal;


import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class CommandLineOption2Test {

    private static final List<String> DEFAULT = Arrays.asList(".");
    private static final List<String> EMPTY = Collections.EMPTY_LIST;

    static class Options {
        boolean noHeader;
        boolean noFooter;
        boolean isSimple;
        boolean isQuite;
        List<String> javaDoc;
        List<String> javaSource;
        String destination;
        int tabSize;
        int margin;

        Options(String destination, List<String> javaSource, List<String> javaDoc, int margin, int tabSize, boolean noHeader, boolean noFooter, boolean simple, boolean quite) {
            this.destination = destination;
            isQuite = quite;
            isSimple = simple;
            this.javaDoc = javaDoc;
            this.javaSource = javaSource;
            this.margin = margin;
            this.noFooter = noFooter;
            this.noHeader = noHeader;
            this.tabSize = tabSize;
        }

        static Options create(String destination,
                                      List<String> javaSource,
                                      List<String> javaDoc,
                                      int margin,
                                      int tabSize) {
            return new Options(destination, javaSource, javaDoc, margin, tabSize, false, false, false, false);
        }

        static Options create(String destination,
                              List<String> javaSource,
                              List<String> javaDoc,
                              int margin,
                              int tabSize,
                              boolean noHeader,
                              boolean noFooter,
                              boolean isSimple,
                              boolean isQuite
        ) {
            return new Options(destination, javaSource, javaDoc, margin, tabSize, noHeader, noFooter, isSimple, isQuite);

        }
    }


    @Test
    public void test() throws ParseException {
        String[] args = {};
        checkValues(args, Options.create(".", DEFAULT, EMPTY, 0, 4, false, false, false, false));

        List<String> expectedSources = Arrays.asList("a/b/c","a","b","c");

        checkValues("-js a/b/c,a,b,c".split(" "), Options.create(".",expectedSources, EMPTY,0,4));

    }


    private void checkValues(String[] args,
                             Options options) throws ParseException {

        CommandLineOption2 clo = new CommandLineOption2(args);
        assertEquals("destination not same", options.destination, clo.getDestination());
        assertEquals("", options.noHeader, clo.noHeader());
        assertEquals("", options.noFooter, clo.noFooter());
        assertEquals("", options.isSimple, clo.isSimple());
        assertEquals("", options.isQuite, clo.isQuite());
        assertEquals("", options.javaDoc, clo.getJavaDocUrls());
        assertEquals("", options.javaSource, clo.getSourceFiles());
        assertEquals("", options.tabSize, clo.getTabCount());
        assertEquals("", options.margin, clo.getMarginSize());
    }


}
