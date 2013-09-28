package com.java2html.references;

/*
 * Copyright (c) 2013. test license
 */

import java.util.*;

public class SymbolTable<S extends Symbol>  {

    private final Map<String, S> allSymbolsById = new HashMap<String, S>();
    private final Map<Symbol.Type, Map<String, Set<S>>> symbolsByParentIdByChildType = new HashMap<Symbol.Type, Map<String, Set<S>>>();

    private final Map<Symbol.Type, Map<String, S>> symbolsByIdByType = new HashMap<Symbol.Type, Map<String, S>>();

    public void add(S symbol) {

        // Add Symbols
        allSymbolsById.put(symbol.getId(), symbol);

        // grouped by Type
        Map<String, S> symbolsById = symbolsByIdByType.get(symbol.getType());
        if  (symbolsById == null) {
            symbolsById = new HashMap<String, S>();
            symbolsByIdByType.put(symbol.getType(), symbolsById);
        }
        symbolsById.put(symbol.getId(), symbol);

        // grouped by Parent
        if (symbol.getParentSymbol() != null) {
            Map<String, Set<S>> childSymbolsByParentId = symbolsByParentIdByChildType.get(symbol.getType());
            if (childSymbolsByParentId == null) {
                childSymbolsByParentId = new HashMap<String, Set<S>>();
                symbolsByParentIdByChildType.put(symbol.getType(), childSymbolsByParentId);
            }
            Set<S> childSymbols = childSymbolsByParentId.get(symbol.getParentSymbol().getId());
            if (childSymbols == null) {
                childSymbols = new HashSet<S>();
                childSymbolsByParentId.put(symbol.getParentSymbol().getId(), childSymbols);
            }
            childSymbols.add(symbol);
        }
    }

    public S lookup(String symbolId) {

        return allSymbolsById.get(symbolId);
    }

    public Collection<S> getAllFileSymbols() {
       return symbolsByIdByType.get(Symbol.Type.File).values();
    }

    // eg all packages
    public Collection<S> getAllDirSymbols() {
        return symbolsByIdByType.get(Symbol.Type.Dir).values();
    }

    // eg return all files for a package
    public Set<S> getFileSymbolsInDir(String dirSymbolId) {
        return symbolsByParentIdByChildType.get(Symbol.Type.File).get(dirSymbolId);
    }
}
