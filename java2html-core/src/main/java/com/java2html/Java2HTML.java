/*
 * Copyright (c) 1999-2007, Enterprise Solution Consultants Limited, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

/*
 * BIG TODO - In order of priorty
 * run peformace  analyzer
 * JavaDoc should be able to scan http:// refrences for JavaDoc doco
 * Tidy up option - ie check before commencing processing
 * Add SQL support ( all possible versions)
 * C++/ C++ etc..
 * change title default colour to line nuber colour
 * Specifi Properites files with default that are overidden by commandlines
 * link instances to declaration
 * link methods ( probably use exta frame)
 * add extracr fram for class summart of method & fields an inner classes (possiblt)
 * link import declarariton
 * Class list frame will need extra link for class summary ( can u have two targets )
 * Single Java File
 * Add Top right hand field indicating type (Java but in the future will include SQL)
 * Allow Thridr PArty Plugins
 * Simpler single file option , eg j2h AFile.java, j2h AFile.java AFile2.java, j2h A????.java
 * Specifiy File criteria option (accepting wild cards) as above
 * better error handling of non conformant JAva , use red highlighting
 * get rid of nasty globalvariable/ not worthwhile ?!
 * What if two java sources are supplied with duplicate directory names !?
 * specify directory list to parse
 * index files should appear in directories
 * add sophisicated header option
 * CR for all generated HTML should use OS CR
 * sort small D: d: problem
     * [DEFAULT] should be proceded by a directory name to distinigush it from others
 * check C: C:/ probelm when creating support HTML files
 * missing create files
 */
package com.java2html;

import com.java2html.ant.Link;
import com.java2html.internal.CommandLineOptions;
import com.java2html.internal.Helper;
import com.java2html.java_parser.JavaDocManager;
import com.java2html.java_parser.JavaSource;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

/**
 * Generates Java2HTML output
 *
 * TODO
 * Hi
 *  property expansion
 *  fix umlat and foreign chars problem
 *  create test java which use javadoc sample package and class
 *  write unit test case which tests javadoc links using webunit or jsoup
 *  create ITCase for general results,
 *     test each frame
 *     link to full
 *     ant test
 *     mojo test
 *
 * Low
 *   support other languages,
 *      reorg for dir for src2html - maintain old class, and ant task integration
 *       abstract reference lookup with Type and matching String
 *       0th parse detects sources
 *       1st parse build up all com.java2html.references
 *       2nd parse detectect com.java2html.references
 *
 *
 *
 */

public class Java2HTML {

    // Options
    private int marginSize = 0;
    private int tabSize = 4;

    private boolean header = true;
    private boolean footer = true;
    private boolean simple = false;
    private boolean quiet  = false;

//    private List<ReferenceParser> referenceParsers = new ArrayList<ReferenceParser>();
//    private List<SourceParser> sourceParsers = new ArrayList<SourceParser>();

    private String title = "Java Source";

    private List<String> javaSourceFileList = null;

    private List<Link> javaDocOptionLinks = Collections.emptyList();

    private String destination = "output";
    public static ResourceBundle bundle = ResourceBundle.getBundle("general_text");

    /**
     * Called by Command Line Wrappers
     *
     * @param options
     */
    public static void main(String[] options) {

        int success = 0;

        System.out.println(Helper.version);
        System.out.println(bundle.getString("copyright"));

        Java2HTML java2HTML = new Java2HTML();

        try {
            CommandLineOptions cmd = new CommandLineOptions(options);
            if (!cmd.setOptionsFromCommandLine(java2HTML)) {
                return; // return if just asking for help
            }
            java2HTML.buildJava2HTML();
        }
        catch (Exception e) {
            final String msg = e.getMessage();
            System.err.print( msg != null ? msg : e.toString() );
            success = 1;
             e.printStackTrace();
        }
        System.exit(success);
    }

    /**
     * Builds the Java2HTML
     *
     * returns false if any failures were detected
     */
    public boolean buildJava2HTML() throws IOException, BadOptionException {

        createSupportingFiles();

        JavaDocManager javaDoc = new JavaDocManager(javaDocOptionLinks.toArray(new Link[0]));

        if (javaSourceFileList == null) {
            setJavaDirectorySource(Arrays.asList("."));
        }
        // Performs first parse
        JavaSource javaSource = new JavaSource(javaSourceFileList, destination, marginSize,
                                    tabSize, header, footer, javaDoc);
        if (!simple) javaSource.createPackageIndex(destination, title);
        javaSource.setQuiet( quiet );

        javaSource.generateJava2HTML();

        return true;
    }

    private void createSupportingFiles() throws IOException {

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                        DateFormat.SHORT);

        HashMap<String,String> subs= new HashMap<String,String>();
        subs.put("date", df.format(new Date()));
        subs.put("version", "1.0");
        subs.put("title", title);


        Helper helper = new Helper(destination, subs, quiet);

        //Create StyleSheet
        File f = new File(destination + "/stylesheet.css");
        FileUtils.copyURLToFile(getClass().getResource("/stylesheet.css"), f);


        if ( !quiet ) System.out.println("Created: " + f.getAbsolutePath());

        // Check File.Separator

        if (!simple) {

            helper.createPage("front.html");
            // Create main Index.html
            helper.createPage("index.html");

        }
    }

    // setters


    /**
     * Set the margin size that should be generated when buildJava2HTML() is called.
     *
     * @param marginSize Margin Size
     */
    public void setMarginSize(int marginSize) {
        this.marginSize = marginSize;
    }

    /**
     * Set the number of spaces that tabs will be converted
     * to when buildJava2HTML() is called.
     *
     * @param tabSize Number of spaces tabs should be converted to.
     */
    public void setTabSize(int tabSize) {
        this.tabSize = tabSize;
    }

    /**
     * Determines if a header should be generated when buildJava2HTML() is called.
     *
     * @param header set to true if a header is required
     */
    public void setHeader(boolean header) {
        this.header = header;
    }
    /**
     * Determines if only the java source and stylesheet files will be output
     *
     * @param simple set to true for simple output
     */
    public void setSimple(boolean simple) {
        this.simple = simple;
    }

    /**
     * Determines if a footer should be generated when buildJava2HTML() is called.
     *
     * @param footer set to true if a header is required
     */
    public void setFooter(boolean footer) {
        this.footer = footer;
    }


    /**
     * Set the title that will be used when buildJava2HTML() is called.
     *
     * @param title The title that will be displayed in the generated HTML
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Determines whether operation is in quiet mode (= not verbose).
     *
     * @param quiet true to be quiet, false to be verbose. default is verbose.
     */
    public final void setQuiet( final boolean quiet ) {
        this.quiet = quiet;
    } // setQuiet
    public final boolean isQuiet() {
        return quiet;
    } // isQuiet

    /**
     * Set the Java Source directories that will be converted into HTML.
     *
     * This has replaced setJavaSorce(String[])
     *
     * Overwrite any setting used by setJavaFileSource
     *
     * @param directories List of Java Source Directories
     */
    public void setJavaDirectorySource(List<String> directories) throws BadOptionException {

        // Validate that all sources are directories
        for (String directory : directories)  {
            File file = new File(directory);
            if (!file.isDirectory()) throw new BadOptionException(directory + " is not a directory");
        }

        // Convert directory to String[] of file names
        javaSourceFileList = new ArrayList<String>();
        for (String directory :directories) {
            getFileListFromDirectory(directory, javaSourceFileList);
        }


    }

    private static void getFileListFromDirectory(String directory, List<String> files) {

          File directoryFile = new File(directory);
          String[] list = directoryFile.list();
          if (list == null) return;

          for (String file : list) {

              String fileName = directory + File.separatorChar + file;

              if (new File(fileName).isFile()) {
                  if (fileName.endsWith(".java")) files.add(fileName);
              }
              else {
                  getFileListFromDirectory(fileName, files);
              }
          }
      }

    /**
     * Sets a list of java source files that will be converted into HTML.
     *
     * Use instead of setJavaDirectorySource() if a file lisy is availabe
     *
     *
     * @param files List of Java Files
     */
    public void setJavaFileSource(List<String> files) {
        javaSourceFileList = files;
    }

    /**
         * Set the Java Doc directories.
         *
         * @param javaDocLinks List of JavaDocOptions
         */
        public void setJavaDocLinks(List<Link> javaDocLinks) {
            javaDocOptionLinks = javaDocLinks;
        }

    /**
     * Sets the output directory that the generated HTML will be placed into.
     *
     * @param destination Directory where output will be directed to
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

//    private void loadParsers() {
//        List<ReferenceParser> referenceParsers = locateAllReferenceParsers();
//
//        for (ReferenceParser referenceParser : referenceParsers) {
//            if (referenceParser instanceof SourceParser){
//                sourceParsers.add((SourceParser)referenceParser);
//            }
//            else {
//                referenceParsers.add(referenceParser);
//            }
//        }
//    }
//
//    private List<ReferenceParser> locateAllReferenceParsers() {
//        // TODO needs to locate dynamiclly either from  classpath or something
//
//        List<ReferenceParser> referenceParsers = new ArrayList<ReferenceParser>();
//        referenceParsers.add(new JavaSource());
//        referenceParsers.add(new JavaDocManager());
//        return referenceParsers;
//
//    }

}
