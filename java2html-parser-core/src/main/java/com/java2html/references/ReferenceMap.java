package com.java2html.references;

public interface ReferenceMap {

    String getPathRoot();

    String getReference(ReferenceId referenceId);

    String getReference(String referenceId);
}
