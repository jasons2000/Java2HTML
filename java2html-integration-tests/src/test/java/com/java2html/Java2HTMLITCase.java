package com.java2html;


import org.junit.Test;

public class Java2HTMLITCase {

//    private final static String TEST_SOURCE = "./java2html-core/src/test/sample_java/testSource";




    @Test
    public void testSampleSourceWithJavaDoc() throws Exception {

        runMainSplit("-js %JAVA2HTML_HOME%\\src\\tests\\testSource -d ..\\..\\..\\JAVA_1_6_0_doc  -n Tests -m 4");
    }

    @Test
    public void testFullJavaSource() throws Exception {
        String source6 = "C:\\PROGRA~1\\Java\\jdk1.6.0_43\\src.zip" ;
        String source7 = "C:\\PROGRA~1\\Java\\jdk1.7.0_17\\src.zip" ;
        runMain( "-js", source7, "-d" , "./java2html-integration-tests/target/itcase" ,"-n", "test_Source", "-t", "4", "-l", "-q");
    }

    @Test
    public void testThisSourceWithJavaDoc() throws Exception {

//        runMain("-js " + js +" -jd http://docs.oracle.com/javase/6/docs/api -n The_Java2HTML_Source -t 4 -m 4");
    }

    @Test
    public void testJava6Source() throws Exception {
        runMainSplit("j2h -js C:\\J2sdK1.4.0\\src");

    }

    @Test
     public void testJava7Source() throws Exception {
        runMainSplit("j2h -js C:\\J2sdK1.4.0\\src");
     }

    private static void runMainSplit(String args) {
        Java2HTML.main(args.split(" "));
    }

    private static void runMain(String... args) {
          Java2HTML.main(args);
      }

}
