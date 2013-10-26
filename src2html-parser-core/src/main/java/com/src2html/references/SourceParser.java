package com.src2html.references;


import com.src2html.internal.ParsingException;

import java.io.Reader;
import java.util.Collection;

public interface SourceParser<S extends Symbol>  {

    boolean isFileNameMatch(String fileName);

    //fullpath just required for th string
    void populateForReference(String fullPath, Reader reader) throws ParsingException;

    // null return means not parsable
    String snippetToHtml(String codeSnippet);

    String toHtml(Reader reader, String pathToRoot,SourceParser<? extends Symbol>...  otherLanguages) throws ParsingException;

    String getLanguageId();

    public S lookup(String symbolId);

    public Collection<S> getAllFileSymbols();

    // eg all packages
    public Collection<S> getAllDirSymbols() ;

    // eg return all files for a package
    public Collection<S> getFileSymbolsInDir(String dirSymbolId);
}
