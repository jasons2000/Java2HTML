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

import java.io.*;
import java.util.*;

public class JavaSource implements SourceParser<JavaSymbol> {

//    public Map<String, Map<String, String>> allClassesHRefByPackage = new HashMap<String, Map<String, String>>();
//    public Map<String, PackageH> directoryToPackage = new HashMap<String, PackageH>();

    private SymbolTable<JavaSymbol> symbolTable = new SymbolTable<JavaSymbol>();

    private boolean quiet = false;

    private JavaParser parser = new JavaParser(System.in); // Todo: System.in not required

    public JavaSource() {

    }

    public final boolean isQuiet() {
        return quiet;
    } // isQuiet

    public final void setQuiet(final boolean quiet) {
        this.quiet = quiet;
    } // setQuiet


    @Override
    public boolean isFileNameMatch(String fileName) {
        return fileName.endsWith(".java");
    }

    @Override
    public void populateForReference(String fullPath, Reader reader) throws ParsingException {

        try {


            String packageName = PackageLocator.scan(reader);
            populateSymbolTable(fullPath, packageName);
        } catch (IOException e) {
            throw new ParsingException(e);
        }
    }


    @Override
    public String snippetToHtml(String codeSnippet) {
        return null;
    }

    @Override
    public String toHtml(File file, SourceParser<? extends Symbol> otherLanguages) throws ParsingException {
        try {
            StringWriter sw = new StringWriter();
            HTMLFileWriter dest = new HTMLFileWriter(sw, 4, 4); // todo make this HTML only, reworkout dependency

            parser.parse(reader, dest, symbolTable, prePath);
            dest.flush();
            return sw.toString();
        } catch (IOException ex) {
            throw new ParsingException(ex);
        }
    }


    @Override
    public String getLanguageId() {
        return "JAVA";
    }

    @Override
    public Collection<JavaSymbol> getAllFileSymbols() {
        return symbolTable.getAllFileSymbols();
    }

    @Override
    public Collection<JavaSymbol> getAllDirSymbols() {
        return symbolTable.getAllDirSymbols();
    }

    @Override
    public Collection<JavaSymbol> getScopedFiles(JavaSymbol limitingScope) {
        return symbolTable.getFileSymbolsInDir(limitingScope.getId());
    }

    private void populateSymbolTable(String fullPathFileName, String packageLevel) {

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
        } else {
            href = Helper.convertDots(packageLevel, '/') +
                    Helper.webSep + fileName + ".html";
        }

        JavaSymbol packageSymbol = new JavaSymbol(href, packageLevel, null, Symbol.Type.Dir);
        symbolTable.add(packageSymbol);

        JavaSymbol classSymbol = new JavaSymbol(href, packageLevel + "." + classString, packageSymbol, Symbol.Type.File);
        symbolTable.add(classSymbol);

    }

}
