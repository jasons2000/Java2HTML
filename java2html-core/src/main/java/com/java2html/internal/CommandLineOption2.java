package com.java2html.internal;

import org.apache.commons.cli.*;

public class CommandLineOption2 {

    public CommandLineOption2(String[] args) {
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
                .withDescription("Margin Size (with Line Numbers)")
                .create("m"));

        options.addOption(OptionBuilder.withLongOpt("tabs")
                .hasArg()
                .withDescription("Tab Size")
                .create("t"));

        options.addOption(OptionBuilder.withLongOpt("javadoc")
                .hasArgs()
                .withValueSeparator(',')
                .withDescription("")
                .create("jd"));

        options.addOption(OptionBuilder.withLongOpt("javasource")
                .hasArgs()
                .withValueSeparator(',')
                .withDescription("Tab Size")
                .create("js"));

        // create the parser
        CommandLineParser parser = new PosixParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

        }
        catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }


    }
}
