package com.java2html.references;

/*
 * Copyright (c) 2013. test license
 */

import java.util.HashMap;
import java.util.Map;

public class SymbolTableByLanguage {

    private Map<String, SymbolTable> refsByLanguage = new HashMap<String, SymbolTable>();

    public void add(String namespace) {
        SymbolTable refs = refsByLanguage.get(namespace);
        if (refs == null) {
            refs = new SymbolTableMutable();
            refsByLanguage.put(namespace, refs);
        }

    }

}
