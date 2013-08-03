package com.java2html.references;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 */
public class ReferenceIdMutable implements ReferenceId {

    private static final String PATH_SEP = "//|\\\\";
    private final String element; // null is root
    private String href;

    private Map<String, ReferenceIdMutable> children;

    public ReferenceIdMutable() {
        element = null;
    }

    public ReferenceIdMutable(String element) {
        this.element = element;
    }

    public ReferenceIdMutable add(String element) {
        String[] elements = element.split(PATH_SEP);

        ReferenceIdMutable referenceId;
        ReferenceIdMutable that = this;
        for (String el : elements) {
            referenceId = new ReferenceIdMutable(el);
            if (that.children == null) {
                that.children = new LinkedHashMap<String, ReferenceIdMutable>();
            }
            that.children.put(element, referenceId);
            that = referenceId;
        }
        return that;
    }

    public void setHRef(String href) {
        this.href = href;
    }

    public ReferenceId getSub(String... subChild) {
        ReferenceIdMutable that = this;
        for (String string : subChild) {
            String[] ids = string.split(PATH_SEP);
            for (String id : ids) {
                if (that.children == null) {
                    return null;
                }
                else {
                    that = that.children.get(id);
                }
            }
        }
        return that;
    }

    @Override
    public String getHRef(String... subChild) {
        ReferenceId sub = getSub(subChild);
        if (sub != null) {
            return sub.getHRef();
        }
        return null;
    }

    @Override
    public String getHRef() {
        return href;
    }

}
