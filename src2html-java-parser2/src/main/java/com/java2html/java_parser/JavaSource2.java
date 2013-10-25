package com.java2html.java_parser;

import com.java2html.java_parser.ast.declaration.Program;
import com.java2html.java_parser.parser.DeclarationParser;

/**
 */
public class JavaSource2 {

    public static void main(String[] args) {
        Program p = DeclarationParser.parse("import x;      \nclass HelloWorl { int x;}");
        p.toString();

        System.out.println(p);
    }
}
