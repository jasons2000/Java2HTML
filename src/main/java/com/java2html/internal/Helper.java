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

import org.apache.commons.lang.text.StrSubstitutor;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Helper {

    static String lineSep = System.getProperty("line.separator");

    static final String webSep = "/";

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

        File f =  new File(destination + File.separator + fileName);

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


    public static String getStyleOption() {
        String text = "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"stylesheet.css\" TITLE=\"Style\">";
        return text.toString();

    }

    public static String getPreText(String styleSheetRef, String className) {
        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">" + lineSep +
                "<HTML>" + lineSep +
                "<HEAD>" + lineSep +
                "<LINK REL=STYLESHEET TYPE=\"text/css\" HREF=\"" + styleSheetRef +
                "\" TITLE=\"Style\">" + lineSep +
                "<META NAME=\"GENERATOR\" CONTENT=\"" + version + "\">" + lineSep +
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

    /**
     * replaces "." with web delimeter c
     */
    static String convertDots(String webRef, char c) {
        return webRef.replace('.', c);
    }

    /**
     * replaces "\" with web delimeter
     */
    static String convert(String webRef) {
        return webRef.replace('\\', '/').replaceFirst(":", "|");
    }


    public static void getFileListFromDirectory(String directory, List<String> files) {

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
}
