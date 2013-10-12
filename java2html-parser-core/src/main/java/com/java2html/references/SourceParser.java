package com.java2html.references;


import com.java2html.internal.ParsingException;

import java.io.File;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

public interface SourceParser<S extends Symbol>  {

    boolean isFileNameMatch(String fileName);

    //fullpath just required for th string
    void populateForReference(String fullPath, Reader reader) throws ParsingException;

    // null return means not parsable
    String snippetToHtml(String codeSnippet);

    String toHtml(Reader reader, String pathToRoot,SourceParser<? extends Symbol>...  otherLanguages) throws ParsingException;

    String getLanguageId();

    SymbolTable<S> getSymbolTable();

}
