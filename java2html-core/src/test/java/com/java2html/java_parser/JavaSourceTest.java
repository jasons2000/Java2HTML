package com.java2html.java_parser;

import com.java2html.Java2HTML;
import com.java2html.internal.HTMLFileWriter;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

/**
 */
public class JavaSourceTest {
    @Test
    public void testParse() throws Exception {

        JavaSource  javaSource = new JavaSource();

        StringReader reader = new StringReader("class AJavaClass{ private String aString;}");

        StringWriter writer = new StringWriter();

        javaSource.toHtml(reader, new HTMLFileWriter(writer, 4, 4), ".", new Java2HTML(), new JavaDocManager());

        System.out.println(writer);

    }
}
