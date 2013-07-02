package com.java2html;


import com.java2html.testingtools.WebServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;


public class Java2HTMLITCase  {

    private WebServer webServer;

    @Before
    public void prepare() throws Exception {
        //        webServer = new WebServer();
        setBaseUrl("http://localhost:8080/test");
    }

    @Test
    public void test1() {
        beginAt("home.xhtml"); //Open the browser on http://localhost:8080/test/home.xhtml
        clickLink("login");
        assertTitleEquals("Login");
        setTextField("username", "test");
        setTextField("password", "test123");
        submit();
        assertTitleEquals("Welcome, test!");
    }

    @Test
    public void testSampleSourceWithJavaDoc() throws Exception {

        runMainSplit("-js %JAVA2HTML_HOME%\\src\\tests\\testSource -d ..\\..\\..\\JAVA_1_6_0_doc  -n Tests -m 4");
    }


    @Test
    public void testThisSourceWithJavaDoc() throws Exception {

//        runMain("-js " + js +" -jd http://docs.oracle.com/javase/6/docs/api -n The_Java2HTML_Source -t 4 -m 4");
    }

    private static void runMainSplit(String args) {
        Java2HTML.main(args.split(" "));
    }

    private static void runMain(String... args) {
        Java2HTML.main(args);
    }

}
