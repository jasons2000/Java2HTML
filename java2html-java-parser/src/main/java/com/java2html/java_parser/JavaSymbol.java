package com.java2html.java_parser;

/*
 * Copyright (c) 2013. test license
 */

import com.java2html.references.Symbol;

enum Type {
    Package,Class
}

public class JavaSymbol implements Symbol {

    private String id;
    private Type type;
    private String href;
    private String nameScope;

    public JavaSymbol(String href, String id, String nameScope, Type type) {
        this.href = href;
        this.id = id;
        this.type = type;
        this.nameScope = nameScope;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getNameScope() {
        return nameScope;
    }

    @Override
    public String getHRef() {
        return href;
    }

    @Override
    public String getFileLocation() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
