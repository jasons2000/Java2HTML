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

import java.io.*;
import java.util.Properties;


/**
 * Determines the target package for a given java source file. Performs a very
 * simple (and thus fast) parsing up to the point where the package is determined
 * and stops there.
 *
 * @author Leon Poyyayil
 */
public final class PackageLocator {

    // ----- members -----
    private static final boolean DEBUG = false;

    public static String scan(Reader reader) throws IOException {

        boolean mbInSingleLineComment = false;
        boolean mbInMultiLineComment = false;
        boolean mbPackageKeywordFound = false;
        StringBuilder mCurToken = new StringBuilder();
        StringBuilder packageName = new StringBuilder();

        int lnPrevChar = -1;
        int lnCurChar = reader.read();
        while (lnCurChar >= 0) {
            if (DEBUG) {
                System.out.println("" +
                        (lnCurChar < 0x20 ?
                                "0x" + Integer.toHexString(lnCurChar) :
                                "'" + (char) lnCurChar + "'") +
                        ": single=" + mbInSingleLineComment +
                        ", multi=" + mbInMultiLineComment +
                        ", pkg=" + mbPackageKeywordFound +
                        ", token='" + mCurToken + "'" +
                        ", package='" + packageName + "'");
            }
            switch (lnCurChar) {
                case '\n':
                case '\r':
                    mbInSingleLineComment = false;
                    break;

                case ' ':
                case '\f':
                case '\t':
                    break;

                case '/':
                    switch (lnPrevChar) {
                        case '/':
                            mbInSingleLineComment = true;
                            break;
                        case '*':
                            if (mbInMultiLineComment) {
                                mbInMultiLineComment = false;
                            }
                            break;
                    }
                    break;

                case '*':
                    switch (lnPrevChar) {
                        case '/':
                            mbInMultiLineComment = true;
                            break;
                    }
                    break;

                case ';':
                    if (!mbInSingleLineComment && !mbInMultiLineComment) {
                        if (mbPackageKeywordFound) {
                            return packageName.toString();
                        }
                    }
                    break;

                default:
                    if (!mbInSingleLineComment && !mbInMultiLineComment) {
                        switch (lnPrevChar) {
                            case ' ':
                            case '\f':
                            case '\t':
                            case '\n':
                            case '\r':
                                if (mCurToken.length() > 0 && !mbPackageKeywordFound) {
                                    // no non-comment/non-whitespace tokens before a package are allowed
                                    // (not even annotations: these need to be in a special file: 'package-info.java')
                                    return packageName.toString();
                                }
                                break;
                        }
                        if (mbPackageKeywordFound) {
                            packageName.append((char) lnCurChar);
                        }
                        else {
                            mCurToken.append((char) lnCurChar);
                        }
                    }
                    break;
            }
            if (mCurToken.length() > 0) {
                mbPackageKeywordFound = "package".equals(mCurToken.toString());
            }
            if (DEBUG) {
              /*
              System.out.println( "" +
                ( lnCurChar < 0x20 ?
                  "0x" + Integer.toHexString( lnCurChar ) :
                  "'" + (char)lnCurChar + "'" ) +
                ": single=" + mbInSingleLineComment +
                ", multi=" + mbInMultiLineComment +
                ", pkg=" + mbPackageKeywordFound +
                ", token='" + mCurToken + "'" +
                ", package='" + mPackageName + "'" );
              //*/
            }
            lnPrevChar = lnCurChar;
            lnCurChar = reader.read();
        }
        return packageName.toString();

    } // scan


    public static void main(
            final String[] pArgs
    ) throws Exception {
        if (pArgs == null || pArgs.length == 0) {
            throw new Exception("no test directory(-ies) specified");
        }
        for (int lnA = 0; lnA < pArgs.length; lnA++) {
            final File lTestDir = new File(pArgs[lnA]);
            if (!lTestDir.isDirectory()) {
                throw new Exception("not a directory: '" + lTestDir.getAbsolutePath() + "'");
            }

            final File lExpectedList = new File(lTestDir, "expected-packages.properties");
            if (!lExpectedList.isFile()) {
                throw new Exception(lExpectedList.getAbsolutePath() + " not found");
            }

            final Properties lExpectedPackageNames = new Properties();
            final FileInputStream lExpectedListStream = new FileInputStream(lExpectedList);
            try {
                lExpectedPackageNames.load(lExpectedListStream);
            }
            finally {
                lExpectedListStream.close();
            }

            final PackageLocator lLocator = new PackageLocator();

            final File[] lTestFiles = lTestDir.listFiles();
            int lnTestCount = 0;
            int lnErrorCount = 0;
            for (int lnF = 0; lnF < lTestFiles.length; lnF++) {
                final File lTestFile = lTestFiles[lnF];
                if (lTestFile.isFile()) {
                    final String lTestFileName = lTestFile.getName();
                    if (lTestFileName.endsWith(".java")) {
                        final String lExpectedPackageName = lExpectedPackageNames.getProperty(lTestFileName);
                        if (lExpectedPackageName != null) {
                            if (DEBUG) {
                                System.out.println("===========================================================");
                            }
                            final String lPackageName = lLocator.scan(new BufferedReader(new FileReader(lTestFile.getAbsolutePath())));
                            if (!lExpectedPackageName.equals(lPackageName)) {
                                if (DEBUG) {
                                    System.out.println("--------------------------");
                                }
                                System.out.println(lTestFileName + " expected package '" +
                                        lExpectedPackageName + "' but got '" + lPackageName + "'");
                                lnErrorCount++;
                            }
                            lnTestCount++;
                        }
                        else {
                            throw new Exception(
                                    "test case without expected package: " + lTestFile.getAbsolutePath());
                        }
                    }
                }
            }

            if (lnErrorCount > 0) {
                throw new Exception(lnErrorCount + " tests failed");
            }
            if (lnTestCount == 0) {
                throw new Exception("no test .java files found in " + lTestDir.getAbsolutePath());
            }
        }
    } // main


} // class PackageLocator
