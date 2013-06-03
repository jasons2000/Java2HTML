package com.java2html.internal;

import com.java2html.WebServer;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 */
public class JavaDocManagerTest {

    @Test
    public void test() throws Exception {

        WebServer webServer = new WebServer(this.getClass().getResource("/javadoc-sample/jsoup-1.7.2-javadoc").getPath(), "index.html");

        JavaDocManager jdm = new JavaDocManager("http://localhost:" + webServer.getPort());
        contains(jdm.getPackageHRef("org.jsoup.select"), "jsoup/select/package-frame.html");

        contains(jdm.getClassHRef("org.jsoup.select.Selector.SelectorParseException"), "/org/jsoup/select/Selector.SelectorParseException.html");
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
