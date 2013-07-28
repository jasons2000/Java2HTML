package com.java2html.references;

public class ReferenceMapMutable extends ReferenceMap {

    private String root;

    ReferenceMapMutable(String root) {
        this.root = root;

    }

    public void addReference(ReferenceId referenceId);
}
