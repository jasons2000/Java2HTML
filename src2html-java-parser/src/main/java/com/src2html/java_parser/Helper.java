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

package com.src2html.java_parser;

import com.src2html.references.SourceParser;
import com.src2html.references.Symbol;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.io.*;
import java.util.*;

public class Helper {

    public static String lineSep = System.getProperty("line.separator");

    public static final char webSep = '/';

    private final StrSubstitutor substitutor;

    public static final String version = "Java2HTML Version ${project.version}";

    private final String destination;
    private final boolean quiet;

    public Helper(String destination, Map subs, boolean quiet) {
        this.destination = destination;
        this.substitutor = new StrSubstitutor(subs);
        this.quiet = quiet;
        // Create Output destination directory
        (new File(destination)).mkdirs();
    }

    public void createPage(String fileName) throws IOException {

        LineNumberReader reader = new LineNumberReader(new InputStreamReader(getClass().getResource("/templates/" + fileName).openStream()));

        File f = new File(destination + File.separator + fileName);

        PrintWriter printWriter = new PrintWriter(f);

        String line = reader.readLine();
        while (line != null) {
            String subLine = substitutor.replace(line);
            printWriter.println(subLine);
            line = reader.readLine();
        }
        printWriter.close();
        reader.close();

        if (!quiet) System.out.println("Created: " + f.getAbsolutePath());

    }


    private static String getStyleOption(String preDir, String languageSpecificStyleSheet) {
        return "<LINK REL=STYLESHEET TYPE=\"text/css\" HREF=\"" + preDir + "stylesheet.css" + "\" TITLE=\"Style\">" + lineSep +
                "<LINK REL=STYLESHEET TYPE=\"text/css\" HREF=\"" +  preDir + languageSpecificStyleSheet + "\" TITLE=\"Style\">";
    }

    public static String getPreText(String preDir, String styleSheetRef, String className) {
        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">" + lineSep +
                "<HTML>" + lineSep +
                "<HEAD>" + lineSep +
                getStyleOption(preDir, styleSheetRef) + lineSep +
                "<META NAME=\"GENERATOR\" CONTENT=\"" + version + "\">" + lineSep +
                "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\"/>" + lineSep + /*required for umlats etc*/
                "<TITLE>" + className + " (Java2HTML)</TITLE>" + lineSep +
                "</HEAD>" + lineSep +
                "<BODY>";
    }

    public static String getPostText() {
        return "</BODY>" + lineSep + "</HTML>";
    }

    private static String getMainText(String name, String date) {

        return "<TABLE CLASS=\"Header\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                lineSep +
                "<tr>" + lineSep +
                "<td colspan=\"2\" width=\"33%\">&nbsp;</td>" + lineSep +
                "<td align=\"center\" colspan=\"2\" width=\"33%\">" + lineSep +
                "<font size=\"4\">" + name + ".java</font>" + lineSep +
                "</td>" + lineSep +
                "<td align=\"right\" colspan=\"2\" width=\"33%\">&nbsp;</td>" + lineSep +
                "</tr>" + lineSep +
                "</TABLE>" + lineSep;
    }

    public static String getHeader(String name, String date, boolean header) {
        if (header) {
            return getMainText(name, date) + "<pre CLASS=\"Classes\">" + lineSep;
        }
        else {
            return "<pre CLASS=\"Classes\">" + lineSep;
        }
    }

    public static String getFooter(String name, String date, boolean footer) {
        if (footer) {
            return "</pre>" + getMainText(name, date) + "" + lineSep;
        }
        else {
            return "</pre>" + lineSep;
        }
    }

    // top left
    public final static String getPreIndex(String title) {
        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">" +
                lineSep +
                "<HTML>" + lineSep +
                "<HEAD>" + lineSep +
                "<META NAME=\"GENERATOR\" CONTENT=\"" + Helper.version + "\">" + lineSep +
                "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\"/>" + lineSep +
                "<TITLE>" + title + " (Java2HTML)" + lineSep +
                "</TITLE>" + lineSep +
                "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"stylesheet.css\" TITLE=\"Style\">" +
                lineSep +
                "</HEAD>" + lineSep +
                "<BODY>" + lineSep +
                "<FONT size=\"+1\" CLASS=\"FrameHeadingFont\"><A HREF=\"front.html\" TARGET=\"SourceFrame\">" +
                title + "</A></FONT>" + lineSep +
                "<BR> <FONT CLASS=\"FrameItemFont\"><A HREF=\"AllClasses.html\" TARGET=\"packageFrame\">All Classes</A></FONT>" +
                lineSep +
                "<BR> <FONT size=\"+1\" CLASS=\"FrameHeadingFont\">Packages</FONT>" +
                lineSep;
    }

    // Bottom Left (Package Index)
    public final static String getClassesFrame(String packageName) {
        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">" +
                lineSep +
                "<HTML>" + lineSep +
                "<HEAD>" + lineSep +
                "<META NAME=\"GENERATOR\" CONTENT=\"" + Helper.version + "\">" +
                lineSep +
                "<TITLE>" + packageName + " (Java2HTML)</TITLE>" + lineSep +
                "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"stylesheet.css\" TITLE=\"Style\">" +
                lineSep +
                "</HEAD>" + lineSep +
                "<BODY>" + lineSep +
                "<FONT size=\"+1\" CLASS=\"FrameHeadingFont\">" + packageName +
                "</FONT>" + lineSep;
    }


    public static final String postIndex = "</BODY>" + Helper.lineSep +
            "</HTML>" + Helper.lineSep;


    /**
     * replaces "." with web delimeter c
     */
    public static String convertDots(String webRef, char c) {
        return webRef.replace('.', c);
    }

    /**
     * replaces "\" with web delimeter
     */
    public static String convert(String webRef) {
        return webRef.replace('\\', '/').replaceFirst(":", "|");
    }

    // a list of packages
    public static void createPackageIndex(String dest, String title, SourceParser<? extends Symbol> symbolTableMap) throws
            IOException {

        FileWriter file = new FileWriter(dest + File.separator +
                "packages.html");
        StringBuffer index = new StringBuffer(Helper.getPreIndex(title));

        List<Symbol> sortedDirs = new ArrayList<Symbol>();
        sortedDirs.addAll(symbolTableMap.getAllDirSymbols());
        Collections.sort(sortedDirs, new Comparator<Symbol>() {
                    @Override
                    public int compare(Symbol o1, Symbol o2) {
                        return ObjectUtils.compare(o1.getFullId(), o2.getFullId());

                    }
                });

        for (Symbol dir : sortedDirs) {
            Collection<? extends Symbol> children = symbolTableMap.getFileSymbolsInDir(dir.getFullId());
            createClassIndex(dest, dir, children);

            index.append("<BR>" + Helper.lineSep +
                    "<FONT CLASS=\"FrameItemFont\"><A HREF=\"" + dir.getHRef("") +
                    "\" TARGET=\"packageFrame\">" + dir.getDescriptiveName() + "</A></FONT>");
        }

        index.append(Helper.postIndex);
        file.write(index.toString());
        file.close();
    }

    public static String getHrefForClassIndexForPackage(String packageString) {
        return  (packageString.equals("") ? "default" : packageString) +
                ".index.html";

    }

    // create a list of classes for a particular package
    static void createClassIndex(String dest, Symbol parent, Collection<? extends Symbol> fileListInDir) throws
            IOException {

        FileWriter file = new FileWriter(dest + File.separatorChar + parent.getHRef(""));
        if (parent.getFullParentId() == null) {
            file.write(Helper.getClassesFrame("Package Default"));
        }
        else {
            file.write(Helper.getClassesFrame("Package " + parent.getFullId()));
        }

        List<Symbol> sortedClasses = new ArrayList<Symbol>();
        sortedClasses.addAll(fileListInDir);
        Collections.sort(sortedClasses, new Comparator<Symbol>() {
            @Override
            public int compare(Symbol o1, Symbol o2) {
                return ObjectUtils.compare(o1.getId(), o2.getId());

            }
        });

        for (Symbol symbol : sortedClasses) {

            file.write("<BR>" + Helper.lineSep +
                    "<FONT CLASS=\"FrameItemFont\"><A HREF=\"" + symbol.getHRef("") +
                    "\" TARGET=\"SourceFrame\">" + symbol.getDescriptiveName() + "</A></FONT>");
        }
        file.write(Helper.postIndex);
        file.close();

    }


    public static void createAllClassIndex(String dest,  SourceParser<? extends Symbol> symbolTable) throws IOException {

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

        ArrayList<? extends Symbol> allFileSymbols = new ArrayList<Symbol>(symbolTable.getAllFileSymbols());

        Collections.sort(allFileSymbols, new Comparator<Symbol>() {
                    @Override
                    public int compare(Symbol o1, Symbol o2) {
                        return ObjectUtils.compare(o1.getId(), o2.getId());

                    }
                });


        for (Symbol s : allFileSymbols) {

            file.write("<BR>" + Helper.lineSep +
                    "<FONT CLASS=\"FrameItemFont\"><A HREF=\"" + s.getHRef("") +
                    "\" TARGET=\"SourceFrame\">" + s.getId()+ "</A></FONT>"); // Taken out CR
        }
        file.write(Helper.postIndex);
        file.close();

    }


}
