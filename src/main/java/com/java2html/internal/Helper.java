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

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;

public class Helper {

    static String lineSep = System.getProperty("line.separator");

    static final String webSep = "/";

    public static final String version = "Java2HTML Version ${project.version}";

    public static String getFront() {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
            DateFormat.SHORT);

        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\"" + lineSep +
            "<HTML>" + lineSep +
            "<HEAD>" + lineSep +
            "<META NAME=\"GENERATOR\" CONTENT=\"" + version + "\">" + lineSep +
            "<TITLE>Java2HTML</TITLE>" + lineSep +
            "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"stylesheet.css\" TITLE=\"Style\">" +
            lineSep +
            "</HEAD>" + lineSep +
            "<BODY>" + lineSep +
            "<p><strong>Instructions:-</strong></p>" + lineSep +
            "<ul>" + lineSep +
            "<li>Top-Left Panel Selects a Package</li>" + lineSep +
            "<li>Bottom-Left Panel Selects a Java File</li>" + lineSep +
            "<li>Text displayed like <FONT CLASS=\"Classes\"><A href=\"#\">this</A></FONT> represents a link to another Java source, clicking on it selects it in this panel.</li>" +
            lineSep +
            "</ul>" + lineSep +
            "<p><strong>Credits:-</strong></p>" + lineSep +
            "<ul><li>Produced by " +
            "<a href=\"http://www.java2html.com\" TARGET=\"_top\"><em>" +
            version +
            "</em></a> on the " + df.format(new Date()) +
            "</li></ul>" + lineSep +
            "<em>(If you like this tool, please <a href=\"mailto:?subject=http://www.java2html.com\">email</a> the reference <A href=\"http://www.java2html.com\" TARGET=\"_top\">http://www.java2html.com</A> to a colleague)</em>" +
            lineSep +
            "</BODY>" + lineSep +
            "</HTML>" + lineSep;

    }

    public static String getFrame(String title) {
        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">" + lineSep +
            "<HTML>" + lineSep +
            "<HEAD>" + lineSep +
            "<META NAME=\"GENERATOR\" CONTENT=\"" + version + "\">" + lineSep +
            "<TITLE>" + title + " (Java2HTML)</TITLE>" + lineSep +
            lineSep +
            "</HEAD>" + lineSep +
            "<FRAMESET cols=\"30%, 70%\">" + lineSep +
            "<FRAMESET rows=\"30%, 70%\">" + lineSep +
            "<FRAME src=\"packages.html\" name=\"packageListFrame\">" + lineSep +
            "<FRAME src=\"AllClasses.html\" name=\"packageFrame\">" + lineSep +
            "</FRAMESET>" + lineSep +
            "<FRAME src=\"front.html\" name=\"SourceFrame\">" + lineSep +
            "</FRAMESET>" + lineSep +
            "<NOFRAMES>" + lineSep +
            "<H2>Frame Alert</H2>" + lineSep +
            "<P>" + lineSep +
            "This document is designed to be viewed using the frames feature. If you see this message, you are using a non-frame-capable web client." +
            lineSep +
            "</NOFRAMES>" + lineSep +
            "</HTML>";
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
//        StringBuffer buf = new StringBuffer(webRef);
//        int x = 0;
//        while (true) {
//
//            x = webRef.indexOf('.', x);
//            if (x == -1) {
//                break;
//            }
//            buf.setCharAt(x, c);
//            x++;
//        }
//        return buf.toString();
    }

    /**
     * replaces "\" with web delimeter
     */
    static String convert(String webRef) {
        return webRef.replace('\\', '/').replaceFirst(":", "|");

//        StringBuffer buf = new StringBuffer(webRef);
//        int x = 0;
//        while (true) {
//
//            x = webRef.indexOf(File.separatorChar, x);
//            if (x == -1) {
//                break;
//            }
//            buf.setCharAt(x, '/');
//            x++;
//        }
//        x = webRef.indexOf(':', 0);
//        if (x != -1) {
//            buf.setCharAt(x, '|');
//        }
//        return buf.toString();
    }



    public static Vector getFileListFromDirectory(String directory, Vector vector){

        File directoryFile = new File(directory);
        String[] list = directoryFile.list();
        if (list == null) return vector;
        int cnt = 0;

        while (cnt < list.length) {

            String fileName = directory + File.separatorChar + list[cnt];

            if (new File(fileName).isFile()) {
                if (fileName.endsWith(".java")) vector.addElement(fileName);
            }
            else {
                vector = getFileListFromDirectory(fileName, vector);
            }
            cnt++;
        }
        return vector;
    }
}
