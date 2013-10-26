package com.src2html;

import com.src2html.internal.Link;
import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandLineOptions {

    private CommandLine line;
    private final Options options = new Options();

    public CommandLineOptions(String[] args) throws ParseException {

        options.addOption("h", "help", false, "print this help");
        options.addOption("nh", "noheader", false, "prevents header from being displayed");
        options.addOption("nf", "nofooter", false, "prevents footer from being displayed");
        options.addOption("q", "quite", false, "be extra quite");
        options.addOption("l", "linenumbers", false, "show line numbers");
        options.addOption("s", "simple", false, "Simple output, will include the Java source and stylesheet.css files");

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

    public List<Link> getJavaDocUrls() {

        String[] values = line.getOptionValues("jd");
        List<Link> links = new ArrayList<Link>();
        if (values != null) for (String value : values) {
            links.add(new Link(value));
        }

        return Collections.unmodifiableList(links);
    }

    public String getDestination() {
        return line.getOptionValue("d", ".");
    }

    public boolean isShowLineNumbers() {
        return line.hasOption("l");
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

    public String getUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("j2h", options);
        return null;
    }

    public String getHelp() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "j2h", options);
        return null;
    }


    /**
     * @return false if just Help was requested
     * @throws BadOptionException
     *
     */
    public boolean setOptionsFromCommandLine(Src2Html src2Html) throws
            BadOptionException {
        try {

            if (line.hasOption("h")) {
                // automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("j2h", options);
                return false;
            }

            src2Html.setTitle(getTitle());

            src2Html.setFooter(noFooter());

            src2Html.setHeader(noHeader());

            src2Html.setSimple(isSimple());

            src2Html.setQuiet(isQuite());

            src2Html.setShowLineNumbers(isShowLineNumbers());

            src2Html.setTabSize(getTabCount());

            src2Html.setDestinationDir(getDestination());

            src2Html.setJavaDirectorySource(getSourceFiles());

            src2Html.setJavaDocLinks(getJavaDocUrls());

            return true;

        }
        catch (ParseException e) {
            throw new BadOptionException(e.getMessage());
        }
    }


}
