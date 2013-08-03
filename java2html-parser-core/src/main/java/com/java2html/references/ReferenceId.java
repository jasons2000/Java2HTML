package com.java2html.references;

public interface ReferenceId {


    public ReferenceId getSub(String... subChild);

    public String getHRef(String... subChild);

    public String getHRef();


//    private String languageSourceId;   // Java, C,, etc..
//    private String metaTypeId;         //ClassFile,JavaDoc,
//    private String[] subIdentifiers;


}
