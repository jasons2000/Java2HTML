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

public class JavaSource implements SourceParser {

    public Map<String, Map<String, String>> allClassesHRefByPackage = new HashMap<String, Map<String, String>>();
    public Map<String, PackageH> directoryToPackage = new HashMap<String, PackageH>();

    private SymbolTable<JavaSymbol> symbolTable = new SymbolTable<JavaSymbol>();

    private boolean quiet = false;

    private JavaParser parser = new JavaParser(System.in); // Todo: System.in not required

    public JavaSource() {

    }

    public final boolean isQuiet() {
      return quiet;
    } // isQuiet
    public final void setQuiet( final boolean quiet ) {
      this.quiet = quiet;
    } // setQuiet


    @Override
    public boolean isFileNameMatch(String fileName) {
        return fileName.endsWith(".java");
    }

    @Override
    public void populateForReference(File file) throws ParsingException {

        try {
            String packageName = PackageLocator.scan( new FileReader(file));
            fn(file.getAbsolutePath(), packageName);
        }
        catch (IOException e) {
            throw new ParsingException(e);
        }

    }


    @Override
    public String snippetToHtml(String codeSnippet) {
        return null;
    }

    @Override
    public String toHtml(Reader reader, SourceParser<? extends Symbol> otherLanguages) throws ParsingException {

        try {
            StringWriter sw = new StringWriter();
            HTMLFileWriter dest = new HTMLFileWriter(sw, 4,4); // todo make this HTML only, reworkout dependency

            parser.parse(reader,dest, this, prePath);
            dest.flush();
            return sw.toString();
        }
        catch (IOException ex) {
            throw new ParsingException(ex);
        }
    }


    @Override
    public String getLanguageId() {
        return "JAVA";
    }

    @Override
    public List getAllFileSymbol() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List getAllLimitingScopes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List getScopedFiles(Symbol limitingScope) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void fn(String fullPathFileName, String packageLevel) throws FileNotFoundException {


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
           }
           else {
               href = Helper.convertDots(packageLevel, '/') +
                       Helper.webSep + fileName + ".html";
           }
           //System.out.println("Package 1st Parsed="+packageLevel);

           // put the packagename into hashtable to cross reference filename with packageName on second parse

           directoryToPackage.put(fullPathFileName, new PackageH(packageLevel, classString));

           // put the package + className into hashtable to determin class refercnes
           Map<String, String> hrefByClassName = allClassesHRefByPackage.get(packageLevel);
           if (hrefByClassName == null) {
               hrefByClassName = new HashMap<String, String>();
               allClassesHRefByPackage.put(packageLevel, hrefByClassName);
           }
           hrefByClassName.put(classString, href);

       }

}
