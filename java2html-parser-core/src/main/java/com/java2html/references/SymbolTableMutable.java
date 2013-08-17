package com.java2html.references;

/*
 * Copyright (c) 2013. test license
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SymbolTableMutable<S extends Symbol> implements SymbolTable<S> {

    private final Map<String, S> table = new HashMap<String, S>();

    public void add(S symbol) {
        table.put(symbol.getId(), symbol);
    }

    @Override
    public S lookup(String symbolId) {

        return table.get(symbolId);
    }

    @Override
    public Iterator<S> iterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
