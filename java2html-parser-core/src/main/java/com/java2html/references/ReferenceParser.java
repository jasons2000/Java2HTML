package com.java2html.references;

import java.io.Reader;

public interface ReferenceParser {

    void parseReferences(ReferenceIdMutable referenceLookUp,
                         Reader reader);

}
