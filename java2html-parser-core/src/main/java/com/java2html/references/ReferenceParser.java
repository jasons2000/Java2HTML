package com.java2html.references;

import com.java2html.internal.ParsingException;

import java.io.Reader;

public interface ReferenceParser<S extends Symbol> {

    String getLanguageId();

    // added references for a particular language
    SymbolTableMutable<S> parseReferences(
                            String fullPathFilename,
                            Reader reader)  throws ParsingException;
}
