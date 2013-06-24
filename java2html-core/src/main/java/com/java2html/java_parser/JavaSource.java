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

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

public class JavaSource {

    private boolean quiet = false;

    private JavaParser parser = new JavaParser(System.in); // Todo: System.in not required


    public final boolean isQuiet() {
      return quiet;
    } // isQuiet
    public final void setQuiet( final boolean quiet ) {
      this.quiet = quiet;
    } // setQuiet


    public String findReferences(String fullPathFilename, Reader reader) throws IOException {
        LineNumberReader lineNumberReader = new LineNumberReader(reader);
        String packageName = PackageLocator.scan(lineNumberReader);
        while (lineNumberReader.readLine() != null) {

        }
        System.out.println("*** LN=" +lineNumberReader.getLineNumber());

        return packageName;
    }

    public void toHtml(Reader source, HTMLFileWriter dest, String preDir, Java2HTML java2HTML, JavaDocManager javaDoc) throws ParseException,IOException {
        parser.parse(source,dest,preDir,java2HTML, javaDoc);
    }
}
