package com.java2html.references;


import com.java2html.internal.ParsingException;

import java.io.File;
import java.io.Reader;
import java.util.List;

public interface SourceParser<S extends Symbol>  {

    boolean isFileNameMatch(String fileName);

    void populateForReference(File reader) throws ParsingException;

    // null return means not parsable
    String snippetToHtml(String codeSnippet);

    String toHtml(File file, SourceParser<? extends Symbol>  otherLanguages) throws ParsingException;


    String getLanguageId();

    List<S> getAllFileSymbol();

    // eg all packages
    List<S> getAllLimitingScopes();

    // eg return all files for a package
    List<S> getScopedFiles(S limitingScope);

}
