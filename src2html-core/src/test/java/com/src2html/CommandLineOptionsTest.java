package com.src2html;


import com.src2html.internal.Link;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommandLineOptionsTest {

    private static final List<String> DEFAULT = Arrays.asList(".");
    private static final List<Link> EMPTY = Collections.emptyList();

    static class Options {
        boolean noHeader;
        boolean noFooter;
        boolean isSimple;
        boolean isQuite;
        List<Link> javaDoc;
        List<String> javaSource;
        String destination;
        int tabSize;
        boolean showLineNumbers;

        Options(String destination, List<String> javaSource, List<Link> javaDoc, boolean showLineNumbers, int tabSize, boolean noHeader, boolean noFooter, boolean simple, boolean quite) {
            this.destination = destination;
            isQuite = quite;
            isSimple = simple;
            this.javaDoc = javaDoc;
            this.javaSource = javaSource;
            this.showLineNumbers = showLineNumbers;
            this.noFooter = noFooter;
            this.noHeader = noHeader;
            this.tabSize = tabSize;
        }

        static Options create(String destination,
                                      List<String> javaSource,
                                      List<Link> javaDoc,
                                      boolean showLineNumbers,
                                      int tabSize) {
            return new Options(destination, javaSource, javaDoc, showLineNumbers, tabSize, false, false, false, false);
        }

        static Options create(String destination,
                              List<String> javaSource,
                              List<Link> javaDoc,
                              boolean showLineNumbers,
                              int tabSize,
                              boolean noHeader,
                              boolean noFooter,
                              boolean isSimple,
                              boolean isQuite
        ) {
            return new Options(destination, javaSource, javaDoc, showLineNumbers, tabSize, noHeader, noFooter, isSimple, isQuite);

        }
    }


    @Test
    public void test() throws ParseException {

        checkValues("", Options.create(".", DEFAULT, EMPTY, false, 4, false, false, false, false));

        checkValues("-s", Options.create(".", DEFAULT, EMPTY, false, 4, false, false, true, false));

        checkValues("-q", Options.create(".", DEFAULT, EMPTY, false, 4, false, false, false, true));

        checkValues("-sq", Options.create(".", DEFAULT, EMPTY, false, 4, false, false, true, true));

        checkValues("-nh", Options.create(".", DEFAULT, EMPTY, false, 4, true, false, false, false));

        checkValues("-nf", Options.create(".", DEFAULT, EMPTY, false, 4, false, true, false, false));


        List<String> expectedSources = Arrays.asList("a/b/c","a","b","c");
        List<Link> expectedJavaDoc = Arrays.asList(new Link("e"),new Link("f"));
        checkValues("-js a/b/c a b c", Options.create(".",expectedSources, EMPTY,false,4));

        checkValues("-js a/b/c a b c -jd e f", Options.create(".",expectedSources, expectedJavaDoc,false,4));

        List<String> expectedSourcesWithTwoSets = Arrays.asList("a/b/c","a","b","c", "g", "h");
        checkValues("-js a/b/c a b c -jd e f -js g h", Options.create(".",expectedSourcesWithTwoSets, expectedJavaDoc,false,4));

        checkValues("-javasource a/b/c a b c -jd e f -js g h", Options.create(".",expectedSourcesWithTwoSets, expectedJavaDoc,false,4));

        checkValues("-javasource a/b/c a b c -l -jd e f -js g h", Options.create(".",expectedSourcesWithTwoSets, expectedJavaDoc,true,4));
    }

    @Test
    public void testUsage() throws ParseException, BadOptionException {
        CommandLineOptions clo = new CommandLineOptions(new String[]{ "-h"});
        clo.setOptionsFromCommandLine(new Java2HTML());

    }

    private void checkValues(String args,
                             Options options) throws ParseException {

        CommandLineOptions clo = new CommandLineOptions(args.split(" "));
        assertEquals("destination not same", options.destination, clo.getDestination());
        assertEquals("", options.noHeader, clo.noHeader());
        assertEquals("", options.noFooter, clo.noFooter());
        assertEquals("", options.isSimple, clo.isSimple());
        assertEquals("", options.isQuite, clo.isQuite());
        assertEquals("", options.javaDoc, clo.getJavaDocUrls());
        assertEquals("", options.javaSource, clo.getSourceFiles());
        assertEquals("", options.tabSize, clo.getTabCount());
        assertEquals("", options.showLineNumbers, clo.isShowLineNumbers());
    }


}
