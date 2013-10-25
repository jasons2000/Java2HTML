package com.java2html.references;

import com.java2html.internal.ParsingException;

import java.io.Reader;
import java.util.List;

/**
 * This will be used for JavaDoc  & JavaSrc
 * JavaSrc will check the wrapped JavaDoc
 */
public interface ReferenceParser<S extends Symbol> {

    S lookUp(String symbolId);
}


