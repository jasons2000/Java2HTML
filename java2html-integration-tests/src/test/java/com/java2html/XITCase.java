package com.java2html;

import com.java2html.testingtools.WebServer;
import org.junit.Before;
import org.junit.Test;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class XITCase {

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
}