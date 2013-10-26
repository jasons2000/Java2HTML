package com.src2html.java_parser;

import com.src2html.internal.Link;
import com.src2html.testingtools.WebServer;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 */
public class JavaDocManagerTest {

    @Test
    public void test() throws Exception {

        WebServer webServer = new WebServer(getClass().getResource("/javadoc-sample/jsoup-1.7.2-javadoc").getPath(), "index.html");

        JavaDocManager jdm = new JavaDocManager(new Link("http://localhost:" + webServer.getPort()));
        contains(jdm.getSymbolTable().lookup("org.jsoup.select").getHRef(""), "jsoup/select/package-frame.html");

        contains(jdm.getSymbolTable().lookup("org.jsoup.select.Selector.SelectorParseException").getHRef(""), "/org/jsoup/select/Selector.SelectorParseException.html");
        System.out.println(jdm.toString());

        webServer.close();
    }

    private static void contains(String s, String... contains) {
        assertTrue(s.contains("http://localhost:"));
        for (String contain : contains) {
            assertTrue(s.contains(contain));
        }
    }
}
