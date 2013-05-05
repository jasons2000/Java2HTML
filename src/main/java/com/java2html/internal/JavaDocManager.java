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
import java.util.*;

// TODO :Does this deal with the import AClass; scenario, probabaly not

public class JavaDocManager {
    private static Hashtable classList = new Hashtable();
    public static Hashtable packageList = new Hashtable(); //use JavaSource

    public JavaDocManager(JavaDoc[] jdOptions) throws IOException {

        if (jdOptions == null) return;

        for (int i = 0; i < jdOptions.length; i++) {
            if (jdOptions[i].getHttpRef() == null) {
                String absolutePath = "file:///" +
                    Helper.convert( (jdOptions[i].getLocalRef()).
                                   getCanonicalPath());
                addDirectory(jdOptions[i].getLocalRef(), absolutePath);
            }
            else {
                addDirectory(jdOptions[i].getLocalRef(), jdOptions[i].getHttpRef());
            }
        }
    }

    public static void main(String[] args) throws Exception {

        JavaDoc jdo = new JavaDoc(new File(args[0]), args[1]);
        JavaDocManager jd = new JavaDocManager(new JavaDoc[] {jdo});
        System.out.println("ClassList:-");
        jd.print();

    }

    public void print() {

        Enumeration en = classList.keys();

        while (en.hasMoreElements()) {
            String s = (String) en.nextElement();
            System.out.print(s + ",");
            System.out.println( (String) classList.get(s));
        }
    }

    public String getClassHRef(String text) {
        return (String) classList.get(text);
    }

    public void addDirectory(File directory, String refDirectory) {
        //File dir = new File(directory);
        //if (dir.isDirectory()) throw new Exception ("Not Directory");
        add(directory, "", refDirectory, true);
    }

    private void add(File dir, String packageLevel, String refDirectory,
                     boolean top) {

        String[] list = dir.list();
        if (list == null) {
            return;
        }
        int cnt = 0;
        File file = null;

        while (cnt < list.length) {

            file = new File(dir.getAbsolutePath() + File.separatorChar + list[cnt]);
            if (file.isFile()) {
                if (list[cnt].equals("package-summary.html")) {
                    packageList.put(packageLevel,
                                    refDirectory + Helper.webSep + list[cnt]);
                }
                else if (list[cnt].endsWith(".html")) {
                    String classString = list[cnt].substring(0,
                        list[cnt].lastIndexOf('.'));
                    int isPackageFile = classString.lastIndexOf('.');
                    if (top && (isPackageFile != -1)) {
                        classList.put(classString,
                                      refDirectory + Helper.webSep + list[cnt]);
                    }
                    else {
                        classList.put(packageLevel + "." + classString,
                                      refDirectory + Helper.webSep + list[cnt]);
                    }
                }
            }
            else {

                //TODO need to ignore directories with . in the name
                String temp = packageLevel;
                if (temp == "") {
                    temp = list[cnt];
                }
                else {
                    temp = temp + "." + list[cnt];
                }
                add(new File(dir.getAbsolutePath() + File.separatorChar + list[cnt]), temp,
                    refDirectory + Helper.webSep + list[cnt], false);
            }
            cnt++;
        }
    }
}
