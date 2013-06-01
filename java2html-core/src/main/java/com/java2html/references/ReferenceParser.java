package com.java2html.references;

import java.io.InputStream;

public interface ReferenceParser {

    void parseReferences(ReferenceMapMutable referenceLookUp,
                         InputStream inputStream);

}
