package com.java2html.references;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReferenceId {

    private final String element; // null is root
    private String href;

    private Map<String, ReferenceId> children;

    static public class Builder {
        private String element; // null is root
        private String href;
        private List<String> descendents = new ArrayList<String>();

        public Builder() {
        }

        public Builder(String element) {
            this.element = element;
        }

        public Builder add(String element) {
            descendents.add(element);
            return this;

        }


          public Builder setHref(String href) {
              this.href = href;
              return this;
          }

        public ReferenceId build() {

            ReferenceId referenceId = new ReferenceId(element);
            for (String descendant : descendents) {

            }

            public ReferenceId add(String element) {

                  if (children == null) {
                      children = new LinkedHashMap<String, ReferenceId>();
                  }
                  children.put(element, referenceId);
                  return referenceId;
              }
        }

    }

    // root
    private ReferenceId() {
        element = null;
    }

    private ReferenceId(String element) {
        this.element = element;
    }

//    private String languageSourceId;   // Java, C,, etc..
//    private String metaTypeId;         //ClassFile,JavaDoc,
//    private String[] subIdentifiers;

    public ReferenceId getReference(String... subChild) {
        ReferenceId that = this;
        for (String id : subChild) {
            if (that.children == null) {
                break;
            }
            else {
                that = children.get(id);
            }
        }
        return that;
    }

    public String getHref(String... subChild) {

        ReferenceId that =  getReference(subChild);
        if (that == null) return null;
        return that.href;
    }
}
