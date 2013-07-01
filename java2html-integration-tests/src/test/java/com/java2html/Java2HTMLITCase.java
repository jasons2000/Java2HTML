package com.java2html;


import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IntegrationTests.class)
public class Java2HTMLITCase {


    @Test
    public void testSampleSourceWithJavaDoc() throws Exception {

        runMainSplit("-js %JAVA2HTML_HOME%\\src\\tests\\testSource -d ..\\..\\..\\JAVA_1_6_0_doc  -n Tests -m 4");
    }

    @Test
    @Category(IntegrationTestsWithExternalDependencies.class)
    public void testFullJavaSource6() throws Exception {

        runMain("-js", "C:\\PROGRA~1\\Java\\jdk1.6.0_43\\src.zip", "-d", "./java2html-integration-tests/target/itcase6", "-n", "test_JavaSource_6", "-t", "4", "-l", "-q");
    }


    @Test
    @Category(IntegrationTestsWithExternalDependencies.class)
    public void testFullJavaSource7() throws Exception {
        runMain("-js", "C:\\PROGRA~1\\Java\\jdk1.7.0_17\\src.zip", "-d", "./java2html-integration-tests/target/itcase7", "-n", "test_JavaSource_7", "-t", "4", "-l", "-q");
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
