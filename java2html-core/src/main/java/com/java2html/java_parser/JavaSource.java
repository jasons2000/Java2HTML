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

package com.java2html.java_parser;

import com.java2html.internal.HTMLFileWriter;
import com.java2html.internal.Helper;

import java.io.*;
import java.util.*;

public class JavaSource {


    public Map<String, Map<String,String>> classList = new HashMap<String, Map<String,String>>();
    public Map<String, String> packageList = new HashMap<String, String>(); // TODO Make Private
    public Map<String, Package> directoryToPackage = new HashMap<String, Package>();

    private boolean quiet = false;
    private final PackageLocator mLocator = new PackageLocator();

    private JavaParser parser = new JavaParser(System.in); // Todo: System.in not required

    private JavaDocManager javaDoc;

    private class Package {

        Package(String packageLevel, String className) {
            this.packageLevel = packageLevel;
            this.className = className;
        }

        String packageLevel;
        String className;
    }


    public final boolean isQuiet() {
      return quiet;
    } // isQuiet
    public final void setQuiet( final boolean quiet ) {
      this.quiet = quiet;
    } // setQuiet


    private String destination;
    private int marginSize;
    private int tabSize;
    private boolean header;
    private boolean footer;

    public JavaSource(List<String> javaSourceFileList,
                      String destination,
                      int marginSize,
                      int tabSize,
                      boolean header,
                      boolean footer,
                      JavaDocManager javaDoc) {

        for (String sourceDir : javaSourceFileList) {
            processFile(sourceDir);
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
        Map<String,String> ht = classList.get(packageName);
        if (ht == null) {
            return null;
        }
        //System.out.println("Match ClassName="+className+", packagName="+packageName);
        return ht.get(className);
    }

    public static void main(String[] args) throws IOException {

//        JavaSource js = new JavaSource(new String[] {args[1]}
//                                       , ".", 4, 4, false, false);
//        System.out.println("ClassList:-");
//        js.print();
//        createPackageIndex(args[1], js, "Title Goes Here");
    }




    public void print() {
        String pl, className, href = null;
        Iterator<String> en = classList.keySet().iterator();
        while (en.hasNext()) {
            pl = en.next();
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
            final Reader lJavaSourceReader = new BufferedReader( new FileReader(fullPathfileName));

            String packageLevel = PackageLocator.scan( lJavaSourceReader);
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
    public void generateJava2HTML() throws IOException {


        for (Map.Entry<String, Package> entry : directoryToPackage.entrySet() ) {

            String fileName = entry.getKey();
            Package aPackage =  entry.getValue();

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

            // Make diirecroty (seperate from file portion)
            File dir = new File(s +
                                Helper.convertDots(aPackage.packageLevel, '/'));
            dir.mkdirs();
            //File f = new File(destFileName);
            //System.out.println("temp"+temp);

            HTMLFileWriter dest = new HTMLFileWriter(destFileName, marginSize, tabSize);
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
}
