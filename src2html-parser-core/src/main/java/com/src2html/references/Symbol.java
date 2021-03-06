package com.src2html.references;

/*
 * Copyright (c) 2013. test license
 */

public interface Symbol {
    String getFullId();
    String getId(); // scoped to parent id
    String getFullParentId();
    String getDescriptiveName();
    String getHRef(String preDir);
    String getFileLocation();

    Type getType();

    enum Type {
        Dir,File,Other
    }
}


