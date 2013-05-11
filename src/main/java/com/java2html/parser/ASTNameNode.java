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

/* Generated By:JJTree: Do not edit this line. ASTNameNode.java */
// Modiefied by JS
package com.java2html.parser;

import java.util.*;
import com.java2html.*;
import com.java2html.internal.*;

public class ASTNameNode
    extends SimpleNode {

    /*public ASTNameNode(int id) {
        super(id);
    }*/

    public ASTNameNode(JavaParser p, int id) {
        super(p, id);
    }

    String text = null;
    boolean isPackage = false;
    boolean isImport = false;
    boolean isAnnotation = false;

    protected String getTokenClass(Token t) {
      if ( isAnnotation ) {
        return "Annotation";
      }
      return super.getTokenClass( t );
    } // getTokenClass

    private String getRef(String text) {
        String href = parser.javaSource.getClassHRef(text);
        if (href == null) {
            href = parser.javaDoc.getClassHRef(text);
        }
        else {
            href = parser.prePath + href;
        }
        return href;
    }

    private String getRefNoDots(String text) {

        String href = null;
        String s = null;

        // prepending a dot to the given name to prevent matching on imports which
        // accidentially end with the given text but aren't what we're looking for:
        // 'import com.foo.bar.FooBar;' shouldn't be matching when we're looking for 'Bar';
        final String qtext = "." + text;

        Enumeration e = parser.importList.elements(); // get the list of imports for this file
        while (e.hasMoreElements()) {
            s = (String) e.nextElement();
            if (s.endsWith(qtext)) { // is it in a non '.*' import
                if (s.indexOf(".") == -1) { // TODO: this deals with import AClass;, need to look at
                    href = getRef("." + s);
                }
                else {
                    href = getRef(s);
                }
                break;
            }
            else if (s.endsWith(".*")) { // is it in '.*' import
                String tx = s.substring(0, s.length() - 1) + text; // generate a fully qualified name
                //System.out.println("***************************"+tx);
                href = getRef(tx); //get the ref to this if it exists
                if (href != null) {
                    break;
                }
            }
        }
        return href;

    }


    public void process(HTMLFileWriter ostr) {

        Token t = begin;
        //System.out.println("Begin:"+t);
        //System.out.println("End:"+end);

        printSpecial(t.specialToken, ostr);
        //System.out.println("TEXT="+text);

        String href = null;
        String extra = "";

        if (isPackage) {

            href = (String) parser.javaSource.packageList.get(text);
            if (href != null) {
                href = parser.prePath + href;
                extra = " target=\"packageFrame\"";
            }

        }
        else if (text.endsWith(".*")) {
            //System.out.println("************************text"+text);

            href = (String) parser.javaSource.packageList.get(text.substring(0,
                text.length() - 2));
            if (href != null) {
                href = parser.prePath + href;
                extra = " target=\"packageFrame\"";
            }
            else {
                href = JavaDocManager.packageList.get(text.substring(0,
                    text.length() - 2));
            }
        }
        else if ( (text.indexOf('.')) != -1) { // Is there a '.' in text, if so then
            // Possibly fully qualified name
            String tempText = text;

            while (true) {
                if (tempText.indexOf('.') == -1) {
                    href = getRefNoDots(tempText);
                }
                else {
                    href = getRef(tempText); //get the href fors this fully qualified name
                }
                if (href != null) {
                    break;
                }
                int lastIndex = tempText.lastIndexOf('.');
                if (lastIndex == -1) {
                    break;
                }
                tempText = tempText.substring(0, lastIndex);
                //System.out.println("tempText="+ tempText);
            }
        }
        else {
            href = getRefNoDots(text);
        }


        if (href != null) {
            ostr.setHTMLMode(false);
            ostr.write("<A HREF=\"" + href + "\"" + extra + ">");
            ostr.setHTMLMode(true);
        }

        printToken(t, ostr);

        if (href != null && !isPackage && !isImport) {
            // for links which don't point to a package or import we close the link now
            // this looks nicer when having matched on static methods (which now only underlines
            // the class and not the called method too) and on generic types (again only the
            // class of the generic type and not its parameter list too).
            ostr.setHTMLMode(false);
            ostr.write("</A>");
            ostr.setHTMLMode(true);
            href = null; // prevent double closing of the tag below
        }

        //tidy up
        if (t == end) {
            if (href != null) {
                ostr.setHTMLMode(false);
                ostr.write("</A>");
                ostr.setHTMLMode(true);
            }
            return;
        }
        t = t.next;

        while (t != end) {
            print(t, ostr);
            t = t.next;
        }
        print(t, ostr);
        if (href != null) {
            ostr.setHTMLMode(false);
            ostr.write("</A>");
            ostr.setHTMLMode(true);
        }
    }
}