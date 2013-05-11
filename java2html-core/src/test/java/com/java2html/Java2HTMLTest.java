package com.java2html;


import org.junit.Test;

import java.util.Arrays;

public class Java2HTMLTest {

    @Test
    public void testBuildJava2HTML() throws Exception {
        Java2HTML java2HTML = new Java2HTML();

        java2HTML.setJavaDirectorySource(Arrays.asList("./src/test/sample_java/testSource"));
        java2HTML.setDestination("target/junitOutput");
        java2HTML.setTitle("Unit Test");

        java2HTML.buildJava2HTML();

    }
}
