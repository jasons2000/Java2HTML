package com.java2html;

import com.java2html.categories.IntegrationTestsWithExternalDependencies;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IntegrationTestsWithExternalDependencies.class)
public class ProprietaryTest extends BaseJava2HTMLTest {

    @Test
    public void testFullJavaSource6() throws Exception {

        runMain("-js", "C:\\PROGRA~1\\Java\\jdk1.6.0_43\\src.zip", "-d", "./java2html-integration-tests/target/itcase6", "-n", "test_JavaSource_6", "-t", "4", "-l", "-q");
    }


    @Test
    public void testFullJavaSource7() throws Exception {
        runMain("-js", "C:\\PROGRA~1\\Java\\jdk1.7.0_17\\src.zip", "-d", "./java2html-integration-tests/target/itcase7", "-n", "test_JavaSource_7", "-t", "4", "-l", "-q");
    }

}
