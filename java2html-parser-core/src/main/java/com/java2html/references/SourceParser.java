package com.java2html.references;


import javax.swing.text.html.HTMLWriter;
import java.io.Reader;

public interface SourceParser extends ReferenceParser {

    boolean isMatch(String fileName);

    String toHtml(ReferenceMap referenceMap, HTMLWriter htmlWriter, Reader reader);

}
