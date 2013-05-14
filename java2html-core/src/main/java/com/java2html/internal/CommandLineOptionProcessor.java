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

import com.java2html.BadOptionException;
import com.java2html.Java2HTML;
import com.java2html.JavaDoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CommandLineOptionProcessor {

    private String[] args;

    private boolean[] commandLineOptionSet;

    public CommandLineOptionProcessor(String[] args) {
        this.args = args;
        commandLineOptionSet = new boolean[args.length];
    }

    /**
     * @return false if just Help was requested
     * @throws BadOptionException
     */
    public boolean setOptionsFromCommandLine(Java2HTML java2HTML) throws
        BadOptionException {

        if (args.length == 0 ||
            getOptionFlag(new String[] {"help", "h", "?", "/?"})) {
            System.out.println(Java2HTML.bundle.getString("usage") );
            return false;
        }

        List<String> names = getOptionSingleValue(new String[] {"name", "n"}
                                              , false, 1);
        if (names.size() == 1) {
            java2HTML.setTitle(names.get(0));

        }
        java2HTML.setFooter(getOptionFlag(new String[] {"nofooter", "nf"}));

        java2HTML.setHeader(getOptionFlag(new String[] {"noheader", "nh"}));

        java2HTML.setSimple(getOptionFlag(new String[] {"simple", "s"}));

        java2HTML.setQuiet(getOptionFlag(new String[] {"quiet", "q"}));

        int[] margins = getOptionSingleIntegerValue(new String[] {"margin", "m"}
            , false, 1);
        if (margins.length == 1) {
            java2HTML.setMarginSize(margins[0]);

        }
        int[] tabs = getOptionSingleIntegerValue(new String[] {"tabs", "t"}
                                                 , false, 1);
        if (tabs.length == 1) {
            java2HTML.setTabSize(tabs[0]);

        }
        List<String> destinations = getOptionSingleValue(new String[] {
            "destination", "d"}
            , false, 1);
        // todo Create and Validate directories
        if (destinations.size() == 1) {
            java2HTML.setDestination(destinations.get(0));

        }
        List<String> sources = getOptionSingleValue(new String[] {"javasource",
                                                "js"}
                                                , false, -1);
        // todo validate directories
        if (sources.size() > 0) {
            java2HTML.setJavaDirectorySource(sources);

        }
        List<JavaDoc> javaDocsOptions = getOptionJavaDoc(new String[] {
            "javadoc", "jd"});
        // todo validate directories
        if (javaDocsOptions.size() > 0) {
            java2HTML.setJavaDoc(javaDocsOptions);

        }
        checkForUnkownArgs();

        return true;
    }

    private boolean getOptionFlag(String[] names) throws BadOptionException {
        int cnt = 0;
        int settingCount = 0;
        boolean flagSet = false;

        while (cnt < args.length) {

            for (int i = 0; i < names.length; i++) {
                if (args[cnt].equalsIgnoreCase("-" + names[i])) {
                    commandLineOptionSet[cnt] = true;
                    if (settingCount >= 1) {
                        throw new BadOptionException(names[0] +
                            " should only be set once");
                    }
                    settingCount++;
                    flagSet = true;
                    break;
                }
            }
            cnt++;
        }

        return flagSet;
    }

    /**
     * if maxSettings = -1 then assume no max limit
     *
     */
    private List<String> getOptionSingleValue(String[] names, boolean required,
                                          int maxSettingCount) throws
        BadOptionException {

        int cnt = 0;
        int settingCount = 0;
        Vector values = new Vector();

        while (cnt < args.length) {

            for (int i = 0; i < names.length; i++) {
                if (args[cnt].equalsIgnoreCase("-" + names[i])) {
                    commandLineOptionSet[cnt] = true;
                    cnt++;
                    if (settingCount == maxSettingCount) {
                        throw new BadOptionException(names[0] +
                            " should only be set " + maxSettingCount +
                            " time(s)");
                    }
                    if (cnt >= args.length) {
                        throw new BadOptionException("Value not set for " +
                            names[0]);
                    }

                    commandLineOptionSet[cnt] = true;
                    values.addElement(args[cnt]);
                    settingCount++;
                    break;
                }
            }
            cnt++;
        }
        if (required && settingCount == 0) {
            throw new BadOptionException(names[0] + " needs to be set");
        }
        return values;
    }

    private int[] getOptionSingleIntegerValue(String[] names, boolean required,
                                              int maxSettingCount) throws
        BadOptionException {

        int cnt = 0;
        int settingCount = 0;
        Vector values = new Vector();

        while (cnt < args.length) {

            for (int i = 0; i < names.length; i++) {
                if (args[cnt].equalsIgnoreCase("-" + names[i])) {
                    commandLineOptionSet[cnt] = true;
                    cnt++;
                    if (settingCount == maxSettingCount) {
                        throw new BadOptionException(names[0] +
                            " should only be set " + maxSettingCount +
                            " time(s)");
                    }
                    if (cnt >= args.length) {
                        throw new BadOptionException("Value not set for " +
                            names[0]);
                    }

                    commandLineOptionSet[cnt] = true;
                    values.addElement(args[cnt]);
                    settingCount++;
                    break;
                }
            }
            cnt++;
        }
        if (required && settingCount == 0) {
            throw new BadOptionException(names[0] + " needs to be set");
        }
        int[] intValues = null;
        try {
            intValues = convertToIntegerArray(values);
        }
        catch (Exception e) {
            throw new BadOptionException(names[0] + " must be a number"); // todo add bad value
        }
        return intValues;

    }

    public List<JavaDoc> getOptionJavaDoc(String[] names) throws
        BadOptionException {

        int cnt = 0;
        int settingCount = 0;
        List<JavaDoc> values = new ArrayList<JavaDoc>();

        while (cnt < args.length) {

            for (int i = 0; i < names.length; i++) {
                if (args[cnt].equalsIgnoreCase("-" + names[i])) {
                    commandLineOptionSet[cnt] = true;
                    cnt++;
                    //if (settingCount == maxSettingCount) throw new BadOptionException(names[0] + " should only be set " + maxSettingCount + " time(s)");
                    if (cnt >= args.length) {
                        throw new BadOptionException("Value not set for " +
                            names[0]);
                    }

                    commandLineOptionSet[cnt] = true;
                    JavaDoc jdOption = null;
                    File localRef = new File(args[cnt]);
                    String httpRef = null;
                    if ( (cnt + 1) < args.length &&
                        !args[cnt + 1].startsWith("-")) { // Second Parameter of jd option
                        cnt++;
                        if (!args[cnt].startsWith("http://")) {
                            throw new BadOptionException(
                                "Second Parameter of -jd/-javadoc option must start with 'http://'");
                        }
                        httpRef = args[cnt];
                        commandLineOptionSet[cnt] = true;
                    }
                    if (httpRef != null) {
                        jdOption = new JavaDoc(localRef, httpRef);
                    }
                    else {
                        jdOption = new JavaDoc(localRef);
                    }
                    values.add(jdOption);
                    settingCount++;
                    break;
                }
            }
            cnt++;
        }


        return values;
    }

    private String[] convertToStringArray(Vector vector) {
        String[] stringArray = new String[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            stringArray[i] = (String) vector.elementAt(i);
        }
        return stringArray;
    }

    private int[] convertToIntegerArray(Vector vector) {
        int[] integerArray = new int[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            integerArray[i] = Integer.parseInt( (String) vector.elementAt(i));
        }
        return integerArray;
    }

    public void checkForUnkownArgs() throws BadOptionException {
        for (int i = 0; i < commandLineOptionSet.length; i++) {
            if (!commandLineOptionSet[i]) {
                throw new BadOptionException("Unkown Argument :" + args[i]);
            }
        }
    }
}
