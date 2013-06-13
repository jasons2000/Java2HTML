package com.java2html.references;

import java.io.Reader;

public interface ReferenceParser {

    void parseReferences(ReferenceMapMutable referenceLookUp,
                         Reader reader);

}
