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

package com.java2html.internal;

import com.java2html.*;
import com.java2html.internal.*;
import com.java2html.parser.*;

import java.io.*;
import java.util.*;

public class JavaSource {

    private boolean quiet = false;
    private final PackageLocator mLocator = new PackageLocator();

    private JavaParser parser = new JavaParser(System.in); // Todo: System.in not required

    private JavaDocManager javaDoc;

    private static class Pair {

        Pair(String text, String ref) {
            this.text = text;
            this.ref = ref;
        }

        String text;
        String ref;
    }

    private class Package {

        Package(String packageLevel, String className) {
            this.packageLevel = packageLevel;
            this.className = className;
        }

        String packageLevel;
        String className;
    }

    private Hashtable classList = new Hashtable();
    public  Hashtable packageList = new Hashtable(); // TODO Make Private
    private Hashtable directoryToPackage = new Hashtable();

    //private static Vector fileList = new Vector(); // Stored list of Files

    public Hashtable getFileList() {
        return directoryToPackage;
    }

    public final boolean isQuiet() {
      return quiet;
    } // isQuiet
    public final void setQuiet( final boolean quiet ) {
      this.quiet = quiet;
    } // setQuiet

    // top left
    final static String getPreIndex(String title) {
        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">" +
            Helper.lineSep +
            "<HTML>" + Helper.lineSep +
            "<HEAD>" + Helper.lineSep +
            "<META NAME=\"GENERATOR\" CONTENT=\"" + Helper.version + "\">" +
            Helper.lineSep +
            "<TITLE>" + title + " (Java2HTML)" + Helper.lineSep +
            "</TITLE>" + Helper.lineSep +
            "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"stylesheet.css\" TITLE=\"Style\">" +
            Helper.lineSep +
            "</HEAD>" + Helper.lineSep +
            "<BODY>" + Helper.lineSep +
            "<FONT size=\"+1\" CLASS=\"FrameHeadingFont\"><A HREF=\"front.html\" TARGET=\"SourceFrame\">" +
            title + "</A></FONT>" + Helper.lineSep +
            "<BR> <FONT CLASS=\"FrameItemFont\"><A HREF=\"AllClasses.html\" TARGET=\"packageFrame\">All Classes</A></FONT>" +
            Helper.lineSep +
            "<BR> <FONT size=\"+1\" CLASS=\"FrameHeadingFont\">Packages</FONT>" +
            Helper.lineSep;
    }

    // Bottom Left (Package Index)
    final static String getClassesFrame(String packageName) {
        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">" +
            Helper.lineSep +
            "<HTML>" + Helper.lineSep +
            "<HEAD>" + Helper.lineSep +
            "<META NAME=\"GENERATOR\" CONTENT=\"" + Helper.version + "\">" +
            Helper.lineSep +
            "<TITLE>" + packageName + " (Java2HTML)</TITLE>" + Helper.lineSep +
            "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"stylesheet.css\" TITLE=\"Style\">" +
            Helper.lineSep +
            "</HEAD>" + Helper.lineSep +
            "<BODY>" + Helper.lineSep +
            "<FONT size=\"+1\" CLASS=\"FrameHeadingFont\">" + packageName +
            "</FONT>" + Helper.lineSep;
    }

    private static final String postIndex = "</BODY>" + Helper.lineSep +
        "</HTML>" + Helper.lineSep;

    private String destination;
    private int marginSize;
    private int tabSize;
    private boolean header;
    private boolean footer;

    public JavaSource(String[] javaSourceFileList,
                      String destination,
                      int marginSize,
                      int tabSize,
                      boolean header,
                      boolean footer,
                      JavaDocManager javaDoc) {

        for (int i = 0 ; i < javaSourceFileList.length; i++) {
            processFile(javaSourceFileList[i]);
        }

        this.destination = destination;
        this.marginSize = marginSize;
        this.tabSize = tabSize;
        this.header = header;
        this.footer = footer;
        this.javaDoc = javaDoc;
    }

    public String getClassHRef(String text) {
        //System.out.println("Text="+text);
        int x = text.lastIndexOf(".");
        String packageName = text.substring(0, x);
        String className = text.substring(x + 1, text.length());
        //System.out.println("****Cn="+className+", packagName="+packageName);
        Hashtable ht = (Hashtable) classList.get(packageName);
        if (ht == null) {
            return null;
        }
        //System.out.println("Match ClassName="+className+", packagName="+packageName);
        return (String) ht.get(className);
    }

    public static void main(String[] args) throws IOException {

//        JavaSource js = new JavaSource(new String[] {args[1]}
//                                       , ".", 4, 4, false, false);
//        System.out.println("ClassList:-");
//        js.print();
//        createPackageIndex(args[1], js, "Title Goes Here");
    }

    public void createPackageIndex(String dest, String title) throws
        IOException {

        FileWriter file = new FileWriter(dest + File.separator +
                                         "packages.html");
        StringBuffer index = new StringBuffer(getPreIndex(title));

        Enumeration e = classList.keys();
        Vector sortedVector = new Vector();
        String text = null;

        createAllClassIndex(dest);

        while (e.hasMoreElements()) {

            text = (String) e.nextElement();
            int c = sortedVector.size();

            while (c > 0) {
                if (text.compareTo( (String) sortedVector.elementAt(c - 1)) > 0) {
                    break;
                }
                c--;
            }
            sortedVector.insertElementAt(text, c);
        }

        e = sortedVector.elements();
        text = null;
        String href = null;

        while (e.hasMoreElements()) {
            text = (String) e.nextElement();
            href = createClassIndex(dest, text);

            //System.out.println("text="+text+" href=" + href);
            packageList.put(text, href);
            if (text.equals("")) {
                text = "[DEFAULT]";
            }
            index.append("<BR>" + Helper.lineSep +
                         "<FONT CLASS=\"FrameItemFont\"><A HREF=\"" + href +
                         "\" TARGET=\"packageFrame\">" + text + "</A></FONT>");
        }

        index.append(postIndex);
        file.write(index.toString());
        file.close();
    }

    void createAllClassIndex(String dest) throws IOException {

        FileWriter file = new FileWriter(dest + File.separator +
                                         "AllClasses.html");
        file.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">"
                   + Helper.lineSep + "<HTML>"
                   + Helper.lineSep + "<HEAD>"
                   + Helper.lineSep + "<META NAME=\"GENERATOR\" CONTENT=\"" +
                   Helper.version + "\">" + Helper.lineSep +
                   "<TITLE>All Classes (Java2HTML)</TITLE>" + Helper.lineSep +
                   "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"stylesheet.css\" TITLE=\"Style\">" +
                   Helper.lineSep +
                   "</HEAD>" + Helper.lineSep +
                   "<BODY BGCOLOR=\"white\">" + Helper.lineSep +
                   "<FONT CLASS=\"FrameHeadingFont\" size=\"+1\">" +
                   Helper.lineSep +
                   //"<FONT size=+1>"+
                   "All Classes</FONT>" + Helper.lineSep);

        Vector sortedVector = new Vector();
        Enumeration e = classList.elements();
        Enumeration ef = null;
        String text = null;
        Pair p = null;

        while (e.hasMoreElements()) {

            Hashtable ht = (Hashtable) e.nextElement();
            ef = ht.keys();

            while (ef.hasMoreElements()) {

                text = (String) ef.nextElement();
                int c = sortedVector.size();

                while (c > 0) {

                    String str = ( (Pair) sortedVector.elementAt(c - 1)).text;
                    if (text.compareTo(str) > 0) {
                        break;
                    }
                    c--;
                }
                p = new Pair(text, (String) ht.get(text));
                sortedVector.insertElementAt(p, c);
            }
        }

        e = sortedVector.elements();

        while (e.hasMoreElements()) {

            p = (Pair) e.nextElement();
            file.write("<BR>" + Helper.lineSep +
                       "<FONT CLASS=\"FrameItemFont\"><A HREF=\"" + p.ref +
                       "\" TARGET=\"SourceFrame\">" + p.text + "</A></FONT>"); // TAken out CR
        }
        file.write(postIndex);
        file.close();

    }

    String createClassIndex(String dest, String packageString) throws
        IOException {

        String href2 = (packageString.equals("") ? "default" : packageString) +
            ".index.html";
        FileWriter file = new FileWriter(dest + File.separatorChar + href2);
        if (packageString.equals("")) {
            file.write(getClassesFrame("Package Default"));
        }
        else {
            file.write(getClassesFrame("Package " + packageString));
        }

        Hashtable ht = (Hashtable) classList.get(packageString);
        Enumeration e = ht.keys();

        Vector sortedVector = new Vector();
        String text = null;

        while (e.hasMoreElements()) {

            text = (String) e.nextElement();
            int c = sortedVector.size();

            while (c > 0) {
                if (text.compareTo( (String) sortedVector.elementAt(c - 1)) > 0) {
                    break;
                }
                c--;
            }
            sortedVector.insertElementAt(text, c);
        }

        text = null;
        String href = null;

        e = sortedVector.elements();
        while (e.hasMoreElements()) {

            text = (String) e.nextElement();
            href = (String) ht.get(text);

            file.write("<BR>" + Helper.lineSep +
                       "<FONT CLASS=\"FrameItemFont\"><A HREF=\"" + href +
                       "\" TARGET=\"SourceFrame\">" + text + "</A></FONT>");
        }
        file.write(postIndex);
        file.close();
        return href2;
    }

    public void print() {
        String pl, className, href = null;
        Enumeration en = classList.keys();
        while (en.hasMoreElements()) {
            pl = (String) en.nextElement();
            Hashtable ht = (Hashtable) classList.get(pl);
            Enumeration en1 = ht.keys();
            while (en1.hasMoreElements()) {
                className = (String) en1.nextElement();
                href = (String) ht.get(className);
                //System.out.println("{"+pl+"."+className+"},"+href);
            }
        }
    }

    private void processFile(String fullPathfileName) {

        int i = fullPathfileName.lastIndexOf(File.separator);
        String fileName = fullPathfileName.substring(i+1, fullPathfileName.length());
        //SCANS Java files, will need to add other types here perhaps if we want referenceing for thos other types

        // TODO: lep: this approach has problems with nested/inner/non-public classes
        // -> these have a class name possibly (most probably) different from the base file name.
        // -> won't be cross referenced afterwards in second parse run
        String classString = fileName.substring(0,  fileName.lastIndexOf('.'));
        //int idx = base.length()+1;
        String href;
        try {
            String packageLevel = mLocator.determinePackageName( fullPathfileName );
            // System.err.println( fullPathfileName + ": " + packageLevel );
            if (packageLevel == null || packageLevel.length() == 0) {
                packageLevel = ""; //default package =""
                href = fileName + ".html";
            }
            else {
                href = Helper.convertDots(packageLevel, '/') +
                    Helper.webSep + fileName + ".html";
            }
            //System.out.println("Package 1st Parsed="+packageLevel);

            // put the packagename into hashtable to cross reference filename with packageName on second parse

            directoryToPackage.put(fullPathfileName,
                                   new Package(packageLevel,
                                               classString));

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
        }
        catch (IOException e) {
            System.err.println("IO error for file [" +
                               fullPathfileName + "] " +
                               e.getMessage());
        }
        catch (Error e) {
            System.err.println("Problem encountered with file [" +
                               fullPathfileName+ "] " +
                               e.getMessage());
        }
    }




    /**
     * Add the command Line Directory, as directores passed as -js
     */
    public void generateJava2HTML() throws Exception {

        Hashtable filelist = getFileList();
        Enumeration keys = filelist.keys();

        String fileName = null;
        Package aPackage = null;

        while (keys.hasMoreElements()) {

            fileName = (String) keys.nextElement();
            aPackage = (Package) filelist.get(fileName);

            if ( "package-info.java".equalsIgnoreCase( new File( fileName ).getName() ) ) {
              // skip the replacement (as of JDK 1.5) for package.html
              continue;
            }

            // Create directories
            File temp = new File(destination); // this code deals with the c: or c:\ problem
            String s = temp.getAbsolutePath();
            if (!s.endsWith(File.separator)) {
                s += File.separator; // if not ending with \ add a \
            }
            String destFileName = null;
            if (aPackage.packageLevel.equals("")) {
                destFileName = s + aPackage.className + ".java.html";
            }
            else {
                destFileName = s +
                    Helper.convertDots(aPackage.packageLevel,
                                       File.separatorChar) + File.separatorChar +
                    aPackage.className + ".java.html";
            }

            // Make diirecroty (seperate from file portion)
            File dir = new File(s +
                                Helper.convertDots(aPackage.packageLevel, '/'));
            dir.mkdirs();
            //File f = new File(destFileName);
            //System.out.println("temp"+temp);

            HTMLFileWriter dest = new HTMLFileWriter(destFileName, marginSize,
                tabSize);
            FileReader source = new FileReader(fileName);
            dest.setHTMLMode(false);

            String dot = ".";
            if (aPackage.packageLevel.length() == 0) {
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
                parser.parse(source, dest, preDir, this, javaDoc);
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
                    dest.close();
                }
                catch (IOException e2) {}

                try {
                    source.close();
                }
                catch (IOException e2) {}
            }
            if (error == false) {
                if ( !quiet ) System.out.println("Created: " + destFileName);
            }
        } // while end
    }

    private String getDotDotRootPathFromPackage(String aPackage) {

        int length = aPackage.length();
        if (length == 0) {
            return "";
        }
        StringBuffer s = new StringBuffer("../");

        int prev = 0, index = 0;

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
}
