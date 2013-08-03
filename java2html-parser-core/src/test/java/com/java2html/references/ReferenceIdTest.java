package com.java2html.references;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ReferenceIdTest {

    @Test
    public void test() {
        ReferenceIdMutable ref = new ReferenceIdMutable();
        ref.add("a").add("b").add("c").setHRef("url");

        assertEquals("url", ref.getHRef("a", "b", "c"));

        ReferenceId ref2 = ref.getSub("a", "b");

        assertEquals("url", ref2.getHRef("c"));
    }
}
