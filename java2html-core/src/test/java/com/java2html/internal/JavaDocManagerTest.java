package com.java2html.internal;

import org.junit.Test;

import java.io.IOException;

/**
 */
public class JavaDocManagerTest {

    @Test
    public void test() throws IOException {

        // test need internet connection

        JavaDocManager javaDocManager = new JavaDocManager("http://docs.oracle.com/javase/6/docs/api/" );
      //  System.out.println(javaDocManager.toString());

    }
}
