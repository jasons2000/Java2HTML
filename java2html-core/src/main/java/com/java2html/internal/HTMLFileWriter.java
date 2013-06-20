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

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class HTMLFileWriter extends Writer {

    private final Writer wrappedWriter;

    private boolean htmlMode = false;
    private int lineCount = 0;
    final private int convertTabsToSpacesCount;
    private int skipNewLineCharacter = 0;
    private boolean useLineNumbers = true;

    /**
     * Margin Size (only used if using Line Numbers)
     */
    final private int lineNumberMargin;

    public void setHTMLMode(boolean htmlMode) {
        this.htmlMode = htmlMode;
    }


    public HTMLFileWriter(Writer wrappedWriter, int lineNumberMargin, int convertTabsToSpacesCount) throws IOException {
        this.wrappedWriter = wrappedWriter;

        if (lineNumberMargin > 64) {
            throw new IllegalArgumentException("Margin too Large");
        }
        if (lineNumberMargin < 0) {
            throw new IllegalArgumentException("Margin too Small");
        }
        if (convertTabsToSpacesCount > 64) {
            throw new IllegalArgumentException("TabSize too Large");
        }
        if (convertTabsToSpacesCount < 1) {
            throw new IllegalArgumentException("TabSize too Small");
        }
        if (lineNumberMargin == 0) {
            this.useLineNumbers = false;
        }

        this.lineNumberMargin = lineNumberMargin;
        this.convertTabsToSpacesCount = convertTabsToSpacesCount;
    }


    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {


    }

    public void write(String str)  {

        try {
            if (htmlMode) {
                wrappedWriter.write(getHTMLParsedText(str));
            }
            else {
                wrappedWriter.write(str);
            }
        }
        catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void flush() throws IOException {
        wrappedWriter.flush();
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(wrappedWriter);
    }

    /**
     * writes a line number if option is turned on
     */
    private void writeLineNumber(StringBuffer s) {

        s.append(Helper.lineSep);
        lineCount++;
        if (useLineNumbers) {
            int c = lineCount;
            int tmp = lineNumberMargin;
            int fact = 10;
            while (tmp > 1) {
                fact = fact * 10;
                tmp--;
            }

            c = c % fact;
            String num = "" + c;
            int x = lineNumberMargin - num.length();
            s.append("<FONT CLASS=\"LN\">");
            if (lineCount < fact) {
                s.append(c);
                while (x > 0) {
                    s.append(' ');
                    x--;
                }
            }
            else {
                while (x > 0) {
                    s.append('0');
                    x--;
                }
                s.append(num);
            }
            s.append("</FONT>");
        }
        s.append("<A NAME=\"" + lineCount + "\"></A>");
    }

    /**
     * Write the first line number
     */
    public String getFirstLineNumber() {

        StringBuffer s = new StringBuffer();
        lineCount++;

        if (useLineNumbers) {

            String num = "" + lineCount;
            int x = lineNumberMargin - num.length();
            s.append("<FONT CLASS=\"LN\">" + lineCount);
            while (x > 0) {
                s.append(' ');
                x--;
            }
            s.append("</FONT>");
        }
        s.append("<A NAME=\"" + lineCount + "\"></A>");

        return s.toString();
    }

    private int charCount = 0;

    private String getHTMLParsedText(String str) {
//        return StringEscapeUtils.escapeHtml(str);
        int cnt = 0;
        int x;
        StringBuffer s = new StringBuffer();
        final int len = str.length();
        int c;
        while (cnt < len) {
            c = str.charAt(cnt);
            switch (c) {
                case '&':
                    s.append("&amp;");
                    charCount++;
                    break;

                case '<':
                    s.append("&lt;");
                    charCount++;
                    break;

                case '>':
                    s.append("&gt;");
                    charCount++;
                    break;

                case '\r':
                    charCount = 0;
                    writeLineNumber(s);
                    skipNewLineCharacter = 2;

                    break;

                case '\n':
                    charCount = 0;
                    if (skipNewLineCharacter == 0) {
                        writeLineNumber(s);
                    }
                    break;

                case '\t':
                    x = convertTabsToSpacesCount -
                        charCount % convertTabsToSpacesCount;
                    while (x > 0) {
                        s.append(' ');
                        x--;
                        charCount++;
                    }

                    break;

                default:
                    s.append( (char) c);
                    charCount++;
                    break;
            }
            cnt++;

            if (skipNewLineCharacter > 0) {
                skipNewLineCharacter--;
            }
        }
        return s.toString();
    }

    /* TODO: added conversion of tabs to spaces */

    public static void main(String[] args) throws IOException {
        HTMLFileWriter fw = new HTMLFileWriter(new StringWriter(), 4, 4);
        fw.setHTMLMode(true);
        fw.write("<PRE><H1> This is {@value}  an &amp; Test </TEST></H1>" +
                 Helper.lineSep);
        fw.setHTMLMode(false);
        fw.write("<PRE><H1> This is {@value}  an &amp; Test </TEST></H1>" +
                 Helper.lineSep);


        fw.close();

    }
}
