package com.src2html.references;

/**
 * This will be used for JavaDoc  & JavaSrc
 * JavaSrc will check the wrapped JavaDoc
 */
public interface ReferenceParser<S extends Symbol> {

    S lookUp(String symbolId);
}


