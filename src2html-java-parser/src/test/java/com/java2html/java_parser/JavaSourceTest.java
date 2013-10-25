package com.java2html.java_parser;

import com.java2html.internal.Link;
import com.java2html.testingtools.WebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

/**
 */
public class JavaSourceTest {

    private WebServer webServer;
    private JavaDocManager jdm;


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

        JavaSourceParser javaSourceParser = new JavaSourceParser();

        Reader reader = new InputStreamReader(getClass().getResourceAsStream("/javasrc_sample/SampleInputSource.java"));

        StringWriter writer = new StringWriter();

        javaSourceParser.toHtml(reader, "");

        System.out.println(writer);
    }

    @Test
    public void testLoopIndefinitely() throws IOException, ParseException {
        JavaSourceParser javaSourceParser = new JavaSourceParser();

        Reader reader = new InputStreamReader(getClass().getResourceAsStream("/javasrc_sample/LoopIndefinitelyBug.java"));

        StringWriter writer = new StringWriter();

        javaSourceParser.toHtml(reader, "");

        System.out.println(writer);

    }
}
