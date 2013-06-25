/*
 * Copyright (c) 1999-2013, Dasuni Limited, All Rights Reserved.
 *
 *
 * Ant Task
 */

package com.java2html.ant;

import com.java2html.Java2HTML;
import com.java2html.internal.Link;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Java2HTMLTask extends Task {

    private Java2HTML java2HTML = new Java2HTML();

    private List<String> javaSourceFileList = new ArrayList<String>();
    private List<Link> javaDocList = new ArrayList<Link>();

    private boolean failOnError = false;

    public void execute() throws BuildException {
        try {
            java2HTML.setJavaFileSource(javaSourceFileList);
            java2HTML.setJavaDocLinks(javaDocList);
            if (!java2HTML.buildJava2HTML() && failOnError) throw new BuildException("Some Java files failed to convert to HTML");
        }
        catch (Exception e) {
            throw new BuildException("Java2HTML Build Problem:" + e.getMessage(), e);
        }
    }


      /**
     * Set the margin size that should be generated when buildJava2HTML() is called.
     *
     * @param showLineNumbers Show Line Numbers
     */
    public void setShowLineNumbers(boolean showLineNumbers) {
        java2HTML.setShowLineNumbers(showLineNumbers);
    }

    /**
     * Set the number of spaces that tabs will be converted
     * to when buildJava2HTML() is called.
     *
     * @param tabSize Number of spaces tabs should be converted to.
     */
    public void setTabSize(int tabSize) {
        java2HTML.setTabSize(tabSize);
    }

    /**
     * Determines if a header should be generated when buildJava2HTML() is called.
     *
     * @param header set to true if a header is required
     */
    public void setHeader(boolean header) {
        java2HTML.setHeader(header);
    }
    /**
     * Determines if only the java source and stylesheet files will be output
     *
     * @param simple set to true for simple output
     */
    public void setSimple(boolean simple) {
        java2HTML.setSimple(simple);
    }


    /**
     * Determines if a footer should be generated when buildJava2HTML() is called.
     *
     * @param footer set to true if a header is required
     */
    public void setFooter(boolean footer) {
        java2HTML.setFooter(footer);
    }


    /**
     * Set the title that will be used when buildJava2HTML() is called.
     *
     * @param title The title that will be displayed in the generated HTML
     */
    public void setTitle(String title) {
        java2HTML.setTitle(title);
    }

    /**
     * Determines whether operation is in quiet mode (= not verbose).
     *
     * @param quiet true to be quiet, false to be verbose. default is verbose.
     */
    public final void setQuiet( final boolean quiet ) {
      java2HTML.setQuiet( quiet );
    } // setQuiet

    /**
     * Set the Java Source directories that will be converted into HTML.
     *
     * @param fileSet List of Java Source Directories
     */

    public void addConfiguredFileSet(FileSet fileSet) {

        DirectoryScanner dScan = fileSet.getDirectoryScanner(getProject());
        dScan.scan();
        String baseDir = dScan.getBasedir().getAbsolutePath() + File.separator;
        String[] files = dScan.getIncludedFiles();

        // append base sedir
        for (String file : files) {
            javaSourceFileList.add(baseDir + file);
        }
    }

    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    public Link createLink() {
        Link link = new Link();
        javaDocList.add(link);
        return link;
    }



//    public void setJavaSource(String[] directories) throws BadOptionException {
//
//        javaSourceList = directories;
//
//        // Validate that all sources are directories
//        for (int i = 0; i < directories.length; i++)  {
//            File file = new File(javaSourceList[i]);
//            if (!file.isDirectory()) throw new BadOptionException(javaSourceList[i] + " is not a directory");
//        }
//    }

    /**
     * Set the Java Doc directories.
     *
     * @param javaDocOption List of JavaDocOptions
     */
//    public void setJavaDoc(JavaDocOption[] javaDocOptions) {
//        javaDocOptionList = javaDocOptions;
//    }

    /**
     * Sets the output directory that the generated HTML will be placed into.
     *
     * @param destination Directory where output will be directed to
     */
    public void setDestination(File destination) {
        if ( !java2HTML.isQuiet() ) System.out.println(destination.getAbsolutePath());
        java2HTML.setDestinationDir(destination.getAbsolutePath());
    }



}