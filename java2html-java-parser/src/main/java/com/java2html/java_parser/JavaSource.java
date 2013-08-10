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
import com.java2html.internal.Link;
import com.java2html.internal.ParsingException;
import com.java2html.references.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JavaSource implements SourceParser {

    public Map<String, Map<String, String>> allClassesHRefByPackage = new HashMap<String, Map<String, String>>();
    public Map<String, PackageH> directoryToPackage = new HashMap<String, PackageH>();

    private boolean quiet = false;

    private JavaParser parser = new JavaParser(System.in); // Todo: System.in not required

    public JavaSource() {

    }


    public final boolean isQuiet() {
      return quiet;
    } // isQuiet
    public final void setQuiet( final boolean quiet ) {
      this.quiet = quiet;
    } // setQuiet


    @Override
    public boolean isMatch(String fileName) {
        return fileName.endsWith(".java");
    }

    @Override
     public SymbolTableMutable<JavaSymbol> parseReferences(String fullPathFilename, Reader reader)  throws ParsingException  {

        SymbolTableMutable<JavaSymbol> table = new SymbolTableMutable<JavaSymbol>();

        try {
            String packageName = PackageLocator.scan(reader);
            fn(fullPathFilename, packageName);
        }
        catch (IOException e) {
            throw new ParsingException(e);
        }

        return table;
     }


    /**
         * Builds the Java2HTML
         * <p/>
         * returns false if any failures were detected
         */
        public boolean buildJava2HTML() throws IOException, BadOptionException {

            createSupportingFiles();

            // we load up the javaDoc with java doc references, these will be gverridden by javasrc references but that's a good thing
            SymbolTableMutable javaDocTable = new JavaDocManager(javaDocOptionLinks.toArray(new Link[0])).getReferenceMapJavaDoc();

            if (javaSourceFileNameList == null) {
                setJavaDirectorySource(Arrays.asList("."));
            }
            // Performs first parse


           // javaSource.setQuiet(quiet);
            int marginSize = 0;

            for (String fullPathFileName : javaSourceFileNameList) {
                Reader reader = new BufferedReader(new TFileReader(new TFile(fullPathFileName)));

                LineNumberReader lineNumberReader = new LineNumberReader(reader);
                String packageLevel = javaSourceParser.parseReferences(javaDocTable, fullPathFileName, lineNumberReader);
                // count lines
                marginSize = getMarginSize(lineNumberReader);


                reader.close();
                fn(fullPathFileName, packageLevel);
            }

            if (!simple) Helper.createPackageIndex(destinationDir, title, allClassesHRefByPackage, packageList);


            // Generate files - 2nd parse
            for (Map.Entry<String, PackageH> entry : directoryToPackage.entrySet()) {
                String fileName = entry.getKey();
                PackageH aPackage = entry.getValue();
                // skip the replacement (as of JDK 1.5) for package.html
                if (!"package-info.java".equalsIgnoreCase(new TFile(fileName).getName())) {

                    createTargetFile(javaSourceParser, javaDocTable, fileName, aPackage, marginSize);
                }
            }

            return true;
        }

    private void createSupportingFiles() throws IOException {

          DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                  DateFormat.SHORT);

          HashMap<String, String> subs = new HashMap<String, String>();
          subs.put("date", df.format(new Date()));
          subs.put("version", "1.0");
          subs.put("title", title);


          Helper helper = new Helper(destinationDir, subs, quiet);

          //Create StyleSheet
          File f = new File(destinationDir + "/stylesheet.css");
          FileUtils.copyURLToFile(getClass().getResource("/stylesheet.css"), f);

          //Create Java StyleSheet
          File javaCss = new File(destinationDir + "/java_stylesheet.css");
          FileUtils.copyURLToFile(getClass().getResource("/java_stylesheet.css"), javaCss);


          if (!quiet) System.out.println("Created: " + f.getAbsolutePath());

          // Check File.Separator

          if (!simple) {

              helper.createPage("front.html");
              // Create main Index.html
              helper.createPage("index.html");

          }
      }


    @Override
    public String toHtml(SymbolTableByLanguage referenceMap, Reader reader) throws ParsingException {
        try {
            StringWriter sw = new StringWriter();
            HTMLFileWriter dest = new HTMLFileWriter(sw, 4,4); // todo make this HTML only, reworkout dependency

            parser.parse(reader,dest,referenceMap, prePath);
            dest.flush();
            return sw.toString();
        }
        catch (IOException ex) {
            throw new ParsingException(ex);
        }

    }

    private void createTargetFile(SourceParser javaSource, JavaDocManager javaDoc, String fileName, PackageH aPackage, int marginSize) throws IOException {

           // Create directories
           File temp = new File(destinationDir); // this code deals with the c: or c:\ problem
           String s = temp.getAbsolutePath();
           if (!s.endsWith(File.separator)) {
               s += File.separator; // if not ending with \ add a \
           }
           String destFileName;
           if (aPackage.packageLevel.isEmpty()) {
               destFileName = s;
           }
           else {
               destFileName = s +
                   Helper.convertDots(aPackage.packageLevel, File.separatorChar) + File.separatorChar;
           }
           destFileName =  destFileName + aPackage.className + ".java.html";


           // Make directory (seperate from file portion)
           File dir = new File(s +
                   Helper.convertDots(aPackage.packageLevel, '/'));
           dir.mkdirs();
           //File f = new File(destFileName);
           //System.out.println("temp"+temp);

           BufferedWriter dest = new BufferedWriter(new FileWriter(destFileName));
           TFileReader source = new TFileReader(new TFile(fileName));

           String dot = ".";
           if (aPackage.packageLevel.isEmpty()) {
               dot = ""; // If no package then remove the dot

           }
           String packageLevel = Helper.convert(aPackage.packageLevel);
           String preDir = getDotDotRootPathFromPackage(packageLevel);
           String preText = Helper.getPreText(preDir + "java_stylesheet.css",
                   aPackage.packageLevel + dot +
                           aPackage.className); // what is this doing ?
                   dest.write(Helper.getHeader(aPackage.className, "", header));
           dest.write(preText); //TODO: add date string
   //        dest.write(dest.getFirstLineNumber()); // todo what the hell was this for???
           boolean error = false;
           //System.out.print("Reading: "+fileName); // TODO check

           try {
               String html = javaSource.toHtml(source, preDir, this, javaDoc);
               dest.write(html);
           }
           catch (ParsingException e) {
               dest.write("<BR><BR>");
               dest.write("<FONT CLASS=\"ParseError\">");
               final String msg = "Non Legal Java File: "+e.getMessage()+")";
               dest.write(msg);
               dest.write("</FONT>");

                   error = true;
               //System.out.println("Parse Error for file: "+file.getName()/*+", "+e.getMessage()*/);
               System.out.println(fileName + ": Parse Error, Non-Legal Java File: " + e.getMessage());
               // e.printStackTrace();
           }
           finally {
               // Clear up resources
   //            try {
                   dest.write(Helper.getFooter(aPackage.className, "", footer)); //TODO: add date string
                   dest.write(Helper.getPostText());
                   dest.flush();
                   dest.close();
   //            }
   //            catch (IOException e2) {
   //            }
               IOUtils.closeQuietly(source);

           }
           if (!error  && !quiet) {
               System.out.println("Created: " + destFileName);
           }

       }


    @Override
    public String getLanguageId() {
        return "JAVA";
    }

    private void fn(String fullPathFileName, String packageLevel) throws FileNotFoundException {


           int i = fullPathFileName.lastIndexOf(File.separator);
           String fileName = fullPathFileName.substring(i + 1, fullPathFileName.length());
           //SCANS Java files, will need to add other types here perhaps if we want referenceing for thos other types

           // TODO: lep: this approach has problems with nested/inner/non-public classes
           // -> these have a class name possibly (most probably) different from the base file name.
           // -> won't be cross referenced afterwards in second parse run
           String classString = fileName.substring(0, fileName.lastIndexOf('.'));
           //int idx = base.length()+1;
           String href;
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

           directoryToPackage.put(fullPathFileName, new PackageH(packageLevel, classString));

           // put the package + className into hashtable to determin class refercnes
           Map<String, String> hrefByClassName = allClassesHRefByPackage.get(packageLevel);
           if (hrefByClassName == null) {
               hrefByClassName = new HashMap<String, String>();
               allClassesHRefByPackage.put(packageLevel, hrefByClassName);
           }
           hrefByClassName.put(classString, href);

       }



}
