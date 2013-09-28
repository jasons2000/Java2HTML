package com.java2html.java_parser;

/*
 * Copyright (c) 2013. test license
 */

import com.java2html.references.Symbol;


public class JavaSymbol implements Symbol<JavaSymbol> {

    private String id;
    private Type type;
    private String href;
    private JavaSymbol parent; // optional , unknown or don't care

    public JavaSymbol(String href, String id, JavaSymbol parent, Type type) {
        this.href = href;
        this.id = id;
        this.type = type;
        this.parent = parent;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public JavaSymbol getParentSymbol() {
        return parent;
    }

    @Override
    public String getHRef() {
        return href;
    }

    @Override
    public String getFileLocation() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaSymbol that = (JavaSymbol) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
