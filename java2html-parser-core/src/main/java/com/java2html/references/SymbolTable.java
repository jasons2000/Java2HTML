package com.java2html.references;

/*
 * Copyright (c) 2013. test license
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
public class SymbolTable<S extends Symbol>  {

    private final Map<String, S> table = new HashMap<String, S>();

    public void add(S symbol) {
        table.put(symbol.getId(), symbol);
    }

    public S lookup(String symbolId) {

        return table.get(symbolId);
    }


    List<S> getAllFileSymbol() {

    }

    // eg all packages
    List<S> getAllLimitingScopes() {

    }

    // eg return all files for a package
    List<S> getScopedFiles(S limitingScope) {

    }
}
