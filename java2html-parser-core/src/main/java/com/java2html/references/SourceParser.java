package com.java2html.references;


import com.java2html.internal.ParsingException;

import java.io.Reader;

public interface SourceParser extends ReferenceParser {

    boolean isMatch(String fileName);

    String toHtml(ReferenceId referenceId, Reader reader) throws ParsingException;

}
