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
import com.java2html.internal.ParsingException;
import com.java2html.references.*;

import java.io.Reader;
import java.io.StringWriter;

public class JavaSource implements SourceParser {

    private boolean quiet = false;

    private JavaParser parser = new JavaParser(System.in); // Todo: System.in not required


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
    public String toHtml(SymbolTableByLanguage referenceMap, Reader reader) throws ParsingException {
        StringWriter sw = new StringWriter();
        HTMLFileWriter dest = new HTMLFileWriter(sw, 4,4); // todo make this HTML only, reworkout dependency

        parser.parse(reader,dest,referenceMap, prePath);
        dest.flush();
        return sw.toString();

    }

    @Override
    public String getLanguageId() {
        return "JAVA";
    }

    @Override
    public void parseReferences(SymbolTableMutable symbolTable, String fullPathFilename,Reader reader) {
        String packageName = PackageLocator.scan(reader);

         return packageName;

    }

}
