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

import com.src2html.internal.HTMLFileWriter;
import com.src2html.internal.ParsingException;
import com.src2html.references.SourceParser;
import com.src2html.references.Symbol;
import com.src2html.references.SymbolTable;

import java.io.*;
import java.util.Collection;

public class JavaSourceParser implements SourceParser<JavaSymbol> {

//    public Map<String, Map<String, String>> allClassesHRefByPackage = new HashMap<String, Map<String, String>>();
//    public Map<String, PackageH> directoryToPackage = new HashMap<String, PackageH>();

    private SymbolTable<JavaSymbol> symbolTable = new SymbolTable<JavaSymbol>();

    private boolean quiet = false;

    private JavaParser parser = new JavaParser(System.in); // Todo: System.in not required

    public JavaSourceParser() {
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
            populateSymbolTableWithFile(fullPath, packageName);
        } catch (IOException e) {
            throw new ParsingException(e);
        }
    }


    @Override
    public String snippetToHtml(String codeSnippet) {
        return null;
    }

    @Override
    public String toHtml(Reader reader, String pathToRoot, SourceParser<? extends Symbol>... otherLanguages) throws ParsingException {
        try {
            StringWriter sw = new StringWriter();
            HTMLFileWriter dest = new HTMLFileWriter(sw, 4, 4); // todo make this HTML only, reworkout dependency

            parser.parse(reader, dest, this, pathToRoot);
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
    public JavaSymbol lookup(String symbolId) {
        return symbolTable.lookup(symbolId);
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
    public Collection<JavaSymbol> getFileSymbolsInDir(String dirSymbolId) {
        return symbolTable.getFileSymbolsInDir(dirSymbolId);
    }


    private void populateSymbolTableWithFile(String fullPathFileName, String packageLevel) {

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
        String packageParent;
        String packageId;
        if (packageLevel == null || packageLevel.isEmpty()) {
            packageId = ""; //default package =""
            packageParent = null;
            href = fileName + ".html";
        } else {
            int ix = packageLevel.lastIndexOf(".");
            if (ix == -1) {
                packageId = packageLevel;
                packageParent = "";
            } else {
                packageId = packageLevel.substring(ix + 1);
                packageParent = packageLevel.substring(0, ix);
            }
            href = Helper.convertDots(packageLevel, Helper.webSep) +
                    Helper.webSep + fileName + ".html";
        }

        String packageHref = Helper.getHrefForClassIndexForPackage(packageLevel);
        JavaSymbol packageSymbol = new JavaSymbol( packageHref, packageId, packageParent, Symbol.Type.Dir);
        symbolTable.add(packageSymbol);

        JavaSymbol classSymbol = new JavaSymbol(href, classString, packageLevel, fullPathFileName);
        symbolTable.add(classSymbol);

    }

}
