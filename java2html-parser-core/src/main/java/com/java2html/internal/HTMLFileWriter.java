package com.java2html.internal;

/*
 * Copyright (c) 2013. test license
 */


import org.apache.commons.io.IOUtils;

import java.io.*;


public class HTMLFileWriter extends Writer {

    private final Writer wrappedWriter;
    private final LineNumberReader lineNumberReader; //todo what does this do?
    private final PipedWriter pipedWriter;

    private int lineCount = 0;
    final private int convertTabsToSpacesCount;
    private boolean wasCR = false;
    private boolean useLineNumbers = true;

    /**
     * Margin Size (only used if using Line Numbers)
     */
    final private int lineNumberMargin;
    private int skipNewLineCharacter;



    public HTMLFileWriter(Writer wrappedWriter, int lineNumberMargin, int convertTabsToSpacesCount) throws IOException {
        this.wrappedWriter = wrappedWriter;

        pipedWriter = new PipedWriter();

        this.lineNumberReader = new LineNumberReader(new PipedReader(pipedWriter));

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
        throw new UnsupportedOperationException();
    }

    public void write(String str) {

        try {
                wrappedWriter.write(getHTMLParsedText(str));
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
    private void writeLineNumber(StringBuilder s) {

        s.append(System.getProperty("line.separator"));
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

        StringBuilder s = new StringBuilder();
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

    private String getHTMLParsedText(String str) throws IOException {
        //        return StringEscapeUtils.escapeHtml(str);
        int cnt = 0;
        int x;
        StringBuilder s = new StringBuilder();
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
                    s.append((char) c);
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
}
