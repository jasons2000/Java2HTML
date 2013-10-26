package com.src2html.internal;

import java.io.IOException;

public class ParsingException extends IOException {

    public ParsingException(Throwable r) {
        super(r);
    }

    public ParsingException(String error) {
            super(error);
        }

}
