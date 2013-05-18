package com.java2html.internal;

import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;

public class CommandLineOption2 {

    private CommandLine line;

    public CommandLineOption2(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("nh", "noheader", false, "prevents header from being displayed");
        options.addOption("nf", "nofooter", false, "prevents footer from being displayed");
        options.addOption("q", "quite", false, "be extra quite");
        options.addOption("s", "simple", false, "Simple output, just include the Java source and stylesheet.css files");

        options.addOption(OptionBuilder.
                withLongOpt("destination")
                .hasArg()
                .withDescription("Destination output directory")
                .create("d"));

        options.addOption(OptionBuilder.withLongOpt("margin")
                .hasArg()
                .withType(new Integer(0))
                .withDescription("Margin Size (with Line Numbers)")
                .create("m"));

        options.addOption(OptionBuilder.withLongOpt("tabs")
                .hasArg()
                .withType(new Integer(0))
                .withDescription("Tab Size")
                .create("t"));

        options.addOption(OptionBuilder.withLongOpt("javadoc")
                .hasArgs()
                .withValueSeparator(',')
                .withDescription("Java Doc HTTP location")
                .create("jd"));

        options.addOption(OptionBuilder.withLongOpt("javasource")
                .hasArgs()
                .withValueSeparator(',')
                .withDescription("Java Source File Location")
                .create("js"));

        // create the parser
        CommandLineParser parser = new PosixParser();
        // parse the command line arguments
        line = parser.parse(options, args);
    }

    public List<String> getSourceFiles() {
        String[] javaSources = line.getOptionValues("jd");
        if (javaSources == null) {
            javaSources = new String[]{"."};
        }
        return Arrays.asList(javaSources);

    }

    public List<String> getJavaDocUrls() {
        String[] values = line.getOptionValues("jd");
        return Arrays.asList(values != null ? values : new String[0]);
    }

    public String getDestination() {
        return line.getOptionValue("d", ".");
    }

    public int getMarginSize() throws ParseException {
        Integer margin = (Integer) line.getParsedOptionValue("m");
        if (margin == null) {
            margin = 0;
        }
        return margin;
    }

    public int getTabCount() throws ParseException {
        Integer tabcount = (Integer) line.getParsedOptionValue("t");
        if (tabcount == null) {
            tabcount = 4;
        }
        return tabcount;
    }

    public boolean isQuite() {
        return line.hasOption("v");
    }

    public boolean isSimple() {
        return line.hasOption("s");
    }

    public boolean noHeader() {
        return line.hasOption("nh");
    }

    public boolean noFooter() {
        return line.hasOption("nf");
    }

}
