package com.java2html;

public class BaseJava2HTMLTest {

    protected static void runMainSplit(String args) {
        Java2HTML.main(args.split(" "));
    }

    protected static void runMain(String... args) {
        Java2HTML.main(args);
    }
}
