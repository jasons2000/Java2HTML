package com.java2html.references;

/*
 * Copyright (c) 2013. test license
 */

public interface SymbolTable<S extends Symbol> extends Iterable<S> {
    S lookup(String symbolId);
}
