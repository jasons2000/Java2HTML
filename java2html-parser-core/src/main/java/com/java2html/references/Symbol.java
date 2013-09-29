package com.java2html.references;

/*
 * Copyright (c) 2013. test license
 */

public interface Symbol<S extends Symbol> extends Comparable<S> {
    String getId();
    String getName();
    S getParentSymbol();
    String getHRef();
    String getFileLocation();

    Type getType();

    enum Type {
        Dir,File,Other
    }
}


