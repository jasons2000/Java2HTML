package com.java2html.references;

import org.junit.Test;

public class ReferenceIdTest {

    @Test
    public void test() {
        new ReferenceId.Builder().
//                ReferenceId ref = new ReferenceId().add("a").add("b").add("c").setHref("url");

        assertEquals("url", ref.getHref("a", "b", "c"));

        ref = ref.getReference("a","b");

        assertEquals("url", ref.getHref("c"));
    }
}
