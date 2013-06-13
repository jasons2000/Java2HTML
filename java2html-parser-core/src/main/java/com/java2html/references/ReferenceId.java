package com.java2html.references;

import java.util.Arrays;

public class ReferenceId {

    private String languageSourceId;
    private String metaTypeId;
    private String[] subIdentifiers;

    private ReferenceId(String languageSourceId, String metaTypeId,String[] subIdentifiers) {
        this.languageSourceId = languageSourceId;
        this.metaTypeId = metaTypeId;
        this.subIdentifiers = subIdentifiers;
    }

    static ReferenceId create(String languageSourceId, String metaTypeId) {
        return new ReferenceId(languageSourceId, metaTypeId, new String[0]);
    }

    ReferenceId createSub(String subIdentifier) {
        String[] newSubIdentifiers = Arrays.copyOf(subIdentifiers, subIdentifier.length() +1);

        return new ReferenceId(languageSourceId,metaTypeId, newSubIdentifiers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReferenceId that = (ReferenceId) o;

        if (!languageSourceId.equals(that.languageSourceId)) return false;
        if (!metaTypeId.equals(that.metaTypeId)) return false;
        if (!Arrays.equals(subIdentifiers, that.subIdentifiers)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = languageSourceId.hashCode();
        result = 31 * result + metaTypeId.hashCode();
        result = 31 * result + (subIdentifiers != null ? Arrays.hashCode(subIdentifiers) : 0);
        return result;
    }
}
