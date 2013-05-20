package com.java2html;


import org.junit.Test;

import java.util.Arrays;

public class Java2HTMLTest {

    @Test
    public void testSampleJava() throws Exception {
        Java2HTML java2HTML = new Java2HTML();

        java2HTML.setJavaDirectorySource(Arrays.asList("./src/test/sample_java/testSource"));
        java2HTML.setDestination("target/junitOutput");
        java2HTML.setTitle("Unit Test");

        java2HTML.buildJava2HTML();



        String testJ2H = "-j .\\testSource -n The_Java2HTML_Source -t 4 -m 4";



    }

    @Test
    public void testSampleSourceWithJavaDoc() throws Exception {
        String example3 = "-js %JAVA2HTML_HOME%\\src\\tests\\testSource -d ..\\..\\..\\JAVA_1_6_0_doc  -n Tests -m 4";
    }

    @Test
    public void testThisSourceWithJavaDoc() throws Exception {
        String test2JavaDocOnEnd = "-js C:\\work\\dev\\java2html\\src\\java -jd C:\\j2sdk1.4.0\\docs\\api -n The_Java2HTML_Source -t 4 -m 4";

    }

    @Test
    public void testJava6Source() throws Exception {
        String test2SDK = "j2h -js C:\\J2sdK1.4.0\\src";

    }


}
