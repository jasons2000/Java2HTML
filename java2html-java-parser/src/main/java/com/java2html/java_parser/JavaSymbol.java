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

    public JavaSymbol(String href, String id, Type type) {
        this.href = href;
        this.id = id;
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getNameScope() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
