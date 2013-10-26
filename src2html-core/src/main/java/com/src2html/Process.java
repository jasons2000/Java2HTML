package com.src2html;

import com.src2html.references.ReferenceParser;
import com.src2html.references.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class Process {

    private List<ReferenceParser<? extends Symbol>> referenceParsers = new ArrayList<ReferenceParser<? extends Symbol>>();



    Process(List<ReferenceParser<? extends Symbol>> referenceParsers, List<String> dirs) {
        this.referenceParsers =referenceParsers;
    }




//    public void buildOutput() {
//
//        for (ReferenceParser<? extends Symbol> referenceParser : referenceParsers) {
//
//             referenceParser.parseReferences();
//
//        }
//
//
//
//    }


}
