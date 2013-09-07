package com.java2html.references;


import com.java2html.internal.ParsingException;

import java.io.Reader;
import java.util.List;

public interface SourceParser<S extends Symbol>  {

    boolean isFileNameMatch(String fileName);

    String toHtml(ReferenceParser<S> references, Reader reader) throws ParsingException;

}
