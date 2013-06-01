package com.java2html.references;


public interface SourceParser extends ReferenceParser {

    boolean isMatch(String fileName);

    String toHtml(ReferenceMap referenceMap, String source);

}
