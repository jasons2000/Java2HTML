package com.java2html.references;


import com.java2html.internal.ParsingException;

import java.io.Reader;
import java.util.List;

public interface SourceParser extends ReferenceParser {

    boolean isMatch(String fileName);

    String toHtml(SymbolTableByLanguage references, Reader reader) throws ParsingException;


}
