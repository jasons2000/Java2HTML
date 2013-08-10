package com.java2html.references;

import java.io.Reader;

public interface ReferenceParser {

    String getLanguageId();

    // added references for a particular language
    void parseReferences(SymbolTableMutable referenceLookUp,
                         String fullPathFilename,
                         Reader reader);
}
