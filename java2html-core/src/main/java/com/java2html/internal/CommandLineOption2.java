package com.java2html.internal;

import com.java2html.BadOptionException;
import com.java2html.Java2HTML;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;

public class CommandLineOption2 {

    private CommandLine line;
    private final Options options = new Options();

    public CommandLineOption2(String[] args) throws ParseException {

        options.addOption("h", "help", false, "print this help");
        options.addOption("nh", "noheader", false, "prevents header from being displayed");
        options.addOption("nf", "nofooter", false, "prevents footer from being displayed");
        options.addOption("q", "quite", false, "be extra quite");
        options.addOption("s", "simple", false, "Simple output, just include the Java source and stylesheet.css files");

        options.addOption(OptionBuilder.
                withLongOpt("destination")
                .hasArg()
                .withDescription("Destination output directory")
                .create("d"));

        options.addOption(OptionBuilder.
                withLongOpt("name")
                .hasArg()
                .withDescription("Title")
                .create("n"));

        options.addOption(OptionBuilder.withLongOpt("margin")
                .hasArg()
                .withType(Number.class)
                .withDescription("Margin Size (with Line Numbers)")
                .create("m"));

        options.addOption(OptionBuilder.withLongOpt("tabs")
                .hasArg()
                .withType(Number.class)
                .withDescription("Tab Size")
                .create("t"));

        options.addOption(OptionBuilder.withLongOpt("javadoc")
                .hasArgs()
                .withValueSeparator(' ')
                .withDescription("Java Doc HTTP location")
                .create("jd"));

        options.addOption(OptionBuilder.withLongOpt("javasource")
                .hasArgs()
                .withValueSeparator(' ')
                .withDescription("Java Source File Location")
                .create("js"));

        // create the parser
        CommandLineParser parser = new PosixParser();
        // parse the command line arguments
        line = parser.parse(options, args);
    }

    public List<String> getSourceFiles() {
        String[] javaSources = line.getOptionValues("js");
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
        Long margin = (Long) line.getParsedOptionValue("m");
        if (margin == null) {
            margin = 0l;
        }
        return margin.intValue();
    }

    public int getTabCount() throws ParseException {
        Long tabs = (Long) line.getParsedOptionValue("t");
        if (tabs == null) {
            tabs = 4l;
        }
        return tabs.intValue();
    }

    public boolean isQuite() {
        return line.hasOption("q");
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

    private String getTitle() {
        return line.getOptionValue("n");
    }


    /**
     * @return false if just Help was requested
     * @throws com.java2html.BadOptionException
     *
     */
    public boolean setOptionsFromCommandLine(Java2HTML java2HTML) throws
            BadOptionException {
        try {

            if (line.hasOption("h")) {
                // automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("ant", options);
                return false;
            }

            java2HTML.setTitle(getTitle());

            java2HTML.setFooter(noFooter());

            java2HTML.setHeader(noHeader());

            java2HTML.setSimple(isSimple());

            java2HTML.setQuiet(isQuite());

            java2HTML.setMarginSize(getMarginSize());

            java2HTML.setTabSize(getTabCount());

            java2HTML.setDestination(getDestination());

            java2HTML.setJavaDirectorySource(getSourceFiles());

            java2HTML.setJavaDocUrls(getJavaDocUrls());

            return true;

        }
        catch (ParseException e) {
            throw new BadOptionException(e.getMessage());
        }
    }


}
