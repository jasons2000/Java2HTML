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

import com.java2html.internal.Link;
import com.java2html.references.ReferenceIdMutable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

// TODO :Does this deal with the import AClass; scenario, probabaly not

public class JavaDocManager {

    private ReferenceIdMutable referenceMapJavaDoc = new ReferenceIdMutable();

//    private final Map<String, String> classList = new HashMap<String, String>();
//    private final Map<String, String> packageList = new HashMap<String, String>(); //use JavaSource

    public JavaDocManager(Link... urls) throws IOException {
        ReferenceIdMutable referenceIdMutable = new ReferenceIdMutable("javaDoc");
        ReferenceIdMutable classes =  referenceIdMutable.add("classes");
        ReferenceIdMutable packages=  referenceIdMutable.add("packages");

        for (Link urlString : urls) {
            // todo need to handle non connectables
            URL url = new URL(urlString.getUrl());

            parsePackages(packages, new URL(url, "overview-frame.html"));
            parseClasses(classes ,new URL(url, "allclasses-frame.html"));
        }
    }

    private void parseClasses(ReferenceIdMutable classList, URL urlClasses) {
        //<TD NOWRAP><FONT CLASS="FrameItemFont"><A HREF="javax/swing/AbstractAction.html" title="class in javax.swing" target="classFrame">AbstractAction</A>
        try {
            Connection con = Jsoup.connect(urlClasses.toString()).userAgent("Mozilla");
            Document doc = con.get();

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String titleAttr = link.attr("title");
                String classRef = titleAttr.substring(titleAttr.indexOf(" in") + 4) + "." + link.text();
                classList.add(classRef).setHRef(new URL(urlClasses, link.attr("href")).toString());
            }
        }
        catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void parsePackages(ReferenceIdMutable packageList,URL urlPackages) {

        try {
            Connection con = Jsoup.connect(urlPackages.toString());
            Document doc = con.get();

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String text = link.text();
                 if (text.equals("All Classes")) continue;
                packageList.add(link.text()).setHRef(new URL(urlPackages, link.attr("href")).toString());
            }
        }
        catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public ReferenceIdMutable getReferenceMapJavaDoc() {
        return referenceMapJavaDoc;
    }


//    public String toString() {
//
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("*** Classes ***\n");
//
//        for (String s : classList.keySet()) {
//            sb.append(s + " = ");
//            sb.append(classList.get(s));
//            sb.append("\n");
//        }
//
//        sb.append("*** Packages ***\n");
//
//        for (String s : packageList.keySet()) {
//            sb.append(s +  " = ");
//            sb.append(packageList.get(s));
//            sb.append("\n");
//        }
//
//        return sb.toString();
//    }

//    public String getClassHRef(String classSpec) {
//        return classList.get(classSpec);
//    }
//
//    public String getPackageHRef(String packageSpec) {
//           return packageList.get(packageSpec);
//       }
}

//

