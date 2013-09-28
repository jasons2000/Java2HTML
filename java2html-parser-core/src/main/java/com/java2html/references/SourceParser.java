package com.java2html.references;


import com.java2html.internal.ParsingException;

import java.io.File;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

public interface SourceParser<S extends Symbol>  {

    boolean isFileNameMatch(String fileName);

    void populateForReference(String fullPath, Reader reader) throws ParsingException;

    // null return means not parsable
    String snippetToHtml(String codeSnippet);

    String toHtml(File file, SourceParser<? extends Symbol>  otherLanguages) throws ParsingException;


    String getLanguageId();

    Collection<S> getAllFileSymbols();

    // eg all packages
    Collection<S> getAllDirSymbols();

    // eg return all files for a package
    Collection<S> getScopedFiles(S limitingScope);

}
