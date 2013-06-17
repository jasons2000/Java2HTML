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

import com.java2html.internal.CommandLineOptions;
import com.java2html.internal.HTMLFileWriter;
import com.java2html.internal.Helper;
import com.java2html.internal.Link;
import com.java2html.java_parser.JavaDocManager;
import com.java2html.java_parser.JavaSource;
import com.java2html.java_parser.PackageH;
import com.java2html.java_parser.ParseException;
import org.apache.commons.io.FileUtils;

import java.io.*;
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

    public Map<String, String> packageList = new HashMap<String, String>();
    private List<String> javaSourceFileList = null;

    private List<Link> javaDocOptionLinks = Collections.emptyList();

    private String destination = "output";
    public static ResourceBundle bundle = ResourceBundle.getBundle("general_text");

    public Map<String, Map<String,String>> classList = new HashMap<String, Map<String,String>>();
    public Map<String, PackageH> directoryToPackage = new HashMap<String, PackageH>();


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

        JavaSource javaSource = new JavaSource();
        if (!simple) Helper.createPackageIndex(destination, title, classList, packageList);
        javaSource.setQuiet( quiet );

        for (String sourceDir : javaSourceFileList) {
             Reader reader = new BufferedReader( new FileReader(sourceDir));
            String packageLevel = javaSource.findReferences(reader);
            fn(sourceDir, packageLevel);
        }

        // Generate files - 2nd parse
        for (Map.Entry<String, PackageH> entry : directoryToPackage.entrySet()) {
            String fileName = entry.getKey();
            PackageH aPackage = entry.getValue();
            // skip the replacement (as of JDK 1.5) for package.html
            if (!"package-info.java".equalsIgnoreCase(new File(fileName).getName())) {

                fn2(javaSource, javaDoc, fileName, aPackage);
            }
        }

        return true;
    }

    private void fn2(JavaSource javaSource, JavaDocManager javaDoc, String fileName, PackageH aPackage) throws IOException {

        // Create directories
        File temp = new File(destination); // this code deals with the c: or c:\ problem
        String s = temp.getAbsolutePath();
        if (!s.endsWith(File.separator)) {
            s += File.separator; // if not ending with \ add a \
        }
        String destFileName;
        if (aPackage.packageLevel.isEmpty()) {
            destFileName = s + aPackage.className + ".java.html";
        }
        else {
            destFileName = s +
                    Helper.convertDots(aPackage.packageLevel,
                            File.separatorChar) + File.separatorChar +
                    aPackage.className + ".java.html";
        }

        // Make directory (seperate from file portion)
        File dir = new File(s +
                Helper.convertDots(aPackage.packageLevel, '/'));
        dir.mkdirs();
        //File f = new File(destFileName);
        //System.out.println("temp"+temp);

        HTMLFileWriter dest = new HTMLFileWriter(new BufferedWriter(new FileWriter(destFileName)), marginSize, tabSize);
        FileReader source = new FileReader(fileName);
        dest.setHTMLMode(false);

        String dot = ".";
        if (aPackage.packageLevel.isEmpty()) {
            dot = ""; // If no package then remove the dot

        }
        String packageLevel = Helper.convert(aPackage.packageLevel);
        String preDir = getDotDotRootPathFromPackage(packageLevel);
        dest.write(Helper.getPreText(preDir + "stylesheet.css",
                aPackage.packageLevel + dot +
                        aPackage.className)); // what is this doing ?
        dest.write(Helper.getHeader(aPackage.className, "", header)); //TODO: add date string
        dest.write(dest.getFirstLineNumber());
        dest.setHTMLMode(true);
        boolean error = false;
        //System.out.print("Reading: "+fileName); // TODO check

        try {
            javaSource.toHtml(source, dest, preDir, this, javaDoc);
            dest.setHTMLMode(false);
        }
        catch (ParseException e) {
            // This should never happen
            error = true;
            //System.out.println("Parse Error for file: "+file.getName()/*+", "+e.getMessage()*/);
            System.out.println(fileName + ": Parse Error, Non-Legal Java File: " + e.getMessage());
            // e.printStackTrace();
        }
        catch (IOException e) {
            error = true;
            System.out.println("IO Error. (2nd Parse)");
        }
        finally {
            // Clear up resources
            try {
                dest.write(Helper.getFooter(aPackage.className, "", footer)); //TODO: add date string
                dest.write(Helper.getPostText());
                dest.flush();
                dest.close();
            }
            catch (IOException e2) {
            }

            try {
                source.close();
            }
            catch (IOException e2) {
            }
        }
        if (error == false) {
            if (!quiet) System.out.println("Created: " + destFileName);
        }

    }

    private void fn(String fullPathfileName, String packageLevel) throws FileNotFoundException {


        int i = fullPathfileName.lastIndexOf(File.separator);
        String fileName = fullPathfileName.substring(i+1, fullPathfileName.length());
        //SCANS Java files, will need to add other types here perhaps if we want referenceing for thos other types

        // TODO: lep: this approach has problems with nested/inner/non-public classes
        // -> these have a class name possibly (most probably) different from the base file name.
        // -> won't be cross referenced afterwards in second parse run
        String classString = fileName.substring(0,  fileName.lastIndexOf('.'));
                //int idx = base.length()+1;
        String href;
                   // System.err.println( fullPathfileName + ": " + packageLevel );
                   if (packageLevel == null || packageLevel.length() == 0) {
                       packageLevel = ""; //default package =""
                       href = classString + ".html";
                   }
                   else {
                       href = Helper.convertDots(packageLevel, '/') +
                       Helper.webSep + classString + ".html";
                   }
                   //System.out.println("Package 1st Parsed="+packageLevel);

                   // put the packagename into hashtable to cross reference filename with packageName on second parse

                   directoryToPackage.put(fullPathfileName, new PackageH(packageLevel, classString));

                   // put the package + className into hashtable to determin class refercnes
                   Hashtable pl = (Hashtable) classList.get(packageLevel);
                   if (pl != null) {
                       pl.put(classString, href);
                   }
                   else {
                       Hashtable ht2 = new Hashtable();
                       ht2.put(classString, href);
                       classList.put(packageLevel, ht2);
                   }
//               }
//               catch (IOException e) {
//                   System.err.println("IO error for file [" +
//                                      fullPathfileName + "] " +
//                                      e.getMessage());
//               }
//               catch (Error e) {
//                   System.err.println("Problem encountered with file [" +
//                                      fullPathfileName+ "] " +
//                                      e.getMessage());
//               }
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



    private String getDotDotRootPathFromPackage(String aPackage) {

        int length = aPackage.length();
        if (length == 0) {
            return "";
        }
        StringBuffer s = new StringBuffer("../");

        int index = 0;

        while (true) {
            index = aPackage.indexOf('.', index);
            if (index == -1) {
                break;
            }
            s.append("../");
            index++;
        }
        return s.toString();
    }


    public String getClassHRef(String text) {
           //System.out.println("Text="+text);
           int x = text.lastIndexOf(".");
           String packageName = text.substring(0, x);
           String className = text.substring(x + 1, text.length());
           //System.out.println("****Cn="+className+", packagName="+packageName);
           Map<String,String> ht = classList.get(packageName);
           if (ht == null) {
               return null;
           }
           //System.out.println("Match ClassName="+className+", packagName="+packageName);
           return ht.get(className);
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
