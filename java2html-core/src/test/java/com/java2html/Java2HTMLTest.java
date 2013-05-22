package com.java2html;


import org.junit.Test;

public class Java2HTMLTest {

    @Test
    public void testSampleSourceWithJavaDoc() throws Exception {

        runMain("-js %JAVA2HTML_HOME%\\src\\tests\\testSource -d ..\\..\\..\\JAVA_1_6_0_doc  -n Tests -m 4");
    }

    @Test
    public void testSampleJava() throws Exception {
        runMain("-js ./testSource -n test_Source -t 4 -m 4");
    }

    @Test
    public void testThisSourceWithJavaDoc() throws Exception {
        String js = getClass().getResource("/sample_java/testSource").getFile();
        runMain("-js " + js +" -jd http://docs.oracle.com/javase/6/docs/api -n The_Java2HTML_Source -t 4 -m 4");
    }

    @Test
    public void testJava6Source() throws Exception {
        runMain("j2h -js C:\\J2sdK1.4.0\\src");
    }

    private static void runMain(String args) {
        Java2HTML.main(args.split(" "));
    }

}
