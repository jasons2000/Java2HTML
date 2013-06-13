package com.java2html.references;


import java.io.Reader;

public interface SourceParser extends ReferenceParser {

    boolean isMatch(String fileName);

    String toHtml(ReferenceMap referenceMap, Reader reader);

}
