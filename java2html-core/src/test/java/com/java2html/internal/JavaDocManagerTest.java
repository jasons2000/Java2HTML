package com.java2html.internal;

import com.java2html.WebServer;
import org.junit.Test;

import java.io.IOException;

/**
 */
public class JavaDocManagerTest {

    @Test
    public void test() throws Exception {

        // test need internet connection

        WebServer  webServer = new WebServer("./java2html-core/src/test/resources/javadoc-sample/jsoup-1.7.2-javadoc", "index.html");

        JavaDocManager javaDocManager = new JavaDocManager("http://localhost:" + webServer.getPort() );
        System.out.println(javaDocManager.toString());

    }
}
