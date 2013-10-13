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
        allSymbolsById.put(symbol.getFullId(), symbol);

        // grouped by Type
        Map<String, S> symbolsById = symbolsByIdByType.get(symbol.getType());
        if  (symbolsById == null) {
            symbolsById = new HashMap<String, S>();
            symbolsByIdByType.put(symbol.getType(), symbolsById);
        }
        symbolsById.put(symbol.getId(), symbol);

        // grouped by Parent
//        if (symbol.getFullParentId() != null) {
            Map<String, Set<S>> childSymbolsByParentId = symbolsByParentIdByChildType.get(symbol.getType());
            if (childSymbolsByParentId == null) {
                childSymbolsByParentId = new HashMap<String, Set<S>>();
                symbolsByParentIdByChildType.put(symbol.getType(), childSymbolsByParentId);
            }
            Set<S> childSymbols = childSymbolsByParentId.get(symbol.getFullParentId());
            if (childSymbols == null) {
                childSymbols = new HashSet<S>();
                childSymbolsByParentId.put(symbol.getFullParentId(), childSymbols);
            }
            childSymbols.add(symbol);
//        }
    }

    public S lookup(String symbolId) {

        return allSymbolsById.get(symbolId);
    }

    public Collection<S> getAllFileSymbols() {
        Map<String, S> map = symbolsByIdByType.get(Symbol.Type.File);
        if (map == null) {
            map = Collections.emptyMap();
        }
        return map.values();
    }

    // eg all packages
    public Collection<S> getAllDirSymbols() {
        Map<String, S> map = symbolsByIdByType.get(Symbol.Type.Dir);
        if (map == null) {
            map = Collections.emptyMap();
        }
        return map.values();
    }

    // eg return all files for a package
    public Collection<S> getFileSymbolsInDir(String dirSymbolId) {
        Collection<S> collection = symbolsByParentIdByChildType.get(Symbol.Type.File).get(dirSymbolId);
        if (collection == null)  {
            collection = Collections.emptySet();
        }


        return collection;
    }
}
