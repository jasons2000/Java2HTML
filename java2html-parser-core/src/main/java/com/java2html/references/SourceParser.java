package com.java2html.references;


import com.java2html.internal.ParsingException;

import java.io.Reader;
import java.util.List;

public interface SourceParser extends ReferenceParser {

    boolean isMatch(String fileName);

    String toHtml(SymbolTableByLanguage references, Reader reader) throws ParsingException;

    List<Symbol> getAllFiles();

    // eg all packages
    List<String> getAllLimitingScopes();

    // eg return all files for a package
    List<Symbol> getScopedFiles(String limitingScope);
}
