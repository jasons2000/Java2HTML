package com.java2html.java_parser;

/*
 * Copyright (c) 2013. test license
 */

import com.java2html.references.Symbol;
import org.apache.commons.lang3.ObjectUtils;


public class JavaSymbol implements Symbol {

    private final String id;
    private final String fullParentId;
    private final Type type;
    private final String href;
    private final String fileLocation;

    public JavaSymbol(String href, String id, String fullParentId, Type type) {
        this.href = href;
        this.id = id;
        this.type = type;
        this.fullParentId = fullParentId;
        fileLocation = null;
    }

    public JavaSymbol(String href, String id, String fullParentId, String fileLocation) {
        this.href = href;
        this.id = id;
        this.type = Type.File;
        this.fullParentId= fullParentId;
        this.fileLocation = fileLocation;
    }


    @Override
    public String getFullId() {
        if (fullParentId == null || fullParentId.isEmpty()) {
            return id;
        }
        else {
            return fullParentId + "." + id;
        }
    }

    @Override
    public String getFullParentId() {
        return fullParentId;
    }

    @Override
    public String getDescriptiveName() {
        if (id == "") {
            return "[Default]";
        }
        else if (type == Type.Dir) {
            return getFullId();
        }
        else {
            return id;
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getHRef(String preDir) {
        if (!href.startsWith("http://")) {
            return preDir + href;

        }
        return href;
    }

    @Override
    public String getFileLocation() {
        return fileLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaSymbol that = (JavaSymbol) o;

        if (fullParentId != null ? !fullParentId.equals(that.fullParentId) : that.fullParentId != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fullParentId != null ? fullParentId.hashCode() : 0);
        return result;
    }

    @Override
    public Type getType() {
        return type;
    }



    @Override
    public String toString() {
        return getFullId();
    }
}
