package com.java2html.references;

/*
 * Copyright (c) 2013. test license
 */

import java.util.*;

public class SymbolTable<S extends Symbol>  {

    private final Map<String, S> fileTable = new HashMap<String, S>();
    private final Map<String, Set<S>> scopedFileTable = new HashMap<String, Set<S>>();

    public void add(S symbol) {

        if (symbol)
        fileTable.put(symbol.getId(), symbol);
    }

    public S lookup(String symbolId) {

        return table.get(symbolId);
    }


    Collection<S> getAllFileSymbol() {
       return fileTable.values();
    }

    // eg all packages
    Set<String> getAllLimitingScopes() {
        return scopedFileTable.keySet();

    }

    // eg return all files for a package
    Set<S> getScopedFiles(String limitingScope) {
        return scopedFileTable.get(limitingScope);


    }
}
