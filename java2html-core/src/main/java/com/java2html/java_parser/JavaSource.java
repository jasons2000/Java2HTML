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

import com.java2html.Java2HTML;
import com.java2html.internal.HTMLFileWriter;
import com.java2html.internal.Helper;

import java.io.*;
import java.util.*;

public class JavaSource {

    public Map<String, String> packageList = new HashMap<String, String>(); // TODO Make Private

    private boolean quiet = false;

    private JavaParser parser = new JavaParser(System.in); // Todo: System.in not required

   public static class PackageH {

        public PackageH(String packageLevel, String className) {
            this.packageLevel = packageLevel;
            this.className = className;
        }

        public String packageLevel;
        public String className;
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


    public JavaSource(String destination,
                      int marginSize,
                      int tabSize,
                      boolean header,
                      boolean footer) {

        this.destination = destination;
        this.marginSize = marginSize;
        this.tabSize = tabSize;
        this.header = header;
        this.footer = footer;
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



    public String processFile(Reader reader) throws IOException {

        return PackageLocator.scan( reader);
    }

    public void parse(Reader source, HTMLFileWriter dest, String preDir, Java2HTML java2HTML, JavaDocManager javaDoc) throws ParseException,IOException {
        parser.parse(source,dest,preDir,java2HTML, javaDoc);
    }
}
