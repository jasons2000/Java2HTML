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

import java.io.*;
import java.text.*;
import java.util.*;

public class Helper {

    static String lineSep;

    static {
        lineSep = System.getProperty("line.separator");
        if (lineSep == null) {
            System.out.println(
                "Warning:System Property line.separator not defined\n");
            lineSep = "\r\n";
        }
    }

    public static final String version = "Java2HTML Version @VERSION@";

    public static final String copyRight =
        "Copyright (c) 1999-2007, Enterprise Solution Consultants Limited, All Rights Reserved.." +
        lineSep + "New Versions available from http://www.java2html.com" +
        lineSep + "(type j2h with no arguments to get help)";

    static final String usage = lineSep +
        "Usage: [j2h|java j2h|java -jar j2h.jar][options]" + lineSep +
        "-d <destination>    Destination output directory" + lineSep +
        "-js <java source>    Java Source directory" + lineSep +
        "-jd <javadoc> [web_reference]  JavaDoc Source directory" + lineSep +
        "-m <margin>      Margin Size (with Line Numbers)" + lineSep +
        "-t <tabsize>      Tab size" + lineSep +
        "-n <name>      Title Name" + lineSep +
        "-nh        Prevents the header from being displayed" + lineSep +
        "-nf        Prevents the footer from being displayed" + lineSep +
        "-s         Simple output, just include the Java source and stylesheet.css files" + lineSep +
        "-q|quiet   Prevents verbose output from being displayed" + lineSep +
        "-h (or no arguments)    This Help" + lineSep + lineSep +

        "Edit generated stylesheet.css file to change colours & styles" +
        lineSep +
        "View generated index.html in browser to see results after running" +
        lineSep;

    public static String getStyleSheet() {

        return "/* Java2HTML - Colour Definitions*/" + lineSep +
            lineSep +
            "/* Define colors, fonts and other style attributes here to override the defaults  */" +
            lineSep +
            lineSep +
            "/* Page background Colour */" + lineSep +
            "body { background-color: #FFFFFF; }" + lineSep +
            lineSep +
            "/* Default Text Colour */" + lineSep +
            "body {  color: #000000}" + lineSep +
            lineSep +
            "/* Header/Footer Colours */" + lineSep +
            ".Header  { font-family: Arial, Helvetica, sans-serif; color: #000000;  background-color:#EEEEFF }" +
            lineSep +
            lineSep +
            "/* Line Number */" + lineSep +
            ".LN     { color: #BBBBBB;  background-color:#FFFFFF }" + lineSep +
            lineSep +
            "/* Parse Error */" + lineSep +
            ".ParseError { color: #FF0000 ; font-weight: bold }" + lineSep +
            lineSep +
            "/* Link Colours */" + lineSep +
            ".Classes A:link		{ color: #000000; }" + lineSep +
            ".Classes A:visited	{ color: #000000; }" + lineSep +
            ".Classes PRE 		{ color: #000000; }" + lineSep +
            lineSep +
            "/* Token Colours */" + lineSep +
            ".CharacerLiteral	{ color: #FF00FF; }" + lineSep +
            ".StringLiteral		{ color: #FF00FF; }" + lineSep +
            ".SingleLineComment	{ color: #008000; }" + lineSep +
            ".FormalComment		{ color: #008000; }" + lineSep +
            ".MultiLineComment	{ color: #008000; }" + lineSep +
            ".Abstract		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Annotation	{ color: #66CCFF ; font-weight: bold }" + lineSep +
            ".Assert			{ color: #FF0000 ; font-weight: bold }" + lineSep +
            ".Boolean		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Break			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Byte			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Case			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Catch			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Char			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Class			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Const			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Continue		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Default		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Do			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Double		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Else			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Enum			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Extends		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".False			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Final			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Finally		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Float			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".For			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Goto			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".If			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Implements		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Import		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".InstanceOf		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Int			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Interface		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Long			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Native		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".New			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Package		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Private		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Protected		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Public		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Return		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Short			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Static		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Super			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Switch		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Synchronized		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".This			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Throw			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Throws		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Transient		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".True			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Try			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Void			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".Volatile		{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".While			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".StrictFP			{ color: #0000FF ; font-weight: bold }" + lineSep +
            ".IntegerLiteral	{ color: #000000 }" + lineSep +
            ".DecimalLiteral	{ color: #000000 }" + lineSep +
            ".HexLiteral		{ color: #000000 }" + lineSep +
            ".OctalLiteral		{ color: #000000 }" + lineSep +
            ".FloatPointLiteral	{ color: #000000 }";
    }

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
        StringBuffer buf = new StringBuffer(webRef);
        int x = 0;
        while (true) {

            x = webRef.indexOf('.', x);
            if (x == -1) {
                break;
            }
            buf.setCharAt(x, c);
            x++;
        }
        return buf.toString();
    }

    /**
     * replaces "\" with web delimeter
     */
    static String convert(String webRef) {

        StringBuffer buf = new StringBuffer(webRef);
        int x = 0;
        while (true) {

            x = webRef.indexOf(File.separatorChar, x);
            if (x == -1) {
                break;
            }
            buf.setCharAt(x, '/');
            x++;
        }
        x = webRef.indexOf(':', 0);
        if (x != -1) {
            buf.setCharAt(x, '|');
        }
        return buf.toString();
    }

    static final String webSep = "/";

    public static Vector getFileListFromDirectory(String directory, Vector vector){

        File directoryFile = new File(directory);
        String[] list = directoryFile.list();
        if (list == null) return vector;
        int cnt = 0;
        File file = null;

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
