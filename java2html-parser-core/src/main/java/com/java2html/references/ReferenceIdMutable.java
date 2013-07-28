package com.java2html.references;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 */
public class ReferenceIdMutable implements ReferenceId {

    private final String element; // null is root

    private Map<String, ReferenceIdMutable> children;

    private ReferenceIdMutable(String element) {
        this.element = element;
    }

    public ReferenceIdMutable add(String element) {
        ReferenceIdMutable referenceId = new ReferenceIdMutable(element);
        if (children == null) {
            children = new LinkedHashMap<String, ReferenceIdMutable>();
        }
        children.put(element, referenceId);
        return referenceId;
    }

    public ReferenceId getSubReference(String... subChild) {
        ReferenceIdMutable that = this;
        for (String id : subChild) {
            if (that.children == null) {
                return null;
            }
            else {
                that = children.get(id);
            }
        }
        return that;
    }

}
