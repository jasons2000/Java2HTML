package com.java2html.java_parser;

import com.java2html.Java2HTML;
import com.java2html.internal.HTMLFileWriter;
import com.java2html.internal.Link;
import com.java2html.testingtools.WebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

/**
 */
public class JavaSourceTest {

    private WebServer webServer;
    private JavaDocManager jdm ;



    @Before
    public void setup() throws Exception {
        webServer = new WebServer(getClass().getResource("/javadoc-sample/jsoup-1.7.2-javadoc").getPath(), "index.html");


        jdm = new JavaDocManager(new Link("http://localhost:" + webServer.getPort()));
    }

    @After
    public void cleanup() {
        webServer.close();
    }

    @Test
    public void testParse() throws Exception {

        JavaSource  javaSource = new JavaSource();

        Reader reader = new InputStreamReader( getClass().getResourceAsStream("/javasrc_sample/SampleInputSource.java"));

        StringWriter writer = new StringWriter();

        javaSource.toHtml(reader, new HTMLFileWriter(writer, 4, 4), ".", new Java2HTML(), jdm);

        System.out.println(writer);

    }
}