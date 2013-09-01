package com.java2html.references;

import com.java2html.internal.ParsingException;

import java.io.Reader;
import java.util.List;

/**
 * This will be used for JavaDoc
 */
public interface ReferenceParser<S extends Symbol> {

    String getLanguageId();

    // added references for a particular language
    void parseReferences(String fullPathFilename,
                         Reader reader)  throws ParsingException;


    List<S> getAllFileSymbol();

    // eg all packages
    List<S> getAllLimitingScopes();

    // eg return all files for a package
    List<S> getScopedFiles(S limitingScope);
}
