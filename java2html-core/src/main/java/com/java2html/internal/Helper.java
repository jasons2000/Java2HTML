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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class Helper {

    public static String lineSep = System.getProperty("line.separator");

    public static final String webSep = "/";

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

        // top left
    public final static String getPreIndex(String title) {
        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">" +
            lineSep +
            "<HTML>" + lineSep +
            "<HEAD>" + lineSep +
            "<META NAME=\"GENERATOR\" CONTENT=\"" + Helper.version + "\">" +
            lineSep +
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

    public static void createPackageIndex(String dest, String title, Hashtable classList,Hashtable packageList) throws
            IOException {

            FileWriter file = new FileWriter(dest + File.separator +
                                             "packages.html");
            StringBuffer index = new StringBuffer(Helper.getPreIndex(title));

            Enumeration e = classList.keys();
            Vector sortedVector = new Vector();
            String text;

            createAllClassIndex(dest, classList);

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
                href = createClassIndex(dest, text, classList);

                //System.out.println("text="+text+" href=" + href);
                packageList.put(text, href);
                if (text.equals("")) {
                    text = "[DEFAULT]";
                }
                index.append("<BR>" + Helper.lineSep +
                             "<FONT CLASS=\"FrameItemFont\"><A HREF=\"" + href +
                             "\" TARGET=\"packageFrame\">" + text + "</A></FONT>");
            }

            index.append(Helper.postIndex);
            file.write(index.toString());
            file.close();
        }

    static String createClassIndex(String dest, String packageString, Hashtable classList) throws
           IOException {

           String href2 = (packageString.equals("") ? "default" : packageString) +
               ".index.html";
           FileWriter file = new FileWriter(dest + File.separatorChar + href2);
           if (packageString.equals("")) {
               file.write(Helper.getClassesFrame("Package Default"));
           }
           else {
               file.write(Helper.getClassesFrame("Package " + packageString));
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
           String href;

           e = sortedVector.elements();
           while (e.hasMoreElements()) {

               text = (String) e.nextElement();
               href = (String) ht.get(text);

               file.write("<BR>" + Helper.lineSep +
                          "<FONT CLASS=\"FrameItemFont\"><A HREF=\"" + href +
                          "\" TARGET=\"SourceFrame\">" + text + "</A></FONT>");
           }
           file.write(Helper.postIndex);
           file.close();
           return href2;
       }


    private static class Pair {

        Pair(String text, String ref) {
            this.text = text;
            this.ref = ref;
        }

        String text;
        String ref;
    }



    static void createAllClassIndex(String dest, Hashtable classList) throws IOException {

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
        Enumeration ef;
        String text;
        Pair p;

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
        file.write(Helper.postIndex);
        file.close();

    }





}
